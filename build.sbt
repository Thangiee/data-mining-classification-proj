name := "data-mining-proj-classification"
version := "1.0"
scalaVersion := "2.11.7"

mainClass in assembly := Some("project1.Main")
assemblyOutputPath in assembly := new File("./classifier.jar")

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:_"
)

libraryDependencies ++= Seq(
  "org.spire-math" %% "cats" % "0.2.0",
  "org.spire-math" %% "spire" % "0.10.1",
  "com.github.pathikrit" %% "better-files" % "2.13.0",
  "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test",
  "org.scalacheck" %% "scalacheck" % "1.12.4" % "test"
)
