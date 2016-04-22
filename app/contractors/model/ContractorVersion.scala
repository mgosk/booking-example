package contractors.model

import java.util.UUID

import play.api.libs.json._
import play.api.mvc.PathBindable

case class ContractorVersion(version: UUID) extends AnyVal

object ContractorVersion {

  implicit val contractorVersionWrites = new Writes[ContractorVersion] {
    override def writes(version: ContractorVersion): JsValue = JsString(version.version.toString)
  }

  implicit val contractorVersionReads = new Reads[ContractorVersion] {
    override def reads(json: JsValue): JsResult[ContractorVersion] = json match {
      case JsString(string) => JsSuccess(ContractorVersion(UUID.fromString(string)))
      case _ => JsError("validate.error.invalidContractorVersion")
    }
  }

  implicit def userVersionBindable(implicit uuidBinder: PathBindable[UUID]) = new PathBindable[ContractorVersion] {
    override def bind(key: String, value: String): Either[String, ContractorVersion] = {
      for {
        version <- uuidBinder.bind(key, value).right
      } yield ContractorVersion(version)
    }

    override def unbind(key: String, version: ContractorVersion): String = ???
  }

  def random = ContractorVersion(UUID.randomUUID)
}