package documents.model

import com.pellucid.sealerate
import play.api.libs.json._

sealed abstract class DocumentKind(val code: String)

object DocumentKind {

  case object SaleInvoice extends DocumentKind("saleInvoice")

  def values: Set[DocumentKind] = sealerate.values[DocumentKind]

  def apply(code: String): DocumentKind = values.find(_.code == code).getOrElse(throw new RuntimeException(s"Can't construct DocumentKind from: $code"))

  implicit val currencyWrites = new Writes[DocumentKind] {
    override def writes(o: DocumentKind): JsValue = JsString(o.code)
  }

  implicit val currencyReads = new Reads[DocumentKind] {
    override def reads(json: JsValue): JsResult[DocumentKind] = json match {
      case JsString(string) => JsSuccess(DocumentKind(string))
      case _ => JsError("validate.error.DocumentKind")
    }
  }

}