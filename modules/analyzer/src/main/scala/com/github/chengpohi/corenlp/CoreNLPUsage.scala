package com.github.chengpohi.corenlp

import java.util.Properties

import edu.stanford.nlp.coref.CorefCoreAnnotations.CorefChainAnnotation
import edu.stanford.nlp.ling.CoreAnnotations._
import edu.stanford.nlp.ling.CoreLabel
import edu.stanford.nlp.pipeline.Annotation
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation
import edu.stanford.nlp.util.CoreMap

import scala.collection.JavaConverters._

object CoreNLPUsage extends App {
  val props = new Properties()
  props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref")

  import edu.stanford.nlp.pipeline.StanfordCoreNLP

  val pipeline = new StanfordCoreNLP(props)

  val text = "the quick brown fox jumped over the lazy dog"

  val document = new Annotation(text)

  pipeline.annotate(document)

  val sentences = document.get(classOf[SentencesAnnotation]).asScala

  for (sentence: CoreMap <- sentences) {
    for (token: CoreLabel <- sentence.get(classOf[TokensAnnotation]).asScala) {
      // this is the text of the token
      val word = token.get(classOf[TextAnnotation])
      println(word)
      // this is the POS tag of the token
      val pos = token.get(classOf[PartOfSpeechAnnotation])
      println(pos)
      // this is the NER label of the token
      val ne = token.get(classOf[NamedEntityTagAnnotation])
      println(ne)
    }
    val tree = sentence.get(classOf[TreeAnnotation])
    println(tree)

    val dependencies = sentence.get(classOf[CollapsedCCProcessedDependenciesAnnotation])
    val graph = document.get(classOf[CorefChainAnnotation])
  }

}
