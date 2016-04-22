package sale.model

import java.util.UUID

import play.api.libs.json._
import play.api.mvc.PathBindable

case class InvoiceId(id: UUID) extends AnyVal

object InvoiceId {

  implicit val invoiceIdWrites = new Writes[InvoiceId] {
    override def writes(id: InvoiceId): JsValue = JsString(id.id.toString)
  }

  implicit val invoiceIdReads = new Reads[InvoiceId] {
    override def reads(json: JsValue): JsResult[InvoiceId] = json match {
      case JsString(string) => JsSuccess(InvoiceId(UUID.fromString(string)))
      case _ => JsError("validate.error.invalidInvoiceId")
    }
  }

  implicit def userIdBindable(implicit uuidBinder: PathBindable[UUID]) = new PathBindable[InvoiceId] {
    override def bind(key: String, value: String): Either[String, InvoiceId] = {
      for {
        id <- uuidBinder.bind(key, value).right
      } yield InvoiceId(id)
    }

    override def unbind(key: String, userId: InvoiceId): String = ???
  }

  def random = InvoiceId(UUID.randomUUID)
  def fromString(string: String) = InvoiceId(UUID.fromString(string))
}