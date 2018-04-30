package com.github.uryyyyyyy.jsonApi.route

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server._
import com.github.uryyyyyyy.jsonApi.controller.HelloController
import com.github.uryyyyyyy.jsonApi.dto.JsonFormatCustom._
import com.github.uryyyyyyy.jsonApi.dto._

class Routes(system: ActorSystem) extends CustomDirectives {
  implicit val ec = system.dispatcher

  def myRejectionHandler =
    RejectionHandler.newBuilder()
      .handle { case CustomValidation(value) =>
        complete(StatusCodes.BadRequest, value)
      }
      .result()

  val route = {
    handleRejections(myRejectionHandler){
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
