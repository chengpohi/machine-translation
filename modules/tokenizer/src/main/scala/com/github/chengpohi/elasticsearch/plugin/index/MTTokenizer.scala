package com.github.chengpohi.elasticsearch.plugin.index

import org.apache.lucene.analysis.util.CharTokenizer

/**
  * Created by chengpohi on 13/02/2017.
  */
class MTTokenizer extends CharTokenizer {
  override def isTokenChar(c: Int): Boolean = !Character.isWhitespace(c)
}
