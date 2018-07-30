organization := "com.vijai"

name := "gcp-play"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.12.6"

libraryDependencies ++= {
  val akkaVersion = "2.5.14"
  val akkaHttpVersion = "10.1.3"
  val circeVersion = "0.9.3"
  val scalaLoggingVersion = "3.5.0"

  Seq(
    // guava
    "com.google.guava" % "guava" % "25.1-jre",
    // akka
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % "test",
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
    // logging
    "ch.qos.logback" % "logback-classic" % "1.1.7",
    "org.slf4j" % "log4j-over-slf4j" % "1.7.21",
    "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion,
    "net.logstash.logback" % "logstash-logback-encoder" % "4.8",
    // circe
    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-generic-extras" % circeVersion,
    "io.circe" %% "circe-parser" % circeVersion,
    "io.circe" %% "circe-java8" % circeVersion,
    "io.circe" %% "circe-optics" % circeVersion,
    // TEST
    "org.scalatest" %% "scalatest" % "3.0.1" % Test,
    "org.mockito" % "mockito-all" % "1.10.19" % Test
  )

}

Revolver.settings

fork in Test := true

fork in run := true

javaOptions in reStart ++= Seq("-Dlogback.configurationFile=logback.xml", "-Dconfig.file=src/main/resources/application.conf")

javaOptions in run ++= Seq("-Dlogback.configurationFile=logback.xml", "-Dconfig.file=src/main/resources/application.conf")

scalacOptions in compile ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8", // yes, this is 2 args
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xlint:missing-interpolator",
  "-Xfuture"
)