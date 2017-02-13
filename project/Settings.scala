import sbt._
import sbt.Keys._

object Settings {

  lazy val commonSettings = Seq(
    organization := "com.github.chengpohi",
    unmanagedBase := baseDirectory.value / "lib",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.12.1",
    resolvers += Resolver.mavenLocal,
    ivyScala := ivyScala.value map {
      _.copy(overrideScalaVersion = true)
    }
  )

  lazy val commonDependencies = Seq(
    "org.apache.lucene" % "lucene-test-framework" % "6.4.1" % "test",
    "org.scalatest" %% "scalatest" % "3.0.1" % "test",
    "org.apache.logging.log4j" % "log4j-1.2-api" % "2.7",
    "org.apache.logging.log4j" % "log4j-api" % "2.7",
    "org.apache.logging.log4j" % "log4j-core" % "2.7",
    "com.typesafe" % "config" % "1.3.0",

    "com.github.chengpohi" %% "elasticdsl" % "0.2.3-SNAPSHOT"
  )

}