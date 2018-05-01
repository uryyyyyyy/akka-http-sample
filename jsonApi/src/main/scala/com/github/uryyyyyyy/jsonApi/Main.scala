package com.github.uryyyyyyy.jsonApi

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.github.uryyyyyyy.jsonApi.context.DI
import kamon.Kamon

object Main {
  def main(args: Array[String]) {
    Kamon.loadReportersFromConfig()

    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    val injector = DI.generateInjector(system)

    val bindingFuture = Http().bindAndHandle(new Routes(injector).route, "localhost", 8080)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  }
}