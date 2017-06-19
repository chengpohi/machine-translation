package com.github.chengpohi.mt.translator

import com.github.chengpohi.api.ElasticDSL
import com.github.chengpohi.connector.ELKDSLConfig
import com.github.chengpohi.registry.ELKDSLContext
import org.elasticsearch.action.index.IndexResponse

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.io.Source
import scala.util.{Failure, Success, Try}
import scala.xml.{Elem, Node}
case class Term(word: String, ex: List[String]) {
  def id: String = word.trim.replaceAll("\\s+", "-").toLowerCase
}
case class IndexWord(word: String,
                     ps: Option[String],
                     ex: List[String],
                     target: Map[String, List[String]])

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

  def save(word: Word)(source: String): Future[List[IndexResponse]] = {
    val w = IndexWord(word.word,
                      word.ps,
                      word.ex,
                      word.target.mapValues(w => List(w.word)))

    val res1 = word.target.map {
      case (s, l) => {
        DSL {
          index into "word" / s doc l id l.id
        }
      }
    }
    val res2 = DSL {
      index into "word" / source doc w id word.id
    }
    Future.sequence(res1.toList :+ res2)
  }
}

object WordIndexer extends ELKDSLConfig with ELKDSLContext {
  def main(args: Array[String]): Unit = {
    import dsl._

    val path = "train_data/en_cn.xml"
    val extractor: WordExtractor = new WordExtractor("en", "zh")
    val indexer: WordIndexer = new WordIndexer()

    val res: Stream[Future[List[IndexResponse]]] = extractor
      .extract(Source.fromFile(path))
      .map(w => indexer.save(w)("en"))

    val sequence = Future
      .sequence(res)
      .map(_.flatten.foreach(s => {
        println(s.toJson)
      }))
    sequence.onComplete {
      case Success(s) => println("Finish Extracting ...")
      case Failure(f) => f.printStackTrace()
    }
    Await.result(sequence, Duration.Inf)
  }
}
