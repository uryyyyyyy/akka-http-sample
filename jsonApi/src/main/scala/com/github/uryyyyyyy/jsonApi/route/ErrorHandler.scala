package com.github.uryyyyyyy.jsonApi.route

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{Directive0, Directives, RejectionHandler}

private object ErrorHandler extends Directives {
  def myRejectionHandler: RejectionHandler =
    RejectionHandler.newBuilder()
      .handle { case e: Exception =>
        complete(StatusCodes.BadRequest, e.getMessage)
      }
      .result()
}

trait ErrorHandlerDirective extends Directives {

  def errorHandle: Directive0 = {
    handleRejections(ErrorHandler.myRejectionHandler)
  }

}
