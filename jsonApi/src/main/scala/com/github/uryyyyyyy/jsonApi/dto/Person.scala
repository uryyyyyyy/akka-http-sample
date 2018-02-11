package com.github.uryyyyyyy.jsonApi.dto

import akka.http.scaladsl.server.Rejection
import argonaut.Argonaut._
import argonaut.CodecJson
import com.github.uryyyyyyy.jsonApi.Validator
import de.heikoseeberger.akkahttpargonaut.ArgonautSupport

case class Coin(value: Int){
  require(0 <= value, "value must be plus")
}

case class Person(name: String, age: Int, wallet: List[Coin]){
  require(0 <= wallet.length, "wallet must not be empty")
}

object PersonJsonSupportArgo extends ArgonautSupport {
  implicit def CoinCodecJson: CodecJson[Coin] =
    casecodec1(Coin.apply, Coin.unapply)("value")
  implicit def PersonCodecJson: CodecJson[Person] =
    casecodec3(Person.apply, Person.unapply)("name", "age", "wallet")
}

final case class FieldErrorInfo(name: String, error: String)
final case class ModelValidationRejection(invalidFields: Seq[FieldErrorInfo]) extends Rejection

object PersonValidator extends Validator[Person] {

  private def ageRule(age: Int) = if (age < 16 || age > 99) true else false

  override def apply(model: Person): Seq[FieldErrorInfo] = {

    val ageErrorOpt: Option[FieldErrorInfo] = validationStage(ageRule(model.age), "age", "age must be between 16 and 99")

    (ageErrorOpt :: Nil).flatten
  }

}