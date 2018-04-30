package com.github.uryyyyyyy.jsonApi.route

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{Directive0, Directives, RejectionHandler}

trait ErrorHandlerDirective extends Directives {

  private def myRejectionHandler: RejectionHandler =
    RejectionHandler.newBuilder()
      .handle { case e: Exception =>
        complete(StatusCodes.BadRequest, e.getMessage)
      }
      .result()

  def errorHandle: Directive0 = {
    handleRejections(myRejectionHandler)
  }

}
