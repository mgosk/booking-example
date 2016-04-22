package documents.model

import play.api.libs.json.Json

case class DocumentEntity(_id: String, kind: DocumentKind, identifier: String)


object DocumentEntity {

  implicit val documentEntityFormat = Json.format[DocumentEntity]

  implicit def fromBe(document: Document) =
    DocumentEntity(
      _id = document.accessCode,
      kind = document.kind,
      identifier = document.identifier)

}