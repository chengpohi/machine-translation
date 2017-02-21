package com.github.chengpohi.analyzer.en

import com.github.chengpohi.analyzer.MTTest
import com.github.chengpohi.utils.TokenStreamUtils._
import org.apache.lucene.analysis.BaseTokenStreamTestCase._
import org.apache.lucene.analysis.TokenStream

/**
  * Created by chengpohi on 13/02/2017.
  */

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
