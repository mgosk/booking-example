package acc.model

import java.util.UUID

import play.api.libs.json._
import play.api.mvc.PathBindable

case class DocumentId(id: UUID) extends AnyVal

object DocumentId {

  implicit val invoiceIdWrites = new Writes[DocumentId] {
    override def writes(id: DocumentId): JsValue = JsString(id.id.toString)
  }

  implicit val invoiceIdReads = new Reads[DocumentId] {
    override def reads(json: JsValue): JsResult[DocumentId] = json match {
      case JsString(string) => JsSuccess(DocumentId(UUID.fromString(string)))
      case _ => JsError("validate.error.invalidDocumentId")
    }
  }

  implicit def userIdBindable(implicit uuidBinder: PathBindable[UUID]) = new PathBindable[DocumentId] {
    override def bind(key: String, value: String): Either[String, DocumentId] = {
      for {
        id <- uuidBinder.bind(key, value).right
      } yield DocumentId(id)
    }

    override def unbind(key: String, userId: DocumentId): String = ???
  }

  def random = DocumentId(UUID.randomUUID)
  def fromString(string: String) = DocumentId(UUID.fromString(string))
}