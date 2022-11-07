ThisBuild / organization := "io.github.atinvento100"
ThisBuild / organizationName := "atinvento100"
ThisBuild / organizationHomepage := Some(url("https://github.com/atinvento100/ResultsetToDataframe"))

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/atinvento100/ResultsetToDataframe"),
    "scm:git@github.com:atinvento100/ResultsetToDataframe.git"
  )
)
ThisBuild / developers := List(
  Developer(
    id    = "atinvento100",
    name  = "Rohit Surya A T",
    email = "atinvento@gmail.com",
    url   = url("https://github.com/atinvento100/ResultsetToDataframe")
  )
)

ThisBuild / description := "The library provides the functionality to convert a resultset to a dataframe and also print a resultset in scala"
ThisBuild / licenses := List("Apache 2" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt"))
ThisBuild / homepage := Some(url("https://github.com/atinvento100/ResultsetToDataframe"))

// Remove all additional repository other than Maven Central from POM
ThisBuild / pomIncludeRepository := { _ => false }
ThisBuild / publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}
ThisBuild / publishMavenStyle := true