package sale.controllers

import java.util.{ArrayList => JavaArrayList, List => JavaList}
import javax.inject.Inject

import auth.services.{AuthService, CompanyDataService}
import com.google.common.io.BaseEncoding
import core.AppController
import core.config.AuthorityImpl
import core.model.{Base64FileWrapper, ErrorWrapper}
import documents.DocumentsService
import documents.model.Document
import it.innove.play.pdf.PdfGenerator
import play.api.Play
import play.api.libs.json.{JsError, JsSuccess, Json}
import play.api.mvc.BodyParsers
import sale._
import sale.model.{Invoice, InvoiceId, InvoiceKind}

import scala.concurrent.Future


class InvoiceController @Inject() (override val authService: AuthService, invoiceService: InvoicesService, pdfGenerator: PdfGenerator,
                                   organizationDataService: CompanyDataService, documentsService: DocumentsService, numberingSeriesManager: NumberingSeriesManager) extends AppController {

  def create() = AsyncStack(BodyParsers.parse.json, AuthorityKey -> AuthorityImpl()) { implicit request =>
    //Temporary we have only VAT invoices but later we can add other types
    val invoiceKind:InvoiceKind = InvoiceKind.FakturaVat
    request.body.validate[InvoiceRequest] match {
      case s: JsSuccess[InvoiceRequest] =>
        numberingSeriesManager.generateNumberingInfo(s.get.numberingSeries.id, s.get.name, s.get.issueDate).flatMap {
          case Right(ni) =>
            organizationDataService.find().flatMap {
              case Some(orgData) =>
                val invoice = Invoice.fromRequest(s.get, None, ni, orgData, None)
                documentsService.create(Document.fromInvoice(invoice.id)).flatMap { doc =>
                  invoiceService.create(invoice.copy(accessCode = Some(doc.accessCode))).map { invoice =>
                    Ok(Json.toJson(invoice))
                  }
                }
              case None =>
                Future.successful(BadRequest(Json.toJson(ErrorWrapper("companyDataNotExist", "Najpierw uzupełnił dane firmy"))))
            }
          case Left(error) =>
            Future.successful(BadRequest(Json.toJson(error)))
        }
      case e: JsError =>
        Future.successful(BadRequest(Json.toJson(ErrorWrapper("invalidJson", JsError.toJson(e).toString))))
    }
  }

  def update(id: InvoiceId) = AsyncStack(BodyParsers.parse.json, AuthorityKey -> AuthorityImpl()) { implicit request =>
    request.body.validate[InvoiceRequest] match {
      case s: JsSuccess[InvoiceRequest] =>
        invoiceService.find(id).flatMap {
          case Some(existing) =>
            organizationDataService.find().flatMap {
              case Some(orgData) =>
                val invoice = Invoice.fromRequest(s.get, Some(id), existing.numberingInfo, orgData, existing.accessCode)
                invoiceService.update(invoice).map { invoice =>
                  Ok(Json.toJson(invoice))
                }
              case None =>
                Future.successful(BadRequest(Json.toJson(ErrorWrapper("companyDataNotExist", "Najpierw uzupełnił dane firmy"))))
            }
          case None =>
            Future.successful(BadRequest(Json.toJson(ErrorWrapper("notExist", "Faktura nie istnieje"))))
        }
      case e: JsError =>
        Future.successful(BadRequest(Json.toJson(ErrorWrapper("invalidJson", JsError.toJson(e).toString))))
    }
  }

  def list() = AsyncStack(AuthorityKey -> AuthorityImpl()) { implicit request =>
    invoiceService.find().map { seq =>
      Ok(Json.toJson(InvoicesList(seq.map(InvoicesListRecord.fromInvoice(_)))))
    }
  }

  def get(id: InvoiceId) = AsyncStack(AuthorityKey -> AuthorityImpl()) { implicit request =>
    invoiceService.find(id).map {
      case Some(invoice) => Ok(Json.toJson(invoice))
      case None => BadRequest(Json.toJson(ErrorWrapper("notExist", "Faktura nie istnieje")))
    }
  }

  //http://blog.techdev.de/an-angularjs-directive-to-download-pdf-files/
  def pdf(id: InvoiceId) = AsyncStack(AuthorityKey -> AuthorityImpl()) { implicit request =>
    invoiceService.find(id).map {
      case Some(invoice) =>
        val pdf = pdfGenerator.toBytes(sale.html.invoicePdf(invoice), Play.current.configuration.getString(path = "application.url").get)
        Ok(Json.toJson(Base64FileWrapper(BaseEncoding.base64.encode(pdf), s"faktura-${invoice.numberingInfo.name}.pdf")))
      case None => BadRequest("")
    }
  }

  def publish(id: InvoiceId) = AsyncStack(AuthorityKey -> AuthorityImpl()) { implicit request =>
    invoiceService.find(id).flatMap {
      case Some(invoice) =>
        documentsService.create(Document.fromInvoice(invoice.id)).map { doc =>
          Ok(doc.accessCode)
        }
      case None => Future.successful(BadRequest(""))
    }
  }

}
