package sale.model

import java.util.UUID

import play.api.libs.json._
import play.api.mvc.PathBindable

case class InvoiceVersion(version: UUID) extends AnyVal

object InvoiceVersion {

  implicit val invoiceVersionWrites = new Writes[InvoiceVersion] {
    override def writes(version: InvoiceVersion): JsValue = JsString(version.version.toString)
  }

  implicit val invoiceVersionReads = new Reads[InvoiceVersion] {
    override def reads(json: JsValue): JsResult[InvoiceVersion] = json match {
      case JsString(string) => JsSuccess(InvoiceVersion(UUID.fromString(string)))
      case _ => JsError("validate.error.invalidInvoiceVersion")
    }
  }

  implicit def userIdBindable(implicit uuidBinder: PathBindable[UUID]) = new PathBindable[InvoiceVersion] {
    override def bind(key: String, value: String): Either[String, InvoiceVersion] = {
      for {
        version <- uuidBinder.bind(key, value).right
      } yield InvoiceVersion(version)
    }

    override def unbind(key: String, version: InvoiceVersion): String = ???
  }

  def random = InvoiceVersion(UUID.randomUUID)
}