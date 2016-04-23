package customer.model

import java.util.UUID

import play.api.libs.json._
import play.api.mvc.PathBindable

case class CustomerId(id: UUID)

object CustomerId {

  implicit val format = new Format[CustomerId] {
    override def writes(id: CustomerId): JsValue = JsString(id.id.toString)

    override def reads(json: JsValue): JsResult[CustomerId] = json match {
      case JsString(string) => JsSuccess(CustomerId(UUID.fromString(string)))
      case _ => JsError("validate.error.invalidCustomerId")
    }
  }

  implicit def userIdBindable(implicit uuidBinder: PathBindable[UUID]) = new PathBindable[CustomerId] {
    override def bind(key: String, value: String): Either[String, CustomerId] = {
      for {
        id <- uuidBinder.bind(key, value).right
      } yield CustomerId(id)
    }

    override def unbind(key: String, id: CustomerId): String = ???
  }

  def random = CustomerId(UUID.randomUUID)
  def fromString(string: String) = CustomerId(UUID.fromString(string))
}