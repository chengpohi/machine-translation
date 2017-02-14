package com.github.chengpohi.elasticsearch.plugin.index

import java.io.StringReader

import org.apache.lucene.analysis.standard.StandardTokenizer
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import org.scalatest._

import scala.collection.mutable.ArrayBuffer

/**
  * Created by chengpohi on 13/02/2017.
  */
abstract class MTTest extends FlatSpec with Matchers

class MTTokenizerTest extends MTTest {

  import org.apache.lucene.analysis.BaseTokenStreamTestCase._

  def tokenize(source: Iterable[String]): Iterable[List[String]] = {
    source.map(str => {
      var ar = new ArrayBuffer[String]()
      val tokenizer = new MTTokenizer()
      tokenizer.setReader(new StringReader(str))
      tokenizer.reset()
      val termAtt: CharTermAttribute = tokenizer.getAttribute(classOf[CharTermAttribute])
      while (tokenizer.incrementToken()) {
        ar.append(termAtt.toString)
      }
      tokenizer.end()
      tokenizer.close()
      ar.toList
    })
  }


  it should "generate tokens" in {
    tokenize(List("a ?1 Hello World, Jack, Chen.?", "Foo Bar")).toList.foreach(println)

    /* val tokenizer = new MTTokenizer()
     tokenizer.setReader(new StringReader("?1 Hello World, Jack, Chen.?"))
     assertTokenStreamContents(tokenizer, Array("1", "Hello", "World", "Jack", "Chen"))*/
  }
}
