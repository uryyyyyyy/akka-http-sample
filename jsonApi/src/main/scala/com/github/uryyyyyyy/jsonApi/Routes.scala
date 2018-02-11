package com.github.uryyyyyyy.jsonApi

import akka.actor.ActorSystem
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.{Directive1, _}
import com.github.uryyyyyyy.jsonApi.controller.HelloController
import com.github.uryyyyyyy.jsonApi.dto.PersonJsonSupportArgo._
import com.github.uryyyyyyy.jsonApi.dto.{FieldErrorInfo, ModelValidationRejection, Person, PersonValidator}
import spray.json.DefaultJsonProtocol

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class Routes(system: ActorSystem) extends Directives with CustomDirectives {
  implicit val contactValidator = PersonValidator
  implicit val ec = system.dispatcher
  implicit val validatedFieldFormats = jsonFormat2(FieldErrorInfo)

  def myRejectionHandler =
    RejectionHandler.newBuilder()
      .handle { case ModelValidationRejection(errors) =>
        val ss = Marshal(StatusCodes.BadRequest -> errors).to[HttpResponse]
        val res = Await.result(ss, Duration.Inf)
        complete(res)
      }
      .result()

  val route = {
    handleRejections(myRejectionHandler){
      path("hello") {
        get {
          HelloController.helloGet
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

trait CustomDirectives extends SprayJsonSupport with DefaultJsonProtocol with Directives {

  def validateModel[T](model: T)(implicit validator: Validator[T]): Directive1[T] = {
    validator(model) match {
      case Nil => provide(model)
      case errors: Seq[FieldErrorInfo] => reject(ModelValidationRejection(errors))
    }
  }
}