name := "akka-http-sample"

scalaVersion := "2.12.4"

lazy val commonSettings = Seq(
  organization := "com.github.uryyyyyyy",
  scalaVersion := "2.12.4"
)

val akkaVersion = "2.5.9"
val akkaHttpVersion = "10.0.11"

lazy val jsonApi = (project in file("jsonApi"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-stream" % akkaVersion,
      "com.typesafe.akka" %% "akka-actor"  % akkaVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
      "io.argonaut" %% "argonaut" % "6.2.1",
      "de.heikoseeberger" %% "akka-http-argonaut" % "1.19.0",
      "org.scalaz" %% "scalaz-core" % "7.2.19"
    )
  ).enablePlugins(JavaServerAppPackaging)
