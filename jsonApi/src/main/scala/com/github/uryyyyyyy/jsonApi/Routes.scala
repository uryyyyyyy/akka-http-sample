package com.github.uryyyyyyy.jsonApi

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{Directive1, _}
import com.github.uryyyyyyy.jsonApi.controller.HelloController
import com.github.uryyyyyyy.jsonApi.dto.JsonFormatCustom._
import com.github.uryyyyyyy.jsonApi.dto._

class Routes(system: ActorSystem) extends CustomDirectives {
  implicit val ec = system.dispatcher

  def myRejectionHandler =
    RejectionHandler.newBuilder()
      .handle { case ModelValidationRejection(errors) =>
        complete(StatusCodes.BadRequest, ModelValidationRejection(errors))
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
            validatePerson(person).apply { validatedPerson =>
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

trait Validator[T] {

  protected def validationStage(rule: Boolean, fieldName: String, errorText: String): Option[FieldErrorInfo] =
    if (rule) Some(FieldErrorInfo(fieldName, errorText)) else None

  def validate(model: T): Seq[FieldErrorInfo]

}

trait CustomDirectives extends Directives {

  def validateModel[T](model: T)(implicit validator: Validator[T]): Directive1[T] = {
    validator.validate(model) match {
      case Nil => provide(model)
      case errors: Seq[FieldErrorInfo] => reject(ModelValidationRejection(errors))
    }
  }

  def validatePerson(model: Person): Directive1[Person] = {
    validateModel(model)(PersonValidator)
  }
}