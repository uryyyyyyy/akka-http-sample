package com.github.uryyyyyyy.jsonApi.controller

import akka.http.scaladsl.marshalling.{Marshal, ToResponseMarshallable}
import akka.http.scaladsl.model.{HttpRequest, StatusCodes}
import akka.http.scaladsl.server._
import com.github.uryyyyyyy.jsonApi.dto.FFF._
import com.github.uryyyyyyy.jsonApi.dto.PersonJsonSupportArgo._
import com.github.uryyyyyyy.jsonApi.dto.{Coin, FieldErrorInfo, Person}

import scala.concurrent.{ExecutionContextExecutor, Future}

object HelloController {

  def helloGet(ctx: RequestContext): Future[RouteResult] = {
    import ctx.executionContext
    val person = Person("fred", 23, List(Coin(100), Coin(10), Coin(50)))
    createCustomResponse(ctx.request, person)
  }

  def helloPost(ppp: Person)(ctx: RequestContext): Future[RouteResult] = {
    import ctx.executionContext
    val person = Person("fred", 23, List(Coin(100), Coin(10), Coin(50)))
    val newPerson = person.copy(name = s"${person.name} EX")
    createCustomResponse(ctx.request, newPerson)
  }

  private def createCustomResponse(request: HttpRequest, ss: ToResponseMarshallable)(implicit ec: ExecutionContextExecutor): Future[RouteResult] = {
    Marshal(ss).toResponseFor(request)
      .map(res => RouteResult.Complete(res))
      .recover {
        case Marshal.UnacceptableResponseContentTypeException(supported) ⇒
          RouteResult.Rejected(UnacceptedResponseContentTypeRejection(supported) :: Nil)
        case RejectionError(rej) ⇒
          RouteResult.Rejected(rej :: Nil)
      }
  }

  def handleAA(errors: Seq[FieldErrorInfo])(ctx: RequestContext): Future[RouteResult] = {
    import ctx.executionContext
    Marshal(StatusCodes.BadRequest -> errors).toResponseFor(ctx.request)
      .map(res => RouteResult.Complete(res))
      .recover {
        case Marshal.UnacceptableResponseContentTypeException(supported) ⇒
          RouteResult.Rejected(UnacceptedResponseContentTypeRejection(supported) :: Nil)
        case RejectionError(rej) ⇒
          RouteResult.Rejected(rej :: Nil)
      }
  }

}
