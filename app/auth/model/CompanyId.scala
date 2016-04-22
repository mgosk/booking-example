package auth.model

import java.util.UUID

import play.api.libs.json.{JsError, JsSuccess, _}
import play.api.mvc.PathBindable

case class CompanyId(id: UUID) extends AnyVal


object CompanyId {
  implicit def userIdBindable(implicit uuidBinder: PathBindable[UUID]) = new PathBindable[CompanyId] {
    override def bind(key: String, value: String): Either[String, CompanyId] = {
      for {
        id <- uuidBinder.bind(key, value).right
      } yield CompanyId(id)
    }

    override def unbind(key: String, userId: CompanyId): String = ???
  }

  def random = CompanyId(UUID.randomUUID)
  def fromString(string: String) = CompanyId(UUID.fromString(string))

  implicit val userIdWrites = new Writes[CompanyId] {
    override def writes(id: CompanyId): JsValue = JsString(id.id.toString)
  }

  implicit val userIdReads = new Reads[CompanyId] {
    override def reads(json: JsValue): JsResult[CompanyId] = json match {
      case JsString(string) => JsSuccess(CompanyId(UUID.fromString(string)))
      case _ => JsError("validate.error.invalidCompanyId")
    }
  }
}