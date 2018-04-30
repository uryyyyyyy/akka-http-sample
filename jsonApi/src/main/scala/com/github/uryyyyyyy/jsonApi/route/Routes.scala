package com.github.uryyyyyyy.jsonApi.route

import akka.actor.ActorSystem
import akka.http.scaladsl.server._
import com.github.uryyyyyyy.jsonApi.controller.HelloController
import com.github.uryyyyyyy.jsonApi.dto.JsonFormatCustom._
import com.github.uryyyyyyy.jsonApi.dto._

class Routes(system: ActorSystem) extends ValidationDirectives with ErrorHandlerDirective {
  implicit val ec = system.dispatcher

  val route: Route = {
    errorHandle {
      path("hello") {
        get {
          extractRequest { req =>
            HelloController.helloGet(req)
          }
        } ~ post {
          entity(as[Person]){ implicit person =>
            validateModel(person, PersonValidator) { validatedPerson =>
              extractRequest { req =>
                HelloController.helloPost(validatedPerson, req)
              }
            }
          }
        }
      }
    }
  }

}
