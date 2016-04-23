package hotel.protocol

import play.api.libs.json.Json

case class CreateHotelRequest(name: String)

object CreateHotelRequest {

  implicit val reads = Json.reads[CreateHotelRequest]

}