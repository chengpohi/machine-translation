package com.github.chengpohi.mt.runner

import com.github.chengpohi.algorithm.GramTerm
import com.github.chengpohi.analyzer.en.NgramCorpus

import scala.io.Source

object NgramExplorer extends App {

  import com.github.chengpohi.utils.TokenStreamInstances._

  val data = Source
    .fromResource("training/corpus.txt")
    .getLines()
    .toStream
  implicit val corpus = NgramCorpus(data)

  val test1 = GramTerm("I", "have")
  //P("have" | "I") * P("e")
  val terms1 = corpus.terms(test1)
  val test2 = GramTerm("have", "I")
  //P("I" | "have") * P("e")
  val terms2 = corpus.terms(test2)
  println("total size: " + corpus.size)
  println(s"${test1} size: " + terms1.freq)
  println(s"${test2} size: " + terms2.freq)

  println(s"P(${test1}) = " + terms1.freq)
  println(s"P(${test2}) = " + terms2.freq)

  //I have never seen a better programming language
  val sentence = "have programming a seen never I language better"
  val result = sentence.reorder
  println(result)

  assert(result == "I have never seen a better programming language")
}
