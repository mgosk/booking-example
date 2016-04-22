package documents.model

import java.math.BigInteger
import java.security.SecureRandom

import play.api.libs.json.Json
import sale.model.InvoiceId

case class Document(accessCode: String, kind: DocumentKind, identifier: String)

object Document {

  implicit val documentFormat = Json.format[Document]

  implicit def fromEntity(document: DocumentEntity) =
    Document(
      accessCode = document._id,
      kind = document.kind,
      identifier = document.identifier)

  implicit def fromOption(option: Option[DocumentEntity]): Option[Document] = option.map(fromEntity(_))

  implicit def fromInvoice(invoiceId: InvoiceId) =
    Document(accessCode = generateToken, kind = DocumentKind.SaleInvoice, identifier = invoiceId.id.toString)

  private val random = new SecureRandom()
  private def generateToken: String = {
    new BigInteger(255, random).toString(32)
  }
}
