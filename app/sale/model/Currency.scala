package sale.model

import com.pellucid.sealerate
import play.api.libs.json._

sealed abstract class Currency(val code: String)

object Currency {

  case object PLN extends Currency("PLN")
  case object EUR extends Currency("EUR")
  case object USD extends Currency("USD")

  def values: Set[Currency] = sealerate.values[Currency]

  def apply(code: String): Currency = values.find(_.code == code).getOrElse(throw new RuntimeException(s"Can't construct Currency from: $code"))

  implicit val currencyWrites = new Writes[Currency] {
    override def writes(o: Currency): JsValue = JsString(o.code)
  }

  implicit val currencyReads = new Reads[Currency] {
    override def reads(json: JsValue): JsResult[Currency] = json match {
      case JsString(string) => JsSuccess(Currency(string))
      case _ => JsError("validate.error.invalidPaymentMethod")
    }
  }

}
