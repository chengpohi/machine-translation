package com.github.chengpohi.utils

import java.io.StringReader

import com.github.chengpohi.algorithm.Sentence
import com.github.chengpohi.analyzer.en.NgramCorpus
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import org.apache.lucene.analysis.{Analyzer, TokenStream, Tokenizer}

import scala.collection.mutable.ArrayBuffer

/**
  * Created by chengpohi on 20/02/2017.
  */
object TokenStreamUtils {

  implicit class TokenStringHelper(s: String) {
    def map[T <: Tokenizer](tokenizer: T): List[String] = {
      tokenizer.setReader(new StringReader(s))
      tokenizer.toList
    }

    def tokenize[T <: Tokenizer](tokenizer: T): T = {
      tokenizer.setReader(new StringReader(s))
      tokenizer
    }

    def analyze[T <: Analyzer](analyzer: T): TokenStream = {
      analyzer.tokenStream("", new StringReader(s))
    }

    def map(analyzer: Analyzer): List[String] = {
      analyzer.tokenStream("", new StringReader(s)).toList
    }

    def reorder(implicit corpus: NgramCorpus): String = Sentence(s, corpus)
  }

  implicit class TokenStreamHelper(s: TokenStream) {
    def toList: List[String] = {
      var ar = new ArrayBuffer[String]()
      s.reset()
      val termAtt: CharTermAttribute = s.getAttribute(classOf[CharTermAttribute])
      while (s.incrementToken()) {
        ar.append(termAtt.toString)
      }
      s.end()
      s.close()
      ar.toList
    }

    def print: Unit = {
      s.toList.zipWithIndex.map(i => (i._2, i._1)).foreach(println)
    }
  }

}
