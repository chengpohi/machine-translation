package com.github.chengpohi.analyzer.en

class MTEnNgramWordTokenizer(sentence: String) {
  def tokenize: Stream[(String, String)] = sentence
    .split("\\s+")
    .sliding(2)
    .map(r => (r.head, r.last))
    .toStream
}

object MTEnNgramWordTokenizer {
  def apply(s: String): Stream[(String, String)] =
    new MTEnNgramWordTokenizer(s).tokenize
}
