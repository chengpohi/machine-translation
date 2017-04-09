package com.github.chengpohi.analyzer.en

import com.github.chengpohi.algorithm.{GramTerm, TF}

case class NgramCorpus(terms: List[String], grams: Stream[GramTerm]) {
  //P(f | e)
  def terms(s: GramTerm): TF = {
    val count = grams.count(c => c == s)
    //P(f | e) = number-of-occurrences(ey) / number-of-occurrences(y)
    TF(count, count.toDouble / terms.count(c => c == s.right))
  }

  def size: Int = grams.size
}

object NgramCorpus {
  def apply(s: Stream[String]): NgramCorpus = {
    s.map(i => MTEnNgramWordTokenizer(i))
      .foldLeft(NgramCorpus(List(), Stream.empty))((a, b) => {
        NgramCorpus(a.terms ++ b.terms, a.grams ++ b.grams)
      })
  }
}
