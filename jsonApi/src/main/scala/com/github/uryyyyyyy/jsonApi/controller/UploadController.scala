package com.github.uryyyyyyy.jsonApi.controller

import java.io.File

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server._
import akka.http.scaladsl.server.directives.{FileInfo, FileUploadDirectives}
import com.github.uryyyyyyy.jsonApi.route.JsonFormat
import com.google.inject.Inject

class UploadController @Inject()() extends Directives with JsonFormat with FileUploadDirectives {

  def upload(): Route = {
    storeUploadedFile("csv", (fileInfo: FileInfo) => File.createTempFile(fileInfo.fileName, ".tmp")) {
      case (metadata, file) =>
        // do something with the file and file metadata ...
        file.delete()
        complete(StatusCodes.OK)
    }
  }
}
