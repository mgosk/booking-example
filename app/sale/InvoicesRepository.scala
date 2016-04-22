package sale

import auth.model.Identity
import com.google.inject.{ImplementedBy, Singleton}
import play.api.Play.current
import play.api.libs.json.Json
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json._
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api.Cursor
import sale.model.{InvoiceEntity, InvoiceId}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@ImplementedBy(classOf[InvoicesRepositoryImpl])
trait InvoicesRepository {

  lazy val reactiveMongoApi = current.injector.instanceOf[ReactiveMongoApi]

  def insert(invoice: InvoiceEntity): Future[InvoiceEntity]

  def update(invoice: InvoiceEntity)(implicit identity: Identity): Future[InvoiceEntity]

  def find(id: InvoiceId)(implicit identity: Identity): Future[Option[InvoiceEntity]]

  def findAnonymously(id: InvoiceId): Future[Option[InvoiceEntity]]

  def find()(implicit identity: Identity): Future[Seq[InvoiceEntity]]

}

@Singleton
class InvoicesRepositoryImpl extends InvoicesRepository {

  def collection: JSONCollection = reactiveMongoApi.db.collection[JSONCollection]("sale.invoices")

  override def insert(invoice: InvoiceEntity): Future[InvoiceEntity] =
    collection.insert(invoice).map { lastError =>
      invoice
    }

  override def update(invoice: InvoiceEntity)(implicit identity: Identity): Future[InvoiceEntity] = {
    val selector = Json.obj("_id" -> invoice._id, "owner" -> identity.company.id)
    collection.update(selector, invoice).map { lastError =>
      invoice
    }
  }

  override def find(id: InvoiceId)(implicit identity: Identity): Future[Option[InvoiceEntity]] = {
    val selector = Json.obj("_id" -> id, "owner" -> identity.company.id)
    val cursor: Cursor[InvoiceEntity] = collection.find(selector).sort(Json.obj()).cursor[InvoiceEntity]()
    cursor.collect[Seq]().map(_.headOption)
  }

  override def findAnonymously(id: InvoiceId): Future[Option[InvoiceEntity]] = {
    val selector = Json.obj("_id" -> id)
    val cursor: Cursor[InvoiceEntity] = collection.find(selector).sort(Json.obj()).cursor[InvoiceEntity]()
    cursor.collect[Seq]().map(_.headOption)
  }

  override def find()(implicit identity: Identity): Future[Seq[InvoiceEntity]] = {
    val selector = Json.obj("owner" -> identity.company.id)
    val cursor: Cursor[InvoiceEntity] = collection.find(selector).sort(Json.obj()).cursor[InvoiceEntity]()
    cursor.collect[Seq]()
  }

}
