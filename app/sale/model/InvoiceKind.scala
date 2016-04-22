package sale.model

import com.pellucid.sealerate
import play.api.libs.json.{JsError, JsSuccess, _}
import play.api.mvc.PathBindable

sealed abstract class InvoiceKind(val code: String, val name: String)

object InvoiceKind {

  implicit def userIdBindable(implicit stringBinder: PathBindable[String]) = new PathBindable[InvoiceKind] {
    override def bind(key: String, value: String): Either[String, InvoiceKind] = {
      for {
        string <- stringBinder.bind(key, value).right
      } yield InvoiceKind(string)
    }

    override def unbind(key: String, invoiceKind: InvoiceKind): String = ???
  }

  case object FakturaVat extends InvoiceKind("fakturaVat", "Faktura")

  def values: Set[InvoiceKind] = sealerate.values[InvoiceKind]

  def apply(code: String): InvoiceKind = values.find(_.code == code).getOrElse(throw new RuntimeException(s"Can't construct InvoiceKind from: $code"))

  implicit val statusWrites = new Writes[InvoiceKind] {
    override def writes(vatRate: InvoiceKind): JsValue = JsString(vatRate.code)
  }

  implicit val statusReads = new Reads[InvoiceKind] {
    override def reads(json: JsValue): JsResult[InvoiceKind] = json match {
      case JsString(string) => JsSuccess(InvoiceKind(string))
      case _ => JsError("validate.error.invalidInvoiceKind")
    }
  }

}