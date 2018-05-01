name := "akka-http-sample"

lazy val commonSettings = Seq(
  organization := "com.github.uryyyyyyy",
  scalaVersion := "2.12.6",
  version := "0.1.0"
)

val akkaVersion = "2.5.12"
val akkaHttpVersion = "10.1.1"

lazy val root = (project in file("."))
  .settings(commonSettings: _*)
  .aggregate(jsonApi)

lazy val jsonApi = (project in file("jsonApi"))
  .settings(commonSettings: _*)
  .enablePlugins(JavaServerAppPackaging, JavaAgent)
  .settings(
    javaAgents += "org.aspectj" % "aspectjweaver" % "1.9.1" % "runtime",
    libraryDependencies ++= Seq(
      "org.aspectj" % "aspectjweaver" % "1.9.1" % "provided",
      "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-stream" % akkaVersion,
      "com.typesafe.akka" %% "akka-actor"  % akkaVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
      "com.google.inject" % "guice" % "4.2.0",
      "io.kamon" %% "kamon-akka-http-2.5" % "1.1.0",
      "ch.qos.logback" % "logback-classic" % "1.2.3",
    )
  )
