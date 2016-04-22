package sale

import java.time.Year

import play.api.libs.json.Json
import sale.model._

case class SimpleNumberingSeries(id: NumberingSeriesId,
                                 name: String,
                                 schema: String,
                                 primary: Boolean,
                                 kind: NumberingSeriesKind,
                                 invoiceKind: InvoiceKind)

object SimpleNumberingSeries {

  implicit val simpleNumberingSeriesFormat = Json.format[SimpleNumberingSeries]

  def fromNumberingSeries(ns: NumberingSeries) =
    SimpleNumberingSeries(
      id = ns.id,
      name = ns.name,
      schema = ns.schema,
      primary = ns.primary,
      kind = ns.kind,
      invoiceKind = ns.invoiceKind)
}

//TODO add pagination etc
case class NumberingSeriesList(data: Seq[SimpleNumberingSeries])

object NumberingSeriesList {

  implicit val numberingSeriesListWrites = Json.writes[NumberingSeriesList]

}

case class SetNextNumberRequest(idx: Option[Int], month: Option[String], year: Option[String])

object SetNextNumberRequest {

  implicit val setNextNumberRequestReads = Json.reads[SetNextNumberRequest]

}