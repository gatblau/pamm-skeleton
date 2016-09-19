name := """PAMM Skeleton"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies ++= Seq(
  "org.webjars" % "bootstrap" % "3.3.6",
  "org.webjars" % "angularjs" % "1.5.5",
  "org.webjars" % "angular-ui-router" % "0.2.18",
  "org.webjars.bower" % "angular-ui-validate" % "1.2.2",
  "org.webjars.bower" % "angular-base64" % "2.0.5"
)

libraryDependencies += specs2 % Test

// Add project dependencies here
libraryDependencies += "org.mongodb" %% "casbah" % "3.1.0"
libraryDependencies += "org.mindrot" % "jbcrypt" % "0.3m"

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

routesGenerator := InjectedRoutesGenerator

fork in run := true

dockerUpdateLatest := true
