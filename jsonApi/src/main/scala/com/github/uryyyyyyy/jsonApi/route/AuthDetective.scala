package com.github.uryyyyyyy.jsonApi.route

import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.{ModeledCustomHeader, ModeledCustomHeaderCompanion}
import akka.http.scaladsl.server.{Directive1, Directives, Route}

import scala.util.Try

sealed trait Role
case object Admin extends Role
case object Normal extends Role
case class AuthUser(name: String, role: Role)

final class XAuthHeader(token: String) extends ModeledCustomHeader[XAuthHeader] {
  override def renderInRequests = true
  override def renderInResponses = true
  override val companion = XAuthHeader
  override def value: String = token
}
object XAuthHeader extends ModeledCustomHeaderCompanion[XAuthHeader] {
  override val name = "X-auth"
  override def parse(value: String) = Try(new XAuthHeader(value))
}

trait AuthDirective extends Directives {

  private def check(token: String): Option[AuthUser] = {
    Some(AuthUser(token, Admin))
  }

  private def login(id: String, pass: String): Option[AuthUser] = {
    Some(AuthUser(id, Admin))
  }

  def checkAuth(req: HttpRequest): Directive1[AuthUser] = {
    req.header[XAuthHeader].flatMap(v => check(v.value())) match {
      case None => complete(StatusCodes.Unauthorized, "need Login")
      case Some(authUser) => provide(authUser)
    }
  }

  def loginRoute(id: String, pass: String): Route = {
    login(id, pass) match {
      case None => complete(StatusCodes.BadRequest, "id pass")
      case Some(authUser) => complete(HttpResponse(
        status = StatusCodes.OK,
        headers = List(XAuthHeader(authUser.name))
      ))
    }
  }
}
