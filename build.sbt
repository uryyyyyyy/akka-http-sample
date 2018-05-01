name := "akka-http-sample"

lazy val commonSettings = Seq(
  organization := "com.github.uryyyyyyy",
  scalaVersion := "2.12.6"
)

val akkaVersion = "2.5.9"
val akkaHttpVersion = "10.0.11"

lazy val root = (project in file("."))
  .settings(commonSettings: _*)
  .aggregate(jsonApi)

lazy val jsonApi = (project in file("jsonApi"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-stream" % akkaVersion,
      "com.typesafe.akka" %% "akka-actor"  % akkaVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
      "com.google.inject" % "guice" % "4.2.0"
    )
  ).enablePlugins(JavaServerAppPackaging)
