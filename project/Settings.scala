import sbt._
import sbt.Keys._

object Settings {

  lazy val commonSettings = Seq(
    organization := "com.github.chengpohi",
    version := "0.0.1-SNAPSHOT",
    name := "nlp-tools",
    scalaVersion := "2.12.2",
    resolvers += Resolver.mavenLocal,
    ivyScala := ivyScala.value map {
      _.copy(overrideScalaVersion = true)
    }
  )

  lazy val appDependencies = Seq(
    "jline" % "jline" % "3.0.0.M1"
  )

  lazy val commonDependencies = Seq(
    "org.apache.lucene" % "lucene-test-framework" % "6.3.0" % "test",
    "org.scalatest" %% "scalatest" % "3.0.1" % "test",
    "org.apache.logging.log4j" % "log4j-1.2-api" % "2.7",
    "org.apache.logging.log4j" % "log4j-api" % "2.7",
    "org.apache.logging.log4j" % "log4j-core" % "2.7",
    "com.typesafe" % "config" % "1.3.0",
    "org.apache.opennlp" % "opennlp-tools" % "1.8.3",
    "edu.stanford.nlp" % "stanford-corenlp" % "3.8.0",
    "edu.stanford.nlp" % "stanford-corenlp" % "3.8.0" classifier "models-english",
    "org.scalaz" %% "scalaz-core" % "7.3.0-M18",
    "org.scalaz" %% "scalaz-effect" % "7.3.0-M18",
    "com.github.chengpohi" %% "elasticdsl" % "0.2.3-SNAPSHOT" exclude ("jline", "jline")
  )

}
