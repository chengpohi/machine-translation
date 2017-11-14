package com.github.chengpohi.analyzer.zh

import com.github.chengpohi.analyzer.MTTest
import com.github.chengpohi.utils.TokenStreamInstances._
/*import org.wltea.analyzer.cfg.Configuration
import org.wltea.analyzer.lucene.IKTokenizer
import org.apache.lucene.analysis.BaseTokenStreamTestCase._

/**
 * Created by chengpohi on 21/02/2017.
 */
class IKAnalyzerTest extends MTTest {
  implicit val tokenizer = new IKTokenizer(new Configuration())
  it should "generate tokens by ik analyzer" in {
    val str = "为什么c++处理有序数组比无序数组快?"
    val tokenStream = str.tokenize()
    //assertTokenStreamContents(tokenizer, Array[String]("为什么", "c++", "处理", "有序数组", "无序数组", "快"))
    tokenStream.toList.foreach(println)

    //str.map(new MTEnAnalyzer).foreach(println)
  }
}*/
