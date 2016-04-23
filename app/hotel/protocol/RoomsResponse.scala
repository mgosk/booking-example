package hotel.protocol

import hotel.model.HotelId
import play.api.libs.json.Json

case class RoomsResponse(hotelId: HotelId, roomNr: Int, price: Long)

object RoomsResponse {

  implicit val format = Json.format[RoomsResponse]
}
