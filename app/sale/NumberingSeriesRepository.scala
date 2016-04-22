package sale

import auth.model.Identity
import com.google.inject.{ImplementedBy, Singleton}
import play.api.Play.current
import play.api.libs.json.Json
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json._
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api.Cursor
import sale.model.{InvoiceKind, NumberingSeriesEntity, NumberingSeriesId}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@ImplementedBy(classOf[NumberingSeriesRepositoryImpl])
trait NumberingSeriesRepository {

  lazy val reactiveMongoApi = current.injector.instanceOf[ReactiveMongoApi]

  def insert(ns: NumberingSeriesEntity): Future[NumberingSeriesEntity]

  def find()(implicit identity: Identity): Future[Seq[NumberingSeriesEntity]]

  def find(invoiceKind: InvoiceKind)(implicit identity: Identity): Future[Seq[NumberingSeriesEntity]]

  def find(id: NumberingSeriesId)(implicit identity: Identity): Future[Option[NumberingSeriesEntity]]

}

@Singleton
class NumberingSeriesRepositoryImpl extends NumberingSeriesRepository {

  def collection: JSONCollection = reactiveMongoApi.db.collection[JSONCollection]("sale.numberingSeries")

  override def insert(ns: NumberingSeriesEntity): Future[NumberingSeriesEntity] =
    collection.insert(ns).map { lastError =>
      ns
    }

  override def find()(implicit identity: Identity): Future[Seq[NumberingSeriesEntity]] = {
    val selector = Json.obj("owner" -> identity.company.id)
    val cursor: Cursor[NumberingSeriesEntity] = collection.find(selector).sort(Json.obj()).cursor[NumberingSeriesEntity]()
    cursor.collect[Seq]()
  }

  override def find(invoiceKind: InvoiceKind)(implicit identity: Identity): Future[Seq[NumberingSeriesEntity]] = {
    val selector = Json.obj("owner" -> identity.company.id, "invoiceKind" -> invoiceKind)
    val cursor: Cursor[NumberingSeriesEntity] = collection.find(selector).sort(Json.obj()).cursor[NumberingSeriesEntity]()
    cursor.collect[Seq]()
  }

  override def find(id: NumberingSeriesId)(implicit identity: Identity): Future[Option[NumberingSeriesEntity]] = {
    val cursor: Cursor[NumberingSeriesEntity] = collection.find(Json.obj("_id" -> id, "owner" -> identity.company.id)).sort(Json.obj()).cursor[NumberingSeriesEntity]()
    cursor.collect[Seq]().map(_.headOption)
  }

}
