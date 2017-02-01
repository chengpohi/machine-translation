import java.nio.file.{Files, Paths, StandardCopyOption}

import com.typesafe.sbt.packager.SettingsHelper._
import sbt.Package.ManifestAttributes

//-------Package and Assembly------//
parallelExecution in ThisBuild := false
parallelExecution in Test := false

//------------------------------------------------//


//------------------------------------------------//
//------------------Release-----------------------//

//publish to sonatype
publishArtifact in Test := false

sonatypeProfileName := "com.github.chengpohi"

pomIncludeRepository := { _ => false }

publishMavenStyle := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  isSnapshot.value match {
    case true =>
      Some("snapshots" at nexus + "content/repositories/snapshots")
    case false =>
      Some("releases" at nexus + "service/local/staging/deploy/maven2")
  }
}

pomExtra := (
  <licenses>
    <license>
      <name>Apache 2</name>
      <url>https://www.apache.org/licenses/LICENSE-2.0.txt</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
    <url>https://github.com/chengpohi/elasticdsl</url>
    <scm>
      <url>git@github.com:chengpohi/elasticdsl.git</url>
      <connection>scm:git:git@github.com:chengpohi/elasticdsl.git</connection>
    </scm>
    <developers>
      <developer>
        <id>chengpohi</id>
        <name>chengpohi</name>
        <url>https://github.com/chengpohi/elasticdsl</url>
      </developer>
    </developers>
  )


import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._

//release release-version 0.2.2 next-version 0.2.2-SNAPSHOT
releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  ReleaseStep(action = Command.process("universal:packageBin", _)), //publish zip artificat
  ReleaseStep(action = Command.process("publishSigned", _)), //publishLibarary
  setNextVersion,
  commitNextVersion,
  ReleaseStep(action = Command.process("sonatypeReleaseAll", _)),
  pushChanges
)

scriptClasspath in bashScriptDefines ~= (cp => "../conf" +: cp)

enablePlugins(JavaAppPackaging, UniversalDeployPlugin)

makeDeploymentSettings(Universal, packageBin in Universal, "zip")

val pb = taskKey[Unit]("packageBin")

pb := {
  Command.process("universal:packageBin", state.value)
}

//------------------------------------------------//
//------------------------------------------------//
