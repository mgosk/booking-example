package sale.controllers

import javax.inject.Inject

import auth.services.AuthService
import core.AppController
import core.config.AuthorityImpl
import core.model.ErrorWrapper
import play.api.libs.json.{ JsError, JsSuccess, Json }
import play.api.mvc.BodyParsers
import sale.model.{ InvoiceKind, NumberingSeriesId }
import sale._

import scala.concurrent.Future

class NumberingSeriesController @Inject() (override val authService: AuthService, invoicesService: InvoicesService) extends AppController {

  //for invoice edit page
  def listByInvoiceKind(invoiceKind: InvoiceKind) = AsyncStack(AuthorityKey -> AuthorityImpl()) { implicit request =>
    //Temporary we have only VAT invoices but later we can add other types
    val invoiceKind: InvoiceKind = InvoiceKind.FakturaVat
    invoicesService.findNumberingSeries(invoiceKind).map { seq =>
      Ok(Json.toJson(NumberingSeriesList(seq.map(SimpleNumberingSeries.fromNumberingSeries(_)))))
    }
  }

  //for manage numbering-series page
  def list() = AsyncStack(AuthorityKey -> AuthorityImpl()) { implicit request =>
    invoicesService.findNumberingSeries.map { seq =>
      Ok(Json.toJson(NumberingSeriesList(seq.map(SimpleNumberingSeries.fromNumberingSeries(_)))))
    }
  }

  //for edit numbering-series page
  def get(numberingSeriesId: NumberingSeriesId) = AsyncStack(AuthorityKey -> AuthorityImpl()) { implicit request =>
    invoicesService.findNumberingSeries(numberingSeriesId).map {
      case Some(ns) =>
        Ok(Json.toJson(SimpleNumberingSeries.fromNumberingSeries(ns)))
      case None =>
        BadRequest(Json.toJson(ErrorWrapper.notExist))
    }
  }

  //for edit numbering-series page
  def getNextNumbers(numberingSeriesId: NumberingSeriesId) = AsyncStack(AuthorityKey -> AuthorityImpl()) { implicit request =>
    invoicesService.findNumberingSeries(numberingSeriesId).flatMap {
      case Some(ns) =>
        invoicesService.getNumberingInfo(ns).map { ni =>
          Ok(Json.toJson(ni))
        }
      case None =>
        Future.successful(BadRequest(Json.toJson(ErrorWrapper.notExist)))
    }
  }

  //for edit numbering-series page
  def setNextNumbers(numberingSeriesId: NumberingSeriesId) = AsyncStack(BodyParsers.parse.json, AuthorityKey -> AuthorityImpl()) { implicit request =>
    request.body.validate[SetNextNumberRequest] match {
      case s: JsSuccess[SetNextNumberRequest] =>
        invoicesService.findNumberingSeries(numberingSeriesId).flatMap {
          case Some(ns) =>
            val ni = NumberingInfo(
              numberingSeries = ns,
              month = s.get.month,
              year = s.get.year,
              idx = s.get.idx,
              name = "")
            invoicesService.setNumberingInfo(ni).map { ni =>
              Ok(Json.toJson(ni))
            }
          case None =>
            Future.successful(BadRequest(Json.toJson(ErrorWrapper.notExist)))
        }
      case e: JsError =>
        Future.successful(BadRequest(Json.toJson(ErrorWrapper.invalidJson(e))))
    }
  }

}
