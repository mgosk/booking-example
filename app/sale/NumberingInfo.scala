package sale

import play.api.libs.json.Json
import sale.model.NumberingSeries

case class NumberingInfo(numberingSeries: NumberingSeries,
  month: Option[String],
  year: Option[String],
  idx: Option[Int],
  name: String)

object NumberingInfo {

  implicit val numberingInfoFormat = Json.format[NumberingInfo]

}