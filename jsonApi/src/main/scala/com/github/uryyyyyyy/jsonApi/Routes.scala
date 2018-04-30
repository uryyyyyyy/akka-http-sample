package com.github.uryyyyyyy.jsonApi

import akka.http.scaladsl.server._
import com.github.uryyyyyyy.jsonApi.controller.{HelloController, UploadController}
import com.github.uryyyyyyy.jsonApi.route.{ErrorHandlerDirective, ValidationDirectives}
import com.google.inject.Injector

class Routes(injector: Injector) extends ValidationDirectives with ErrorHandlerDirective {
  val helloController = injector.getInstance(classOf[HelloController])
  val uploadController = injector.getInstance(classOf[UploadController])

  val route: Route = {
    errorHandle {
      path("hello") {
        get {
          helloController.helloGet()
        } ~ post {
          helloController.helloPost()
        }
      } ~ path("login") {
        get {
          helloController.login()
        }
      } ~ path("upload") {
        post {
          uploadController.upload()
        }
      }
    }
  }
}
