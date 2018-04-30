package com.github.uryyyyyyy.jsonApi.dto

import com.github.uryyyyyyy.jsonApi.route.Validator
import spray.json.{JsField, JsObject}

case class Coin(value: Int)

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
