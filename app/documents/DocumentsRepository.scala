package documents

import com.google.inject.{ImplementedBy, Singleton}
import documents.model.DocumentEntity
import play.api.Play.current
import play.api.libs.json.Json
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json._
import play.modules.reactivemongo.json.collection.JSONCollection

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@ImplementedBy(classOf[DocumentsRepositoryImpl])
trait DocumentsRepository {

  lazy val reactiveMongoApi = current.injector.instanceOf[ReactiveMongoApi]

  def insert(document: DocumentEntity): Future[DocumentEntity]

  def find(accessCode: String): Future[Option[DocumentEntity]]

}

@Singleton
class DocumentsRepositoryImpl extends DocumentsRepository {

  def collection: JSONCollection = reactiveMongoApi.db.collection[JSONCollection]("documents")

  override def insert(document: DocumentEntity): Future[DocumentEntity] =
    collection.insert(document).map { lastError =>
      document
    }

  override def find(accessCode: String): Future[Option[DocumentEntity]] = {
    val selector = Json.obj("_id" -> accessCode)
    val cursor = collection.find(selector).sort(Json.obj()).cursor[DocumentEntity]()
    cursor.collect[Seq]().map(_.headOption)
  }

}
