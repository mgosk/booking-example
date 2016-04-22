package sale.model

import play.api.libs.json.Json
import vat.model.VatRate

case class InvoiceRecord(name: String,
                         pkwiu: Option[String],
                         quantity: Int,
                         unit: Option[String],
                         netPrice: BigDecimal,
                         netValue: BigDecimal,
                         vatRate: VatRate,
                         vatValue: BigDecimal,
                         grossValue: BigDecimal)

object InvoiceRecord{

  implicit val invoiceRecordFormat = Json.format[InvoiceRecord]

}