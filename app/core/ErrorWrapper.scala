package core

import play.api.libs.json.{JsError, Json}

case class ErrorWrapper(code: String, message: String, serviceable: Boolean = false)

object ErrorWrapper {

  implicit val errorWrapperWrites = Json.writes[ErrorWrapper]

  val notExist = ErrorWrapper("notExist", "Obiekt nie istnieje")
  def invalidJson(e: JsError) = ErrorWrapper("invalidJson", JsError.toJson(e).toString)

}