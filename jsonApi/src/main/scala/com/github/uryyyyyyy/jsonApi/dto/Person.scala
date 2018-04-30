package com.github.uryyyyyyy.jsonApi.dto

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Rejection
import com.github.uryyyyyyy.jsonApi.route.Validator
import spray.json.{DefaultJsonProtocol, JsField, JsObject, PrettyPrinter}

case class Coin(value: Int)

case class Person(name: String, age: Int, wallet: List[Coin])

final class ModelValidationRejection(optList: Option[JsField]*) extends Rejection {
  val value: JsObject = {
    val fields = optList.flatten
    JsObject(fields :_*)
  }
}

object CoinValidator extends Validator[Coin] {

  private def coinRule(coin: Coin): Option[JsField] =
    validationStage(
      coin.value < 0,
      "coin",
      "coin must not be minus"
    )

  override def validate(model: Coin): Option[JsObject] = {
    validateInternal(coinRule(model))
  }

}

object PersonValidator extends Validator[Person] {

  private def ageRule(age: Int): Option[JsField] =
    validationStage(
      age < 16 || age > 99,
      "age",
      "age must be between 16 and 99"
    )

  private def walletRule(wallet: List[Coin]): Option[JsField] =
    validationStage(
      wallet.isEmpty,
      "wallet",
      "wallet must not be empty"
    )

  private def walletRule2(wallet: List[Coin]): Option[JsField] =
    validationStageList(
      "wallet",
      wallet,
      CoinValidator
    )

  override def validate(model: Person): Option[JsObject] = {
    validateInternal(ageRule(model.age), walletRule(model.wallet), walletRule2(model.wallet))
  }

}

object JsonFormatCustom extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val printer = PrettyPrinter
  implicit val CoinCodecJson = jsonFormat1(Coin)
  implicit val PersonCodecJson = jsonFormat3(Person)
}