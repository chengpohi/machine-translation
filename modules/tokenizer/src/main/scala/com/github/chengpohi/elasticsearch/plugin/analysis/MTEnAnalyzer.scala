package com.github.chengpohi.elasticsearch.plugin.analysis

import java.io.Reader

import org.apache.lucene.analysis.{Analyzer, LowerCaseFilter}
import org.apache.lucene.analysis.Analyzer.TokenStreamComponents

/**
  * Created by chengpohi on 20/02/2017.
  */
class MTEnAnalyzer extends Analyzer {
  override def createComponents(fieldName: String): TokenStreamComponents = {
    val tokenizer = new MTEnTokenizer
    val tok = new LowerCaseFilter(tokenizer)
    new TokenStreamComponents(tokenizer, tok) {
      override def setReader(reader: Reader) {
        super.setReader(reader)
      }
    }
  }
}
