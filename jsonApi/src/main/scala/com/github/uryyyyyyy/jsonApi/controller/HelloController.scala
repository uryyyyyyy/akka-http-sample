package com.github.uryyyyyyy.jsonApi.controller

import akka.http.scaladsl.server._
import com.github.uryyyyyyy.jsonApi.route.JsonFormat
import com.github.uryyyyyyy.jsonApi.dto.{Coin, Person, PersonValidator}
import com.github.uryyyyyyy.jsonApi.route.ValidationDirectives
import com.github.uryyyyyyy.jsonApi.service.HelloService
import com.google.inject.Inject

class HelloController @Inject()(service: HelloService) extends ValidationDirectives with JsonFormat {

  def helloGet(): Route = {
    extractRequest { req =>
      println(req.headers)
      println(service.hello())
      val person = Person("fred", 23, List(Coin(100), Coin(10), Coin(50)))
      complete(person)
    }
  }

  def helloPost(): Route = {
    entity(as[Person]) { person =>
      validateModel(person, PersonValidator) { validatedPerson =>
        extractRequest { req =>
          println(req.headers)
          println(service.hello())
          val newPerson = validatedPerson.copy(name = s"${person.name} EX")
          complete(newPerson)
        }
      }
    }
  }
}
