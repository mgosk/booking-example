package sale.model

import com.pellucid.sealerate
import play.api.libs.json._

sealed abstract class NumberingSeriesKind(val code: String)

object NumberingSeriesKind {

  case object Yearly extends NumberingSeriesKind("yearly")
  case object Monthly extends NumberingSeriesKind("monthly")
  case object Custom extends NumberingSeriesKind("custom")

  def values: Set[NumberingSeriesKind] = sealerate.values[NumberingSeriesKind]

  def apply(code: String): NumberingSeriesKind = values.find(_.code == code).getOrElse(throw new RuntimeException(s"Can't construct NumberingSeriesKind from: $code"))

  implicit val currencyWrites = new Writes[NumberingSeriesKind] {
    override def writes(o: NumberingSeriesKind): JsValue = JsString(o.code)
  }

  implicit val currencyReads = new Reads[NumberingSeriesKind] {
    override def reads(json: JsValue): JsResult[NumberingSeriesKind] = json match {
      case JsString(string) => JsSuccess(NumberingSeriesKind(string))
      case _ => JsError("validate.error.NumberingSeriesKind")
    }
  }

}
