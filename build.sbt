name := "scala-learn"

version := "1.0"

resolvers += "Sonatype OSS Snapshots" at
  "https://oss.sonatype.org/content/repositories/releases"
lazy val root = (project in file(".")).enablePlugins(PlayScala)

enablePlugins(DockerPlugin)

scalaVersion := "2.12.2"

val akkaVersion = "2.5.2"
val akkaHttpVersion = "10.0.6"


addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.4")

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-core" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.softwaremill.macwire" %% "macros" % "2.2.5" % "provided",
  "com.softwaremill.macwire" %% "util" % "2.2.5",
  "com.softwaremill.macwire" %% "proxy" % "2.2.5",
  "org.scalaz" %% "scalaz-core" % "7.2.9",
  "org.typelevel" %% "cats" % "0.9.0",
  "org.scalactic" %% "scalactic" % "3.0.1",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "org.scalacheck" %% "scalacheck" % "1.13.4" % "test",
  "com.storm-enroute" %% "scalameter" % "0.8.2"
)

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-unchecked",
//  "-Xfatal-warnings", //Breaks compilation on warnings!
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xfuture"
)

//To run just a main class:
// enablePlugins(JavaAppPackaging)
// mainClass in Compile := Some("learn.plain.HelloWorld")

maintainer in Docker := "your-team@company.com"
dockerBaseImage := "openjdk:latest"
//dockerRepository in Docker := Some("????")
dockerExposedPorts in Docker := Seq(9000)
dockerExposedVolumes in Docker := Seq("/opt/docker/logs")
daemonUser in Docker := "root"
