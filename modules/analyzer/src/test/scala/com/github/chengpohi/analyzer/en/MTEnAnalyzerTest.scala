package com.github.chengpohi.analyzer.en

import com.github.chengpohi.analyzer.MTTest
import com.github.chengpohi.utils.TokenStreamUtils._
import org.apache.lucene.analysis.BaseTokenStreamTestCase._
import org.apache.lucene.analysis.TokenStream

import scalaz.Scalaz._
import scalaz._
import scalaz.effect.IO

/**
  * Created by chengpohi on 13/02/2017.
  */
class MTEnAnalyzerTest extends MTTest {
  implicit val tokenizer = new MTEnTokenizer
  implicit val analyzer = new MTEnAnalyzer

  it should "generate tokens" in {
    val str = "It's a ?1 .?, c++, c#, Java"

    val tokenStream: TokenStream = str.tokenize()

    assertTokenStreamContents(
      tokenStream,
      Array[String]("It's", "a", "1", "c++", "c#", "Java"))

    val analyzeStream: TokenStream = str.analyze()
    assertTokenStreamContents(
      analyzeStream,
      Array[String]("it's", "a", "1", "c++", "c#", "java"))
  }

  it should "generate" in {
    val str =
      "Why is it faster to process a sorted array than an unsorted array?"
    val result = some(str) >>= (mtEnTokenizer >=> lowerCaseFilter)

    result.foreach(_.println)
  }


  it should "generate tokens from file" in {
    val result = IO {
      val source = scala.io.Source.fromInputStream(this.getClass.getResourceAsStream("/input.txt"))
      source.getLines().toStream
    }.map(_.map(l => {
      some(l) >>= (mtEnTokenizer >=> lowerCaseFilter)
    }))
    result.unsafePerformIO().foreach {
      case Some(t) => t.println
    }
  }
}
