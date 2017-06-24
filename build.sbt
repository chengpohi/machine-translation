import Settings._

scalaVersion := "2.12.2"

lazy val compileScalaStyle = taskKey[Unit]("compileScalaStyle")
compileScalaStyle := org.scalastyle.sbt.ScalastylePlugin.scalastyle
  .in(Compile)
  .toTask("")
  .value

(compile in Compile) <<= (compile in Compile) dependsOn compileScalaStyle
(test in Test) := {
  (test in Test) dependsOn compileScalaStyle
}

(scalastyleConfig in Compile) := file("project/scalastyle-config.xml")
(scalastyleConfig in Test) := file("project/scalastyle-test-config.xml")


lazy val analyzer = project
  .in(file("modules/analyzer"))
  .settings(commonSettings: _*)
  .settings(name := "nlp-analyzer")
  .settings(libraryDependencies ++= commonDependencies)

lazy val app = project
  .in(file("app"))
  .settings(commonSettings: _*)
  .settings(name := "nlp-tools")
  .settings(libraryDependencies ++= commonDependencies ++ appDependencies)
  .dependsOn(analyzer)
