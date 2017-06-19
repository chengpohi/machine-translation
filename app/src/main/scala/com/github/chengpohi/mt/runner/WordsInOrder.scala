package com.github.chengpohi.mt.runner

import com.github.chengpohi.connector.ELKDSLConfig
import com.github.chengpohi.registry.ELKDSLContext

object WordsInOrder extends ELKDSLConfig with ELKDSLContext {
  import dsl._

  def main(args: Array[String]): Unit = {
    val res = DSL {
      analyze text "have programming a seen never I language better" tokenizer "ngram"
    }.toJson
    DSL {
      bulk index "test" / "corpus" doc List(
        List(("text", "I have never seen a better programming language"))
      )
    }
    println(res)
  }

  def mockNgram(): Unit = {
    //I have never seen a better programming language
    val sentence = "have programming a seen never I language better"
    sentence
      .split("\\s+")
      .combinations(2)
      .map(i => i.mkString(","))
      .foreach(println)
  }
}
