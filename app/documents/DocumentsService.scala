package documents

import javax.inject.Inject

import core.model.RawFileWrapper
import documents.model.{ Document, DocumentKind }
import sale.InvoicesService
import sale.model.InvoiceId

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class DocumentsService @Inject() (documentsManager: DocumentsManager, invoiceService: InvoicesService) {

  def create(document: Document): Future[Document] =
    documentsManager.create(document)

  def find(accessCode: String): Future[Option[Document]] =
    documentsManager.find(accessCode)

  def getRawPdf(accessCode: String): Future[Option[RawFileWrapper]] = {
    documentsManager.find(accessCode).flatMap {
      case Some(document) =>
        document.kind match {
          case DocumentKind.SaleInvoice =>
            invoiceService.getPdfAnonymously(InvoiceId.fromString(document.identifier))
        }
      case None => Future.successful(None)
    }
  }

  def getRawHtml(accessCode: String): Future[Option[String]] = {
    documentsManager.find(accessCode).flatMap {
      case Some(document) =>
        document.kind match {
          case DocumentKind.SaleInvoice =>
            invoiceService.getHtmlAnonymously(InvoiceId.fromString(document.identifier))
        }
      case None => Future.successful(None)
    }
  }

}
