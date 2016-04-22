package gus

import javax.inject.{ Inject, Singleton }

import gus.model.GusSearchResponse._
import gus.model._
import play.api.Logger
import play.api.cache.CacheApi
import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class GusService @Inject() (ws: WSClient, cache: CacheApi, gateway: GusGateway) {

  def findCompanyByNip(nip: String, captchaReq: Option[String], format: String): Future[GusSearchResponse] = {
    cache.get[String]("gus.sid") match {
      case None =>
        zaloguj()
      case Some(sid: String) if sid.isEmpty =>
        Future.successful(ExternalError())
      case Some(sid: String) =>
        captchaReq match {
          case Some(captcha) =>
            gateway.sprawdzCaptcha(sid, captcha).flatMap {
              case "true" =>
                getFormattedReport(nip, sid, format)
              case _ => gateway.pobierzCaptcha(sid).map { captcha =>
                CaptchaImageRepeat(captcha)
              }
            }
          case None =>
            gateway.getSearchStatus(sid).flatMap {
              case "0" | "4" =>
                getFormattedReport(nip, sid, format)
              case "1" =>
                gateway.pobierzCaptcha(sid).map { captcha =>
                  CaptchaImage(captcha)
                }
              case "7" => //refresh SID
                zaloguj()
              case str: String =>
                Logger.info(s"Undefined gus search status:$str current SID:$sid")
                gateway.getSessionStatus(sid).flatMap { sessionStatus =>
                  if (sessionStatus == "0")
                    zaloguj
                  else
                    Future.successful(ExternalError())
                }
            }
        }
    }
  }

  def getFormattedReport(nip: String, sid: String, format: String): Future[GusSearchResponse] = {
    getGusReport(sid, nip) flatMap {
      case Some(result) =>
        Future.successful {
          format match {
            case "contractor" => ContractorSearchResult(result)
            case "organizationData" => OrganizationDataSearchResult(result)
            case _ => SimpleSearchResult(result)
          }
        }
      case None =>
        gateway.getSearchStatus(sid).flatMap {
          case "1" =>
            gateway.pobierzCaptcha(sid).map { captcha =>
              CaptchaImageRepeat(captcha)
            }
          case _ =>
            Future.successful(GusSearchResponse.NotFound())
        }
    }
  }

  private def getGusReport(sid: String, nip: String): Future[Option[GusReport]] = {
    gateway.daneSzukaj(sid, nip).flatMap { findResult =>
      findResult match {
        case Some(findResult) =>
          findResult.silosID match {
            case "1" => gateway.pobierzPelnyRaport(sid, findResult.regon, GusReportType.PublDaneRaportDzialalnoscFizycznejCeidg)
            case "2" => gateway.pobierzPelnyRaport(sid, findResult.regon, GusReportType.PublDaneRaportDzialalnoscFizycznejRolnicza)
            case "3" => gateway.pobierzPelnyRaport(sid, findResult.regon, GusReportType.PublDaneRaportDzialalnoscFizycznejPozostala)
            case "4" => gateway.pobierzPelnyRaport(sid, findResult.regon, GusReportType.PublDaneRaportDzialalnoscFizycznejWKrupgn)
            case "6" => gateway.pobierzPelnyRaport(sid, findResult.regon, GusReportType.PublDaneRaportPrawna)
            case _ =>
              Logger.error("gus unsupported silosID")
              Future.successful(None)
          }
        case None => Future.successful(None)
      }
    }
  }

  private def zaloguj(): Future[GusSearchResponse] =
    gateway.zaloguj.flatMap { sid =>
      if (!sid.isEmpty) {
        Logger.info(s"gus. setting new SID:$sid")
        cache.set("gus.sid", sid)
        gateway.pobierzCaptcha(sid).map { captcha =>
          CaptchaImage(captcha)
        }
      } else {
        Logger.info("gus. new SID is empty")
        Future.successful(ExternalError())
      }
    }

}
