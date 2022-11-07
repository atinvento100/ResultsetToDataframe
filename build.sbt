ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.10"

organization := "io.github.atinvento100"
homepage := Some(url("https://github.com/atinvento100/ResultsetToDataframe"))
scmInfo := Some(ScmInfo(url("https://github.com/atinvento100/ResultsetToDataframe"), "git@github.com:atinvento100/ResultsetToDataframe.git"))
//developers := List(Developer("rahasak", "rahasak", "rahasak@scorelab.org", url("https://gitlab.com/rahasak-labs")))
licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0"))
publishMavenStyle := true

// disable publishw ith scala version, otherwise artifact name will include scala version
// e.g cassper_2.11
crossPaths := false

// add sonatype repository settings
// snapshot versions publish to sonatype snapshot repository
// other versions publish to sonatype staging repository
publishTo := Some(
  if (isSnapshot.value)
    Opts.resolver.sonatypeSnapshots
  else
    Opts.resolver.sonatypeStaging
)


lazy val root = (project in file("."))
  .settings(
    name := "ResultSetDataFrame"
  )

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.0.0",
  "org.apache.spark" %% "spark-sql" % "3.0.0",
  "org.json" % "json" % "20220320"
)