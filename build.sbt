name := "akka-http-sample"

scalaVersion := "2.12.4"

lazy val commonSettings = Seq(
  organization := "com.github.uryyyyyyy",
  scalaVersion := "2.12.4"
)

val akkaVersion = "2.5.9"

lazy val simple = (project in file("simple"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http" % "10.0.11",
      "com.typesafe.akka" %% "akka-stream" % akkaVersion,
      "com.typesafe.akka" %% "akka-actor"  % akkaVersion
    )
  )