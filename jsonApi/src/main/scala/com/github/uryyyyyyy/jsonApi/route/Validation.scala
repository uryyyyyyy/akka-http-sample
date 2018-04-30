package com.github.uryyyyyyy.jsonApi.route

import akka.http.scaladsl.server.{Directive1, Directives, Rejection}
import spray.json.{JsField, JsObject, JsString}

final case class CustomValidation(value: JsObject) extends Rejection

trait Validator[T] {

  protected def validationStage(rule: Boolean, fieldName: String, errorText: String): Option[JsField] =
    if (rule) Some((fieldName, JsString(errorText))) else None

  protected def validationStage[T2](fieldName: String, model: T2, validator: Validator[T2]): Option[JsField] =
    validator.validate(model).map(v => (fieldName, v))

  protected def validationStageList[T2](fieldName: String, models: List[T2], validator: Validator[T2]): Option[JsField] = {
    val values = models.zipWithIndex.flatMap{ case (model, index) => validator.validate(model).map((index.toString, _))}
    if (values.isEmpty) None else Some((fieldName + "[]", JsObject(values :_*)))
  }

  protected def validateInternal(validated: Option[JsField]*): Option[JsObject] = {
    val errors = validated.flatten
    if (errors.isEmpty) None else Some(JsObject(errors :_*))
  }

  def validate(model: T): Option[JsObject]

}

trait CustomDirectives extends Directives {

  def validateModel[T](model: T, validator: Validator[T]): Directive1[T] = {
    val obj = validator.validate(model)
    if (obj.isEmpty) {
      provide(model)
    } else {
      reject(CustomValidation(obj.get))
    }
  }
}
