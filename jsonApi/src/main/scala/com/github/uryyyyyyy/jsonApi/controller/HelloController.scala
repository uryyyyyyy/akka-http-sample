package com.github.uryyyyyyy.jsonApi.controller

import akka.http.scaladsl.server._
import com.github.uryyyyyyy.jsonApi.dto.JsonFormatCustom._
import com.github.uryyyyyyy.jsonApi.dto.{Coin, Person, PersonValidator}
import com.github.uryyyyyyy.jsonApi.route.ValidationDirectives

class HelloController extends ValidationDirectives {

  def helloGet(): Route = {
    extractRequest { req =>
      println(req.headers)
      val person = Person("fred", 23, List(Coin(100), Coin(10), Coin(50)))
      complete(person)
    }
  }

  def helloPost(person: Person): Route = {
    validateModel(person, PersonValidator) { validatedPerson =>
      extractRequest { req =>
        println(req.headers)
        val newPerson = validatedPerson.copy(name = s"${person.name} EX")
        complete(newPerson)
      }
    }
  }
}
