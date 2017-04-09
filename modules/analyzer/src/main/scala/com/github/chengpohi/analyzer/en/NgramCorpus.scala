package com.github.chengpohi.analyzer.en

import com.github.chengpohi.algorithm.TF

class NgramCorpus(grams: Stream[(String, String)]) {
  def terms(s: (String, String)): TF = {
    val count = grams.count(c => c == s)
    TF(count, count.toDouble / grams.size)
  }

  def size: Int = grams.size
}

object NgramCorpus {
  def apply(s: Stream[String]): NgramCorpus = {
    new NgramCorpus(s.flatMap(i => MTEnNgramWordTokenizer(i)))
  }
}
