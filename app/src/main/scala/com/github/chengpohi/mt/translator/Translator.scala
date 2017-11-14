package com.github.chengpohi.mt.translator

import com.github.chengpohi.api.ElasticDSL

import scala.concurrent.Future

class Translator(implicit dsl: ElasticDSL) {

  import scala.concurrent.ExecutionContext.Implicits.global
  import dsl._

  def translate(str: String, source: String): Future[Stream[Word]] = {
    DSL {
      search in "word" / source must List("word" -> str)
    }.as[IndexWord]
      .map(s =>
        s.flatMap(i => {
          i.target.flatMap {
            case (t, ls) =>
              val res = ls.map(l => {
                val term = DSL {
                  search in "word" / t where id equal l
                }.as[Term]
                term.map {
                  case a #:: b =>
                    Word(i.word, i.ps, i.ex, Map(t -> a))
                  case _ =>
                    Word(i.word, i.ps, i.ex, Map())
                }
              })
              Future.sequence(res)
          }
        }))
  }
}

object Translator {
  def apply(implicit dsl: ElasticDSL): Translator = new Translator()
}
