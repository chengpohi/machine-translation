package com.github.chengpohi.elasticsearch.plugin.index

import java.io.StringReader

import com.github.chengpohi.elasticsearch.plugin.analysis.{MTEnAnalyzer, MTEnTokenizer}
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import org.scalatest._

import scala.collection.mutable.ArrayBuffer

/**
  * Created by chengpohi on 13/02/2017.
  */
abstract class MTTest extends FlatSpec with Matchers

class MTTokenizerTest extends MTTest {

  def tokenize(source: Iterable[String]): Iterable[List[String]] = {
    source.map(str => {
      var ar = new ArrayBuffer[String]()
      val tokenizer = new MTEnTokenizer()
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
    tokenize(List("It's a ?1 Hello were World, Jack, Chen.?, c++, c#", "Foo Bar")).toList.foreach(println)
    val analyzer = new MTEnAnalyzer
    val s = analyzer.tokenStream("", new StringReader("C++ Hello World, Test Chengpohi"))
    s.reset()
    val termAtt: CharTermAttribute = s.getAttribute(classOf[CharTermAttribute])
    while(s.incrementToken()) {
      println(termAtt.toString)
    }
    s.end()
    s.close()


    /* val tokenizer = new MTTokenizer()
     tokenizer.setReader(new StringReader("?1 Hello World, Jack, Chen.?"))
     assertTokenStreamContents(tokenizer, Array("1", "Hello", "World", "Jack", "Chen"))*/
  }
}
