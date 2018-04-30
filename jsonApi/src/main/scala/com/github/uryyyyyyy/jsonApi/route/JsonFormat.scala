package com.github.uryyyyyyy.jsonApi.route

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.github.uryyyyyyy.jsonApi.dto.{Coin, Person}
import spray.json.{DefaultJsonProtocol, PrettyPrinter}

trait JsonFormat extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val printer = PrettyPrinter
  implicit val CoinCodecJson = jsonFormat1(Coin)
  implicit val PersonCodecJson = jsonFormat3(Person)
}
