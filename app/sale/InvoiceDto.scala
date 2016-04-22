package sale

import contractors.model.Contractor
import org.joda.time.DateTime
import play.api.libs.json.Json
import sale.model._

case class InvoicesListRecord(id: InvoiceId, name: String, issueDate: DateTime, saleDate: DateTime, netValue: BigDecimal, grossValue: BigDecimal, contractor: String)

object InvoicesListRecord {

  implicit val invoicesListRecordWrites = Json.writes[InvoicesListRecord]

  def fromInvoice(invoice: Invoice) =
    InvoicesListRecord(
      id = invoice.id,
      name = invoice.numberingInfo.name,
      issueDate = invoice.issueDate,
      saleDate = invoice.saleDate,
      netValue = invoice.netValue,
      grossValue = invoice.grossValue,
      contractor = invoice.contractor.name)
}

//TODO add pagination etc
case class InvoicesList(data: Seq[InvoicesListRecord])

object InvoicesList {
  implicit val invoicesListWrites = Json.writes[InvoicesList]

}

case class InvoiceRequest(issueDate: DateTime,
  saleDate: DateTime,
  paymentDate: DateTime,
  paymentMethod: PaymentMethod,
  bankAccount: Option[String] = None,
  bankName: Option[String] = None,
  records: Seq[InvoiceRecord],
  currency: Currency,
  buyerName: Option[String] = None,
  sellerName: Option[String] = None,
  notes: Option[String] = None,
  numberingSeries: SimpleNumberingSeries,
  name: Option[String],
  contractor: Contractor,
  paidValue: BigDecimal)

object InvoiceRequest {

  import core.CustomDateTimeFormat._
  implicit val invoiceRequestReads = Json.reads[InvoiceRequest]

}

