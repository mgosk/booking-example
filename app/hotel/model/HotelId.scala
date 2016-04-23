package hotel.model

import java.util.UUID

import play.api.libs.json._
import play.api.mvc.PathBindable

case class HotelId(id: UUID)

object HotelId {

  implicit val format = new Format[HotelId] {
    override def writes(id: HotelId): JsValue = JsString(id.id.toString)

    override def reads(json: JsValue): JsResult[HotelId] = json match {
      case JsString(string) => JsSuccess(HotelId(UUID.fromString(string)))
      case _ => JsError("validate.error.invalidHotelId")
    }
  }

  implicit def userIdBindable(implicit uuidBinder: PathBindable[UUID]) = new PathBindable[HotelId] {
    override def bind(key: String, value: String): Either[String, HotelId] = {
      for {
        id <- uuidBinder.bind(key, value).right
      } yield HotelId(id)
    }

    override def unbind(key: String, id: HotelId): String = ???
  }

  def random = HotelId(UUID.randomUUID)
  def fromString(string: String) = HotelId(UUID.fromString(string))
}