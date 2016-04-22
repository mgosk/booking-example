package contractors

import auth.model.Identity
import com.google.inject.{ImplementedBy, Singleton}
import contractors.model.{ContractorEntity, ContractorId}
import core.model.State
import play.api.Play._
import play.api.libs.json.Json
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json._
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api.Cursor

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@ImplementedBy(classOf[ContractorsRepositoryImpl])
trait ContractorsRepository {

  lazy val reactiveMongoApi = current.injector.instanceOf[ReactiveMongoApi]

  def insert(contractor: ContractorEntity): Future[ContractorEntity]

  def update(contractor: ContractorEntity)(implicit identity: Identity): Future[ContractorEntity]

  def find(id: ContractorId)(implicit identity: Identity): Future[Option[ContractorEntity]]

  def find(sortKey: String, sortReverse: Boolean)(implicit identity: Identity): Future[Seq[ContractorEntity]]

  def findByTerm(term: String)(implicit identity: Identity): Future[Seq[ContractorEntity]]

}

@Singleton
class ContractorsRepositoryImpl extends ContractorsRepository {

  def collection: JSONCollection = reactiveMongoApi.db.collection[JSONCollection]("contractors")

  override def insert(contractor: ContractorEntity): Future[ContractorEntity] =
    collection.insert(contractor).map { lastError =>
      contractor
    }

  override def update(contractor: ContractorEntity)(implicit identity: Identity): Future[ContractorEntity] = {
    val selector = Json.obj("_id" -> contractor._id, "owner" -> identity.company.id)
    collection.update(selector, contractor).map { lastError =>
      contractor
    }
  }

  override def find(id: ContractorId)(implicit identity: Identity): Future[Option[ContractorEntity]] = {
    val selector = Json.obj("_id" -> id, "state" -> State.Active, "owner" -> identity.company.id)
    val cursor: Cursor[ContractorEntity] = collection.find(selector).sort(Json.obj()).cursor[ContractorEntity]()
    cursor.collect[Seq]().map(seq =>
      seq.headOption)
  }

  override def find(sortKey: String, sortReverse: Boolean)(implicit identity: Identity): Future[Seq[ContractorEntity]] = {
    val selector = Json.obj("state" -> State.Active, "owner" -> identity.company.id)
    val sorter = Json.obj(
      (sortKey match {
        case "name" => "name"
        case _ => "nip"
      })
        -> (if (sortReverse) -1 else 1))
    val cursor: Cursor[ContractorEntity] = collection.find(selector).sort(sorter).cursor[ContractorEntity]()
    cursor.collect[Seq]()
  }

  override def findByTerm(term: String)(implicit identity: Identity): Future[Seq[ContractorEntity]] = {
    val selector = Json.obj("state" -> State.Active, "owner" -> identity.company.id, "searchIndex" -> Json.obj("$regex" -> s".*$term.*", "$options" -> "i"))
    val cursor: Cursor[ContractorEntity] = collection.find(selector).sort(Json.obj()).cursor[ContractorEntity]()
    cursor.collect[Seq]()
  }

}
