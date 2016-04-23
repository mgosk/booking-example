package hotel.model

import play.api.libs.json.Json

case class Room(number: Int, price: Long)

object Room {

  implicit val format = Json.format[Room]
}