package hotel.model

import hotel.protocol.CreateHotelRequest
import play.api.libs.json.Json

case class Hotel(id: HotelId, name:String, city: String, rooms: Seq[Room])

object Hotel {

  implicit val format = Json.format[Hotel]

  implicit def fromRequest(hotel: CreateHotelRequest) = Hotel(
    id = HotelId.random,
    name = hotel.name,
    city = hotel.city,
    rooms = Seq.empty[Room])

}