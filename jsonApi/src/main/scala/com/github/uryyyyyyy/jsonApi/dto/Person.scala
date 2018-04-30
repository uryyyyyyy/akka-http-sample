package com.github.uryyyyyyy.jsonApi.dto

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Rejection
import com.github.uryyyyyyy.jsonApi.Validator
import spray.json.{DefaultJsonProtocol, PrettyPrinter}

case class Coin(value: Int)

case class Person(name: String, age: Int, wallet: List[Coin])

final case class FieldErrorInfo(name: String, error: String)
final case class ModelValidationRejection(invalidFields: Seq[FieldErrorInfo]) extends Rejection

object PersonValidator extends Validator[Person] {

  private def ageRule(age: Int): Option[FieldErrorInfo] =
    validationStage(
      age < 16 || age > 99,
      "age",
      "age must be between 16 and 99"
    )

  private def coinRule(coin: Coin): Option[FieldErrorInfo] =
    validationStage(
      coin.value < 0,
      "coin",
      "coin must not be minus"
    )

  override def validate(model: Person): Seq[FieldErrorInfo] = {
    (ageRule(model.age) :: coinRule(model.wallet.head) :: Nil).flatten
  }

}

object JsonFormatCustom extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val printer = PrettyPrinter
  implicit val validatedFieldFormats = jsonFormat2(FieldErrorInfo)
  implicit val ModelValidationRejectionFormats = jsonFormat1(ModelValidationRejection)
  implicit val CoinCodecJson = jsonFormat1(Coin)
  implicit val PersonCodecJson = jsonFormat3(Person)
}