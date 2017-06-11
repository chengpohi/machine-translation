package com.github.chengpohi.mt.translator

import com.github.chengpohi.registry.ELKCommandRegistry

import scala.concurrent.Future

case class Word(en: String, zh: String)

class Translator {
  def translate(str: String): Future[Stream[Word]] = {
    import ELKCommandRegistry.elasticDSL._
    DSL {
      search in "en" / "cn" term str
    }.as[Word]
  }
}

object Translator {
  def apply: Translator = new Translator()
}
