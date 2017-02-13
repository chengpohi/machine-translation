package com.github.chengpohi.tokenizer

import java.io.{File, StringReader}

import com.github.chengpohi.elasticsearch.plugin.index.MTTokenizer
import org.apache.lucene.analysis.core.WhitespaceTokenizer
import org.apache.lucene.analysis.custom.CustomAnalyzer
import org.apache.lucene.analysis.standard.StandardTokenizer
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute

import scala.collection.mutable.ArrayBuffer
import scala.io.Source

/**
  * Created by chengpohi on 02/02/2017.
  */
trait Language

case object EN extends Language

case object CN extends Language

class TokenizerT(language: Language) {
  private val EN_NON_BREAKING_FILE = new File("./nonbreaking_prefixes/en_nonbreaking_prefix.txt")
  lazy val EN_NON_BREAKING_PREFIX: Stream[String] =
    Source.fromFile(EN_NON_BREAKING_FILE).getLines().toStream.filter(!_.startsWith("#"))
  lazy val NON_BREAKING_PREFIX_MAP = Map(EN -> EN_NON_BREAKING_PREFIX)

  private val MAX_TOKEN_LENGTH = 255

}

object TokenizerT {
  def apply(language: String): TokenizerT = new TokenizerT(EN)

  def main(args: Array[String]): Unit = {
  }
}
