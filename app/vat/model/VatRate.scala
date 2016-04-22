package vat.model

import com.pellucid.sealerate
import play.api.libs.json._

sealed abstract class VatRate(val code: String, val name: String, val rate: BigDecimal)

object VatRate {

  case object Vat23 extends VatRate("vat23", "23%", 0.23)
  case object Vat8 extends VatRate("vat8", "8%", 0.08)
  case object Vat5 extends VatRate("vat5", "5%", 0.05)
  case object Vat0 extends VatRate("vat0", "0%", 0)
  case object VatExempt extends VatRate("vatExempt", "Zwolniony", 0)

  def values: Set[VatRate] = sealerate.values[VatRate]

  def apply(code: String): VatRate = values.find(_.code == code).getOrElse(throw new RuntimeException(s"Can't construct VatRate from: $code"))

  implicit val statusWrites = new Writes[VatRate] {
    override def writes(vatRate: VatRate): JsValue = JsString(vatRate.code)
  }

  implicit val statusReads = new Reads[VatRate] {
    override def reads(json: JsValue): JsResult[VatRate] = json match {
      case JsString(string) => JsSuccess(VatRate(string))
      case _ => JsError("validate.error.invalidVatRate")
    }
  }

}