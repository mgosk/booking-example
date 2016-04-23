package core

import play.api.libs.json.{JsError, Json}

case class ErrorWrapper(code: String, message: String)

object ErrorWrapper {

  implicit val errorWrapperWrites = Json.writes[ErrorWrapper]

  def notFound(message: String) = ErrorWrapper("notFound", message)

  def invalidJson(e: JsError) = ErrorWrapper("invalidJson", JsError.toJson(e).toString)

}