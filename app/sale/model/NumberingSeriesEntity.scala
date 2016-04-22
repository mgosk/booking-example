package sale.model

import auth.model.{CompanyId, UserId}
import play.api.libs.json.Json

case class NumberingSeriesEntity(_id: NumberingSeriesId,
                                 owner: CompanyId,
                                 name: String,
                                 schema: String,
                                 kind: NumberingSeriesKind,
                                 primary: Boolean,
                                 invoiceKind: InvoiceKind)

object NumberingSeriesEntity {

  implicit val numberingSeriesEntityFormat = Json.format[NumberingSeriesEntity]

  implicit def fromBe(ns: NumberingSeries): NumberingSeriesEntity = {
    NumberingSeriesEntity(_id = ns.id,
      owner = ns.owner,
      name = ns.name,
      schema = ns.schema,
      kind = ns.kind,
      primary = ns.primary,
      invoiceKind = ns.invoiceKind)
  }

}
