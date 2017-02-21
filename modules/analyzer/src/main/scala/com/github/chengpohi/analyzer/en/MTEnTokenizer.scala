package com.github.chengpohi.analyzer.en

import org.apache.lucene.analysis.util.CharTokenizer

/**
  * Created by chengpohi on 20/02/2017.
  */
class MTEnTokenizer extends CharTokenizer {
  val SYMBOL = Array('+', '#', '\'')

  override def isTokenChar(c: Int): Boolean = {
    !Character.isWhitespace(c) && Character.isLetterOrDigit(c) || SYMBOL.contains(c)
  }
}
