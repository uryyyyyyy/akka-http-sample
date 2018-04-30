package com.github.uryyyyyyy.jsonApi

import akka.http.scaladsl.server._
import com.github.uryyyyyyy.jsonApi.controller.HelloController
import com.github.uryyyyyyy.jsonApi.dto.JsonFormatCustom._
import com.github.uryyyyyyy.jsonApi.dto._
import com.github.uryyyyyyy.jsonApi.route.{ErrorHandlerDirective, ValidationDirectives}
import com.google.inject.Injector

class Routes(injector: Injector) extends ValidationDirectives with ErrorHandlerDirective {
  val helloController = injector.getInstance(classOf[HelloController])

  val route: Route = {
    errorHandle {
      path("hello") {
        get {
          helloController.helloGet()
        } ~ post {
          entity(as[Person]){ person =>
            helloController.helloPost(person)
          }
        }
      }
    }
  }

}
