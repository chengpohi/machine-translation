package com.github.chengpohi.mt

import com.github.chengpohi.analyzer.en.NgramCorpus

import scala.io.Source

object NgramExplorer extends App {
  import com.github.chengpohi.utils.TokenStreamUtils._
  val data = Source.fromResource("training/corpus.txt")
    .getLines()
    .toStream
  implicit val corpus = NgramCorpus(data)

  val test1 = ("I", "have")
  val test2 = ("have", "I")
  val terms1 = corpus.terms(test1)
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
