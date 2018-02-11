package com.github.uryyyyyyy.jsonApi

import akka.actor.ActorSystem
import akka.http.scaladsl.server.{Directive1, _}
import com.github.uryyyyyyy.jsonApi.controller.HelloController
import com.github.uryyyyyyy.jsonApi.dto.PersonJsonSupportArgo._
import com.github.uryyyyyyy.jsonApi.dto.{FieldErrorInfo, ModelValidationRejection, Person, PersonValidator}

class Routes(system: ActorSystem) extends Directives with CustomDirectives {
  implicit val contactValidator = PersonValidator
  implicit val ec = system.dispatcher

  def myRejectionHandler =
    RejectionHandler.newBuilder()
      .handle { case ModelValidationRejection(errors) =>
        HelloController.handleAA(errors)
      }
      .result()

  val route = {
    handleRejections(myRejectionHandler){
      path("hello") {
        get {
          val aa: Route = (ctx: RequestContext) => HelloController.helloGet(ctx)
          aa
        } ~ post {
          entity(as[Person]){ implicit person =>
            validateModel(person).apply { validatedPerson =>
              HelloController.helloPost(validatedPerson)
            }
          }
        }
      }
    }
  }

}

trait Validator[T] extends (T => Seq[FieldErrorInfo]) {

  protected def validationStage(rule: Boolean, fieldName: String, errorText: String): Option[FieldErrorInfo] =
    if (rule) Some(FieldErrorInfo(fieldName, errorText)) else None

}

trait CustomDirectives extends Directives {

  def validateModel[T](model: T)(implicit validator: Validator[T]): Directive1[T] = {
    validator(model) match {
      case Nil => provide(model)
      case errors: Seq[FieldErrorInfo] => reject(ModelValidationRejection(errors))
    }
  }
}