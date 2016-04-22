package sale.model

import auth.model._
import contractors.model.Contractor
import org.joda.time.DateTime
import play.api.libs.json.Json
import sale.{InvoiceRequest, NumberingInfo}

case class Invoice(id: InvoiceId,
                   version: InvoiceVersion,
                   owner: CompanyId,
                   timestamp: DateTime,
                   issueDate: DateTime,
                   saleDate: DateTime,
                   paymentDate: DateTime,
                   paymentMethod: PaymentMethod,
                   bankAccount: Option[String],
                   bankName: Option[String],
                   records: Seq[InvoiceRecord],
                   netValue: BigDecimal,
                   grossValue: BigDecimal,
                   currency: Currency,
                   buyerName: Option[String],
                   sellerName: Option[String],
                   notes: Option[String],
                   numberingInfo: NumberingInfo,
                   contractor: Contractor,
                   organizationData: CompanyData,
                   accessCode: Option[String],
                   paidValue: BigDecimal) {

  def toPayValue: BigDecimal = grossValue - paidValue
}

object Invoice {

  import core.CustomDateTimeFormat._
  implicit val invoiceWrites = Json.writes[Invoice]

  implicit def fromEntity(invoice: InvoiceEntity): Invoice = {
    Invoice(id = invoice._id,
      version = invoice.version,
      owner = invoice.owner,
      timestamp = invoice.timestamp,
      issueDate = invoice.issueDate,
      saleDate = invoice.saleDate,
      paymentDate = invoice.paymentDate,
      paymentMethod = invoice.paymentMethod,
      bankAccount = invoice.bankAccount,
      bankName = invoice.bankName,
      records = invoice.records,
      netValue = invoice.netValue,
      grossValue = invoice.grossValue,
      currency = invoice.currency,
      buyerName = invoice.buyerName,
      sellerName = invoice.sellerName,
      notes = invoice.notes,
      numberingInfo = invoice.numberingInfo,
      contractor = invoice.contractor,
      organizationData = invoice.organizationData,
      accessCode = invoice.accessCode,
      paidValue = invoice.paidValue)
  }

  def fromRequest(invoice: InvoiceRequest, idOpt: Option[InvoiceId], numberingInfo: NumberingInfo, organizationData: CompanyData, accessCode: Option[String])(implicit identity: Identity): Invoice = {
    Invoice(id = idOpt.getOrElse(InvoiceId.random),
      version = InvoiceVersion.random,
      owner = identity.company.id,
      timestamp = DateTime.now,
      issueDate = invoice.issueDate,
      saleDate = invoice.saleDate,
      paymentDate = invoice.paymentDate,
      paymentMethod = invoice.paymentMethod,
      bankAccount = invoice.bankAccount,
      bankName = invoice.bankName,
      records = invoice.records,
      netValue = invoice.records.map(_.netValue).sum,
      grossValue = invoice.records.map(_.grossValue).sum,
      currency = invoice.currency,
      buyerName = invoice.buyerName,
      sellerName = invoice.sellerName,
      notes = invoice.notes,
      numberingInfo = numberingInfo,
      contractor = invoice.contractor,
      organizationData = organizationData,
      accessCode = accessCode,
      paidValue = invoice.paidValue)
  }

  implicit def fromEntityOption(option: Option[InvoiceEntity]): Option[Invoice] = {
    option.map(fromEntity(_))
  }

  implicit def fromEntitySeq(seq: Seq[InvoiceEntity]): Seq[Invoice] = {
    seq.map(fromEntity(_))
  }
}


case class InvoiceEntity(_id: InvoiceId,
                         version: InvoiceVersion,
                         owner: CompanyId,
                         timestamp: DateTime,
                         issueDate: DateTime,
                         saleDate: DateTime,
                         paymentDate: DateTime,
                         paymentMethod: PaymentMethod,
                         bankAccount: Option[String],
                         bankName: Option[String],
                         records: Seq[InvoiceRecord],
                         netValue: BigDecimal,
                         grossValue: BigDecimal,
                         currency: Currency,
                         buyerName: Option[String],
                         sellerName: Option[String],
                         notes: Option[String],
                         numberingInfo: NumberingInfo,
                         contractor: Contractor,
                         organizationData: CompanyData,
                         accessCode: Option[String],
                         paidValue: BigDecimal)



object InvoiceEntity {

  implicit val invoiceEntityFormat = Json.format[InvoiceEntity]

  implicit def fromInvoice(invoice: Invoice): InvoiceEntity = {
    InvoiceEntity(_id = invoice.id,
      version = invoice.version,
      owner = invoice.owner,
      timestamp = invoice.timestamp,
      issueDate = invoice.issueDate,
      saleDate = invoice.saleDate,
      paymentDate = invoice.paymentDate,
      paymentMethod = invoice.paymentMethod,
      bankAccount = invoice.bankAccount,
      bankName = invoice.bankName,
      records = invoice.records,
      netValue = invoice.netValue,
      grossValue = invoice.grossValue,
      currency = invoice.currency,
      buyerName = invoice.buyerName,
      sellerName = invoice.sellerName,
      notes = invoice.notes,
      numberingInfo = invoice.numberingInfo,
      contractor = invoice.contractor,
      organizationData = invoice.organizationData,
      accessCode = invoice.accessCode,
      paidValue = invoice.paidValue)
  }
}