package com.github.chengpohi.elasticsearch.plugin.analysis

import org.apache.lucene.analysis.util.CharTokenizer

/**
  * Created by chengpohi on 20/02/2017.
  */
class MTEnTokenizer extends CharTokenizer {
  val PUNCTUATION = Array('+', '#', '\'')

  override def isTokenChar(c: Int): Boolean = {
    !Character.isWhitespace(c) && Character.isLetterOrDigit(c) || PUNCTUATION.contains(c)
  }
}
