package com.github.chengpohi.algorithm

import com.github.chengpohi.analyzer.en.NgramCorpus


case class TF(count: Int, freq: Double)

case class GramTerm(left: String, right: String, tf: TF = TF(0, 0d)) {
  override def toString: String = s"""($left,$right)"""
}

class Sentence(ngramCorpus: NgramCorpus) {
  def reorder(sentence: String): String = {
    val tokenizer = sentence.split("\\s+")

    val combinations = tokenizer.combinations(2).flatMap(i =>
      List(GramTerm(i(0), i(1)), GramTerm(i(1), i(0)))
    ).toList

    val r = combinations.map(i =>
      i.copy(tf = ngramCorpus.terms(i))
    ).filter(_.tf.freq > 0)
    val t = r.groupBy(_.left).map(i => i._2.sortBy(_.tf.freq).last).toList
    concatSentence(t, tokenizer.size)
  }

  private def concatSentence(r: List[GramTerm], n: Int): String = {
    val head = r.find(i => !r.exists(_.right == i.left))
    head match {
      case Some(t) => {
        (1 until n).foldLeft(List(t.left))((a, b) => {
          a :+ r.find(i => i.left == a.last).map(_.right).getOrElse("")
        }).mkString(" ").trim
      }
      case _ => "Concat error, There is a circle in this sentence"
    }
  }
}

object Sentence {
  def apply(sentence: String, ngramCorpus: NgramCorpus): String =
    new Sentence(ngramCorpus).reorder(sentence)
}
