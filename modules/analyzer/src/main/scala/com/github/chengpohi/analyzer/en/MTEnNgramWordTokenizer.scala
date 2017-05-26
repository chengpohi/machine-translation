package com.github.chengpohi.analyzer.en

import com.github.chengpohi.algorithm.GramTerm

class MTEnNgramWordTokenizer(sentence: String) {
  def tokenize: NgramCorpus = {
    val terms = sentence
      .split("\\s+")
      .toList
    val grams = terms
      .sliding(2)
      .map(r => GramTerm(r.head, r.last))
      .toStream
    NgramCorpus(terms, grams)
  }
}

object MTEnNgramWordTokenizer {
  def apply(s: String): NgramCorpus =
    new MTEnNgramWordTokenizer(s).tokenize
}
