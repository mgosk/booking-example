package sale.model

import com.pellucid.sealerate
import play.api.libs.json._

sealed abstract class PaymentMethod(val code: String, val plName: String)

object PaymentMethod {

  case object Cash extends PaymentMethod("cash","gotówka")
  case object BankTransfer extends PaymentMethod("bankTransfer","przelew")

  def values: Set[PaymentMethod] = sealerate.values[PaymentMethod]

  def apply(code: String): PaymentMethod = values.find(_.code == code).getOrElse(throw new RuntimeException(s"Can't construct PaymentMethod from: $code"))

  implicit val statusWrites = new Writes[PaymentMethod] {
    override def writes(o: PaymentMethod): JsValue = JsString(o.code)
  }

  implicit val statusReads = new Reads[PaymentMethod] {
    override def reads(json: JsValue): JsResult[PaymentMethod] = json match {
      case JsString(string) => JsSuccess(PaymentMethod(string))
      case _ => JsError("validate.error.invalidPaymentMethod")
    }
  }

}
