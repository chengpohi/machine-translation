package com.github.chengpohi.utils

import java.io.StringReader

import com.github.chengpohi.algorithm.Sentence
import com.github.chengpohi.analyzer.en.{MTEnTokenizer, NgramCorpus}
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import org.apache.lucene.analysis.{
Analyzer,
LowerCaseFilter,
TokenStream,
Tokenizer
}

import scala.collection.mutable.ArrayBuffer
import java.io.Reader

import scalaz._
import Scalaz._

/**
  * Created by chengpohi on 20/02/2017.
  */
object TokenStreamInstances {
  implicit class TokenStringHelper(s: String) {
    def map[T <: Tokenizer](tokenizer: T): List[String] = {
      tokenizer.setReader(new StringReader(s))
      tokenizer.toList
    }

    def tokenize()(implicit tokenizer: Tokenizer): TokenStream = {
      tokenizer.setReader(new StringReader(s))
      tokenizer
    }

    def analyze()(implicit analyzer: Analyzer): TokenStream = {
      analyzer.tokenStream("", new StringReader(s))
    }

    def reorder(implicit corpus: NgramCorpus): String = Sentence(s, corpus)
  }

  implicit class tokenStreamInstance(s: TokenStream) {
    def toList: List[String] = {
      var ar = new ArrayBuffer[String]()
      s.reset()
      val termAtt: CharTermAttribute =
        s.getAttribute(classOf[CharTermAttribute])
      while (s.incrementToken()) {
        ar.append(termAtt.toString)
      }
      s.end()
      s.close()
      ar.toList
    }
  }

  import Kleisli._
  import std.option._

  val mtEnTokenizer = kleisli[Option, String, TokenStream](
    (s: String) => {
      val t = new MTEnTokenizer
      t.setReader(new StringReader(s))
      some(t)
    }
  )
  val lowerCaseFilter = kleisli[Option, TokenStream, TokenStream](
    (tokenStream: TokenStream) => some(new LowerCaseFilter(tokenStream))
  )

  implicit object tokenStreamInstance extends Show[TokenStream] {
    override def shows(t: TokenStream): String = t.toList.mkString(System.lineSeparator())
  }
}
