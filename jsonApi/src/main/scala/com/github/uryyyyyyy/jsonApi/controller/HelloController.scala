package com.github.uryyyyyyy.jsonApi.controller

import akka.http.scaladsl.server._
import com.github.uryyyyyyy.jsonApi.dto.{Coin, Person, PersonValidator}
import com.github.uryyyyyyy.jsonApi.route.{AuthDirective, JsonFormat, ValidationDirectives}
import com.github.uryyyyyyy.jsonApi.service.HelloService
import com.google.inject.Inject

class HelloController @Inject()(service: HelloService) extends ValidationDirectives with JsonFormat with AuthDirective {

  def helloGet(): Route = {
    extractRequest { req =>
      checkAuth(req) { authUser =>
        println(authUser)
        println(service.hello())
        val person = Person("fred", 23, List(Coin(100), Coin(10), Coin(50)))
        complete(person)
      }
    }
  }

  def login(): Route = {
    parameters('id, 'pass) { (id, pass) =>
      loginRoute(id, pass)
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
