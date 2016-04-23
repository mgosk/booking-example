package hotel.model

import hotel.protocol.CreateHotelRequest
import play.api.libs.json.Json

case class Hotel(id: HotelId, name: String)

object Hotel {

  implicit val format = Json.format[Hotel]

  implicit def fromRequest(hotel: CreateHotelRequest) = Hotel(
    id = HotelId.random,
    name = hotel.name)

}