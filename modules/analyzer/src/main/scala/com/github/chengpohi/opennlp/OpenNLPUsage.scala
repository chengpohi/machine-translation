package com.github.chengpohi.opennlp

import java.io.{FileInputStream, InputStream}

import opennlp.tools.chunker.{ChunkerME, ChunkerModel}
import opennlp.tools.namefind.{NameFinderME, TokenNameFinderModel}
import opennlp.tools.parser.{Parse, Parser, ParserFactory, ParserModel}
import opennlp.tools.postag.{POSModel, POSSample, POSTaggerME}
import opennlp.tools.sentdetect.{SentenceDetectorME, SentenceModel}
import opennlp.tools.tokenize.{SimpleTokenizer, TokenizerME, TokenizerModel, WhitespaceTokenizer}

import scalaz.Scalaz._

object OpenNLPUsage extends App {
  tokenizer
  sentence
  nameentity
  speech
  parsing
  chunking
}

object tokenizer {
  "simple tokenizer".println
  val simpleTokenizer = SimpleTokenizer.INSTANCE
  simpleTokenizer.tokenize("hello123 world").foreach(println)

  "whitespace tokenizer".println
  val whitespaceTokenizer = WhitespaceTokenizer.INSTANCE
  whitespaceTokenizer.tokenize("foo123 bar").foreach(println)

  "Tokenizer Model".println
  val inputStream: InputStream = new FileInputStream("./models/opennlp/en-token.bin")
  val tokenizerModel = new TokenizerModel(inputStream)
  val tokenizerME = new TokenizerME(tokenizerModel)
  tokenizerME.tokenize("it was a beautiful place").foreach(println)
}

object sentence {
  "sentence detector".println
  val inputStream: InputStream = new FileInputStream("./models/opennlp/en-sent.bin")

  val model = new SentenceModel(inputStream)
  val detector = new SentenceDetectorME(model)
  val sentence = "sentence detect, it's foo bar. How are you ? Do you want to have a drink?"
  detector.sentDetect(sentence).foreach(println)

  "sentence probabilities".println
  detector.getSentenceProbabilities.foreach(println)

  "sentence pos detect".println
  detector.sentPosDetect(sentence).foreach(println)
}

object nameentity {
  "Name Entity Recognition".println
  val inputStream: InputStream = new FileInputStream("./models/opennlp/en-ner-person.bin")
  val model = new TokenNameFinderModel(inputStream)
  val nameFinder = new NameFinderME(model)
  nameFinder.find(Array("Jack", "hello", "world", "Mike")).foreach(println)
}

object speech {
  "Tag of Speech".println
  val inputStream: InputStream = new FileInputStream("./models/opennlp/en-pos-maxent.bin")
  val model = new POSModel(inputStream)
  val tagger = new POSTaggerME(model)

  val sentence = "Hi Welcome to China"
  val whitespaceTokenizer = WhitespaceTokenizer.INSTANCE
  val tokens: Array[String] = whitespaceTokenizer.tokenize(sentence)
  val tags: Array[String] = tagger.tag(tokens)
  val sample = new POSSample(tokens, tags)

  println(sample)
}

object parsing {
  "Parse Sentence".println
  val inputStream = new FileInputStream("./models/opennlp/en-parser-chunking.bin")
  val model = new ParserModel(inputStream)
  val parser: Parser = ParserFactory.create(model)

  import opennlp.tools.cmdline.parser.ParserTool

  val sentence = "Tommrrow need to work"
  val topParses: Array[Parse] = ParserTool.parseLine(sentence, parser, 1)
  topParses.foreach(_.show())
}

object chunking {
  "Chunking Sentence".println
  val sentence = "we need to chunk sentences"
  val tokenizer: WhitespaceTokenizer = WhitespaceTokenizer.INSTANCE
  val tokens: Array[String] = tokenizer.tokenize(sentence)

  val inputStream: InputStream = new FileInputStream("./models/opennlp/en-pos-maxent.bin")
  val model = new POSModel(inputStream)
  val tagger = new POSTaggerME(model)
  val tags: Array[String] = tagger.tag(tokens)

  val chunkerModel = new ChunkerModel(new FileInputStream("./models/opennlp/en-chunker.bin"))
  val chunkerME = new ChunkerME(chunkerModel)

  chunkerME.chunk(tokens, tags).foreach(println)
  chunkerME.chunkAsSpans(tokens, tags).foreach(println)




}
