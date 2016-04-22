package sale

import javax.inject.Inject

import auth.model.{Identity, User}
import core.model.RawFileWrapper
import it.innove.play.pdf.PdfGenerator
import play.api.Play
import sale.model._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class InvoicesService @Inject() (invoicesManager: InvoicesManager, numberingSeriesManager: NumberingSeriesManager, pdfGenerator: PdfGenerator) {

  def create(invoice: Invoice): Future[Invoice] =
    invoicesManager.create(invoice)

  def update(invoice: Invoice)(implicit identity: Identity): Future[Invoice] =
    invoicesManager.update(invoice)

  def find(id: InvoiceId)(implicit identity: Identity): Future[Option[Invoice]] =
    invoicesManager.find(id)

  def find()(implicit identity: Identity): Future[Seq[Invoice]] =
    invoicesManager.find()

  def getPdfAnonymously(id: InvoiceId): Future[Option[RawFileWrapper]] =
    invoicesManager.findAnonymously(id).map {
      case Some(invoice) =>
        val pdf = pdfGenerator.toBytes(sale.html.invoicePdf(invoice), Play.current.configuration.getString(path = "application.url").get)
        Some(RawFileWrapper(pdf, s"faktura-${invoice.numberingInfo.name}.pdf"))
      case None => None
    }

  def getHtmlAnonymously(id: InvoiceId): Future[Option[String]] =
    invoicesManager.findAnonymously(id).map {
      case Some(invoice) =>
        Some(sale.html.invoicePdf.apply(invoice).toString)
      case None => None
    }

  def findNumberingSeries()(implicit identity: Identity): Future[Seq[NumberingSeries]] =
    numberingSeriesManager.find

  def findNumberingSeries(invoiceKind: InvoiceKind)(implicit identity: Identity): Future[Seq[NumberingSeries]] =
    numberingSeriesManager.find(invoiceKind)

  def findNumberingSeries(numberingSeriesId: NumberingSeriesId)(implicit identity: Identity): Future[Option[NumberingSeries]] =
    numberingSeriesManager.find(numberingSeriesId)

  def getNumberingInfo(numberingSeries: NumberingSeries): Future[Seq[NumberingInfo]] = numberingSeriesManager.getNumberingInfo(numberingSeries)

  def setNumberingInfo(numberingInfo: NumberingInfo): Future[NumberingInfo] = numberingSeriesManager.setNumberingInfo(numberingInfo)

}
