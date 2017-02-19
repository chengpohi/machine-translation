package com.github.chengpohi.elasticsearch.plugin.index

import com.github.chengpohi.TokenStreamUtils._
import com.github.chengpohi.elasticsearch.plugin.analysis.{MTEnAnalyzer, MTEnTokenizer}
import org.scalatest._

/**
  * Created by chengpohi on 13/02/2017.
  */
abstract class MTTest extends FlatSpec with Matchers

class MTTokenizerTest extends MTTest {
  it should "generate tokens" in {
    val str = "It's a ?1 Hello were World, Jack, Chen.?, c++, c#"
    str.map(new MTEnTokenizer).foreach(println)
    println("-" * 20)
    str.map(new MTEnAnalyzer).foreach(println)
  }
}
