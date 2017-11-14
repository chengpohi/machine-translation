package com.github.chengpohi.opennlp

import java.io._
import java.nio.charset.StandardCharsets

import opennlp.tools.chunker.{ChunkerME, ChunkerModel}
import opennlp.tools.dictionary.Dictionary
import opennlp.tools.langdetect._
import opennlp.tools.ml.perceptron.PerceptronTrainer
import opennlp.tools.namefind.{NameFinderME, TokenNameFinderModel}
import opennlp.tools.parser.{Parse, Parser, ParserFactory, ParserModel}
import opennlp.tools.postag.{POSModel, POSSample, POSTaggerME}
import opennlp.tools.sentdetect.{
  SentenceDetectorFactory,
  SentenceDetectorME,
  SentenceModel,
  SentenceSampleStream
}
import opennlp.tools.tokenize.{
  SimpleTokenizer,
  TokenizerME,
  TokenizerModel,
  WhitespaceTokenizer
}
import opennlp.tools.util.model.ModelUtil
import opennlp.tools.util.{
  MarkableFileInputStreamFactory,
  PlainTextByLineStream,
  TrainingParameters
}
import com.github.chengpohi.utils.ColorString._

object OpenNLPUsage extends App {
  training
  tokenizer
  sentence
  nameentity
  speech
  parsing
  chunking
  language
}

object tokenizer {
  "simple tokenizer".red
  val simpleTokenizer = SimpleTokenizer.INSTANCE
  simpleTokenizer.tokenize("hello123 world").foreach(println)

  "whitespace tokenizer".red
  val whitespaceTokenizer = WhitespaceTokenizer.INSTANCE
  whitespaceTokenizer.tokenize("foo123 bar").foreach(println)

  "Tokenizer Model".red
  val inputStream: InputStream = new FileInputStream(
    "./models/opennlp/en-token.bin")
  val tokenizerModel = new TokenizerModel(inputStream)
  val tokenizerME = new TokenizerME(tokenizerModel)
  tokenizerME.tokenize("it was a beautiful place").foreach(println)
}

object sentence {

  "sentence detector".red
  val inputStream: InputStream = new FileInputStream(
    "./models/opennlp/en-sent.bin")

  val model = new SentenceModel(inputStream)
  val detector = new SentenceDetectorME(model)
  val sentence =
    "sentence detect, it's foo bar. How are you ? Do you want to have a drink?"
  detector.sentDetect(sentence).foreach(println)

  "sentence probabilities".red
  detector.getSentenceProbabilities.foreach(println)

  "sentence pos detect".red
  detector.sentPosDetect(sentence).foreach(println)
}

object nameentity {
  "Name Entity Recognition".red
  val inputStream: InputStream = new FileInputStream(
    "./models/opennlp/en-ner-person.bin")
  val model = new TokenNameFinderModel(inputStream)
  val nameFinder = new NameFinderME(model)
  nameFinder.find(Array("Jack", "hello", "world", "Mike")).foreach(println)
}

object speech {
  "Tag of Speech".red
  val inputStream: InputStream = new FileInputStream(
    "./models/opennlp/en-pos-maxent.bin")
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
  "Parse Sentence".red
  val inputStream = new FileInputStream(
    "./models/opennlp/en-parser-chunking.bin")
  val model = new ParserModel(inputStream)
  val parser: Parser = ParserFactory.create(model)

  import opennlp.tools.cmdline.parser.ParserTool

  val sentence = "Tommrrow need to work"
  val topParses: Array[Parse] = ParserTool.parseLine(sentence, parser, 1)
  topParses.foreach(_.show())
}

object chunking {
  "Chunking Sentence".red
  val sentence = "we need to chunk sentences"
  val tokenizer: WhitespaceTokenizer = WhitespaceTokenizer.INSTANCE
  val tokens: Array[String] = tokenizer.tokenize(sentence)

  val inputStream: InputStream = new FileInputStream(
    "./models/opennlp/en-pos-maxent.bin")
  val model = new POSModel(inputStream)
  val tagger = new POSTaggerME(model)
  val tags: Array[String] = tagger.tag(tokens)

  val chunkerModel = new ChunkerModel(
    new FileInputStream("./models/opennlp/en-chunker.bin"))
  val chunkerME = new ChunkerME(chunkerModel)

  chunkerME.chunk(tokens, tags).foreach(println)
  chunkerME.chunkAsSpans(tokens, tags).foreach(println)
}

object training {
  trainLanguageModels
  trainSentenceDetector

  def trainSentenceDetector = {
    "traning sentence detector".red

    val inputStreamFactory = new MarkableFileInputStreamFactory(
      new File("./models/opennlp/Sentences.txt"))
    val lineStream =
      new PlainTextByLineStream(inputStreamFactory, StandardCharsets.UTF_8)

    val sampleStream = new SentenceSampleStream(lineStream)

    val abb = new FileInputStream("./models/opennlp/abb.xml")
    val dictionary = new Dictionary(abb)
    val eos = Array('.', '?')

    val factory = new SentenceDetectorFactory("en", false, dictionary, null)

    val model = SentenceDetectorME.train("en",
                                         sampleStream,
                                         factory,
                                         TrainingParameters.defaultParams())
    val modelOut = new BufferedOutputStream(
      new FileOutputStream("./models/opennlp/en-sent.bin"))

    model.serialize(modelOut)
  }

  def trainLanguageModels = {
    "traning language models".red
    val inputStreamFactory = new MarkableFileInputStreamFactory(
      new File("./models/opennlp/DoccatSample.txt"))

    val lineStream =
      new PlainTextByLineStream(inputStreamFactory, StandardCharsets.UTF_8)
    val sampleStream = new LanguageDetectorSampleStream(lineStream)

    val params = ModelUtil.createDefaultTrainingParameters()
    params.put(TrainingParameters.ALGORITHM_PARAM,
               PerceptronTrainer.PERCEPTRON_VALUE)
    params.put(TrainingParameters.CUTOFF_PARAM, 0)

    val factory = new LanguageDetectorFactory()

    val model = LanguageDetectorME.train(sampleStream, params, factory)
    model.serialize(new File("./models/opennlp/langdetect.bin"))
  }

}

object language {
  "language detector".red
  val inputStream: InputStream = new FileInputStream(
    "./models/opennlp/langdetect.bin")
  val model = new LanguageDetectorModel(inputStream)

  val myCategorizer = new LanguageDetectorME(model)
  val language: Language = myCategorizer.predictLanguage("你好")
  println(language)
}
