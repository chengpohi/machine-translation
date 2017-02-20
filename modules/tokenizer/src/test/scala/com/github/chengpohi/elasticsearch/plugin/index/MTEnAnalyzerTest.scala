package com.github.chengpohi.elasticsearch.plugin.index

import com.github.chengpohi.TokenStreamUtils._
import com.github.chengpohi.elasticsearch.plugin.analysis.{MTEnAnalyzer, MTEnTokenizer}
import org.apache.lucene.analysis.BaseTokenStreamTestCase._
import org.apache.lucene.analysis.TokenStream
import org.scalatest._

/**
  * Created by chengpohi on 13/02/2017.
  */
trait MTTest extends FlatSpec with Matchers with BeforeAndAfter

class MTEnAnalyzerTest extends MTTest {
  it should "generate tokens" in {
    val str = "It's a ?1 .?, c++, c#, Java"
    val tokenizer: MTEnTokenizer = str.tokenize(new MTEnTokenizer)
    assertTokenStreamContents(tokenizer, Array[String]("It's", "a", "1", "c++", "c#", "Java"))

    val analyzer: TokenStream = str.analyze(new MTEnAnalyzer)
    assertTokenStreamContents(analyzer, Array[String]("it's", "a", "1", "c++", "c#", "java"))

    str.map(new MTEnAnalyzer).foreach(println)
  }
}
