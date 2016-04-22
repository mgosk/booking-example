package sale.model

import java.util.UUID

import play.api.libs.json._
import play.api.mvc.PathBindable

case class NumberingSeriesId(id: UUID) extends AnyVal

object NumberingSeriesId {

  implicit val numberingSeriesIdWrites = new Writes[NumberingSeriesId] {
    override def writes(id: NumberingSeriesId): JsValue = JsString(id.id.toString)
  }

  implicit val numberingSeriesIdReads = new Reads[NumberingSeriesId] {
    override def reads(json: JsValue): JsResult[NumberingSeriesId] = json match {
      case JsString(string) => JsSuccess(NumberingSeriesId(UUID.fromString(string)))
      case _ => JsError("validate.error.invalidNumberingSeriesId")
    }
  }

  implicit def userIdBindable(implicit uuidBinder: PathBindable[UUID]) = new PathBindable[NumberingSeriesId] {
    override def bind(key: String, value: String): Either[String, NumberingSeriesId] = {
      for {
        id <- uuidBinder.bind(key, value).right
      } yield NumberingSeriesId(id)
    }

    override def unbind(key: String, id: NumberingSeriesId): String = ???
  }

  def random = NumberingSeriesId(UUID.randomUUID)
}