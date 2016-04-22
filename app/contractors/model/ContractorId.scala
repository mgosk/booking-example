package contractors.model

import java.util.UUID

import play.api.libs.json._
import play.api.mvc.PathBindable

case class ContractorId(id: UUID) extends AnyVal

object ContractorId {

  implicit val contractorIdWrites = new Writes[ContractorId] {
    override def writes(id: ContractorId): JsValue = JsString(id.id.toString)
  }

  implicit val contractorIdReads = new Reads[ContractorId] {
    override def reads(json: JsValue): JsResult[ContractorId] = json match {
      case JsString(string) => JsSuccess(ContractorId(UUID.fromString(string)))
      case _ => JsError("validate.error.invalidContractorId")
    }
  }

  implicit def userIdBindable(implicit uuidBinder: PathBindable[UUID]) = new PathBindable[ContractorId] {
    override def bind(key: String, value: String): Either[String, ContractorId] = {
      for {
        id <- uuidBinder.bind(key, value).right
      } yield ContractorId(id)
    }

    override def unbind(key: String, id: ContractorId): String = ???
  }

  def random = ContractorId(UUID.randomUUID)

}