package com.github.uryyyyyyy.jsonApi.dto

import com.github.uryyyyyyy.jsonApi.route.Validator
import spray.json.{JsField, JsObject}

case class Person(name: String, age: Int, wallet: List[Coin])

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
