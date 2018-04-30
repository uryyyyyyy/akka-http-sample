package com.github.uryyyyyyy.jsonApi.controller

import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.server._
import com.github.uryyyyyyy.jsonApi.dto.JsonFormatCustom._
import com.github.uryyyyyyy.jsonApi.dto.{Coin, Person}

object HelloController extends Directives{

  def helloGet(req: HttpRequest): StandardRoute = {
    println(req.headers)
    val person = Person("fred", 23, List(Coin(100), Coin(10), Coin(50)))
    complete(person)
  }

  def helloPost(ppp: Person, req: HttpRequest): StandardRoute = {
    println(req.headers)
    val person = Person("fred", 23, List(Coin(100), Coin(10), Coin(50)))
    val newPerson = person.copy(name = s"${person.name} EX")
    complete(newPerson)
  }
}
