package com.github.chengpohi.tokenizer

import java.io.{File, StringReader}

import org.apache.lucene.analysis.standard.StandardTokenizer
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute

import scala.collection.mutable.ArrayBuffer

/**
  * Created by chengpohi on 02/02/2017.
  */
class TokenizerT(language: String) {
  private val MAX_TOKEN_LENGTH = 255

  def tokenize(source: Iterator[String]): Iterator[List[String]] = {
    source.map(str => {
      var ar = new ArrayBuffer[String]()
      val tokenizer = new StandardTokenizer()
      tokenizer.setMaxTokenLength(MAX_TOKEN_LENGTH)
      tokenizer.setReader(new StringReader(str))
      val termAtt = tokenizer.getAttribute(classOf[CharTermAttribute])
      tokenizer.reset()
      while (tokenizer.incrementToken()) {
        ar.append(termAtt.toString)
      }
      tokenizer.end()
      tokenizer.close()
      ar.toList
    })
  }
}

object TokenizerT {
  def apply(language: String): TokenizerT = new TokenizerT(language)

  def main(args: Array[String]): Unit = {
    val t = TokenizerT("en")
    println(t.tokenize(List("Hello World, Jack, Chen", "Foo Bar").iterator).toList)
  }
}
