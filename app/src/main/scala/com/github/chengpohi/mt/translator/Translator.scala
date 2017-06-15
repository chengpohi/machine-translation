package com.github.chengpohi.mt.translator

import com.github.chengpohi.api.ElasticDSL
import org.elasticsearch.action.index.IndexResponse

import scala.concurrent.Future
import scala.io.Source
import scala.util.{Failure, Success, Try}
import scala.xml.{Elem, Node}

case class Term(word: String, ex: List[String])
case class Word(word: String,
                ps: Option[String],
                ex: List[String],
                target: Map[String, Term]) {
  def id: String = word.trim.replaceAll("\\s+", "-").toLowerCase
}

class WordExtractor(s: String, d: String) {
  def extract(source: Source): Stream[Word] =
    source
      .getLines()
      .toStream
      .filter(!_.isEmpty)
      .flatMap(i => {
        val es: Option[Elem] = Try(scala.xml.XML.loadString(i)).toOption
        es match {
          case Some(e) =>
            val title = e.attributes.asAttrMap("d:title")
            val id = e.attributes.asAttrMap("id")
            val trans = e \\ "_" filter attr("trans")
            val ps = e \\ "_" filter attr("ps")
            val ex = e \\ "_" filter attr("exg")
            val exg = ex \\ "_" filter attr("ex")
            val target =
              Try(trans.map(_.text.trim).toList).getOrElse(List()) match {
                case h :: t => Map(d -> Term(h, t))
                case _ => Map[String, Term]()
              }
            id match {
              case t if id.startsWith("e") =>
                Some(
                  Word(
                    title,
                    Try(ps.head.text).toOption,
                    Try(exg.map(_.text.trim).toList).getOrElse(List()),
                    target
                  ))
              case _ => None
            }

          case None => None
        }
      })

  def attr(value: String)(node: Node): Boolean = {
    node.attributes.exists(_.value.text == value)
  }
}

class WordIndexer(implicit dsl: ElasticDSL) {

  import dsl._

  def save(word: Word)(source: String): Future[IndexResponse] =
    need {
      index into "word" / source doc word id word.id
    }
}

object WordExtractor {
  def main(args: Array[String]): Unit = {
    import scala.concurrent.ExecutionContext.Implicits.global
    implicit val dsl = com.github.chengpohi.registry.ELKDSLContext.dsl
    import dsl._

    val path = "train_data/en_cn.xml"
    val extractor: WordExtractor = new WordExtractor("en", "zh")
    val indexer: WordIndexer = new WordIndexer()

    val res: Stream[Future[IndexResponse]] = extractor
      .extract(Source.fromFile(path))
      .map(w => indexer.save(w)("en"))

    res.foreach(i => {
      i.onComplete {
        case Success(j) => println(j.toJson)
        case Failure(j) => println(j)
      }
    })
  }
}

class Translator(implicit dsl: ElasticDSL) {

  import dsl._

  def translate(str: String, source: String): Future[Stream[Word]] = {
    DSL {
      search in "word" / source must List("word" -> str)
    }.as[Word]
  }
}

object Translator {
  def apply(implicit dsl: ElasticDSL): Translator = new Translator()
}
