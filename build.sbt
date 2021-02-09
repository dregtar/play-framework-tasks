name := """test"""
organization := "test"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.3"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
//add slick
libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-slick" % "5.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "5.0.0"
)
//add jdbc connector
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.45"
libraryDependencies += jdbc % Test

libraryDependencies += "org.scalatestplus" %% "mockito-3-4" % "3.2.2.0" % "test"
// Adds additional packages into Twirl
//TwirlKeys.templateImports += "test.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "test.binders._"
