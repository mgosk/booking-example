package auth.repositories

import auth.model.{CompanyDataEntity, CompanyId}
import com.google.inject.{ImplementedBy, Singleton}
import play.api.Play.current
import play.api.libs.json.Json
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json._
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api.Cursor

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@ImplementedBy(classOf[CompanyDataRepositoryImpl])
trait CompanyDataRepository {

  lazy val reactiveMongoApi = current.injector.instanceOf[ReactiveMongoApi]

  def upsert(od: CompanyDataEntity): Future[CompanyDataEntity]

  def find(id: CompanyId): Future[Option[CompanyDataEntity]]

}

@Singleton
class CompanyDataRepositoryImpl extends CompanyDataRepository {

  def collection: JSONCollection = reactiveMongoApi.db.collection[JSONCollection]("auth.companyData")

  override def upsert(od: CompanyDataEntity): Future[CompanyDataEntity] = {
    val selector = Json.obj("_id" -> od._id)
    collection.update(selector, od, upsert = true).map { lastError =>
      od
    }
  }

  override def find(id: CompanyId): Future[Option[CompanyDataEntity]] = {
    val cursor: Cursor[CompanyDataEntity] = collection.find(Json.obj("_id" -> id)).sort(Json.obj()).cursor[CompanyDataEntity]()
    cursor.collect[Seq]().map(_.headOption)
  }
}
