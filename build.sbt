import Settings._

scalaVersion := "2.12.2"

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
