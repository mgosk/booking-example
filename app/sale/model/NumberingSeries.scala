package sale.model

import auth.model.{CompanyId, UserId}
import play.api.libs.json.Json

case class NumberingSeries(id: NumberingSeriesId,
                           owner: CompanyId,
                           name: String,
                           schema: String,
                           kind: NumberingSeriesKind,
                           primary: Boolean,
                           invoiceKind: InvoiceKind)

object NumberingSeries {

  implicit val numberingSeriesFormat = Json.format[NumberingSeries]

  implicit def fromEntity(ns: NumberingSeriesEntity): NumberingSeries = {
    NumberingSeries(id = ns._id,
      owner = ns.owner,
      name = ns.name,
      schema = ns.schema,
      kind = ns.kind,
      primary = ns.primary,
      invoiceKind = ns.invoiceKind)
  }

  implicit def fromEntityOption(option: Option[NumberingSeriesEntity]): Option[NumberingSeries] = {
    option.map(fromEntity(_))
  }

  implicit def fromEntitySeq(seq: Seq[NumberingSeriesEntity]): Seq[NumberingSeries] = {
    seq.map(fromEntity(_))
  }

}