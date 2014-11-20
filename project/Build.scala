import sbt._
import sbt.Keys._

object Build extends Build {

  val commonSettings = Seq(
    version := "1.0.0",
    organization := "com.collective",
    scalaVersion := "2.11.2",
    scalacOptions ++= List(
      "-encoding", "UTF-8",
      "-target:jvm-1.7",
      "-feature",
      "-unchecked",
      "-deprecation",
      "-Xlint",
      "-Xfatal-warnings"
    )
  )

  val akkaV = "2.3.6"
  val sprayV = "1.3.2"
  val adsLibVersion = "1.30.0"

  lazy val segmentFetcher = Project("segments-fetcher", file("."))
  .settings(commonSettings: _*)
  .settings(
    name := "Segment Fetcher",
    libraryDependencies ++= Seq(
      "com.typesafe.akka"       %%  "akka-actor"           % akkaV,
      "com.typesafe.akka"       %%  "akka-testkit"         % akkaV     % "test",
      "org.json4s"              %%  "json4s-jackson"       % "3.2.10",
      "org.specs2"              %%  "specs2-core"          % "2.3.11"  % "test",
      "org.slf4j"               %   "slf4j-api"            % "1.7.7",
      "ch.qos.logback"          %   "logback-core"         % "1.1.2",
      "ch.qos.logback"          %   "logback-classic"      % "1.1.2",
      "io.spray"                %%  "spray-can"            % sprayV,
      "io.spray"                %%  "spray-client"         % sprayV,
      "io.spray"                %%  "spray-httpx"          % sprayV,
      "io.spray"                %%  "spray-http"           % sprayV,
      "io.spray"                %%  "spray-routing"        % sprayV,
      "io.spray"                %%  "spray-util"           % sprayV,
      "io.spray"                %%  "spray-io"             % sprayV,
      "io.spray"                %%  "spray-testkit"        % sprayV    % "test",
      "io.spray"                %%  "spray-io"             % sprayV,
      "io.spray"                %%  "spray-json"           % "1.3.1",
      "org.parboiled"           %   "parboiled-core"       % "1.1.6",
      "org.parboiled"           %%  "parboiled-scala"      % "1.1.6",
      "org.scalatest"           %% "scalatest"             % "2.2.2"  % "test"
    )
  )

}