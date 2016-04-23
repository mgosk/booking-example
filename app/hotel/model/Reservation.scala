package hotel.model

import java.util.Date

import play.api.libs.json.Json

case class Reservation(hotelId: HotelId, roomNr: Int, dateFrom: Date, dateTo: Date, login: String)

object Reservation {

  implicit val format = Json.format[Reservation]

}