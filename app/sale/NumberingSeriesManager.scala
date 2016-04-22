package sale

import javax.inject.Inject

import auth.model.{ CompanyId, Identity }
import core.model.ErrorWrapper
import org.joda.time.DateTime
import org.sedis.Pool
import sale.model._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class NumberingSeriesManager @Inject() (numberingSeriesRepository: NumberingSeriesRepository, sedisPool: Pool) {

  private def key(numberingSeriesId: NumberingSeriesId, month: Option[String], year: Option[String]) = s"numbering-counter::${numberingSeriesId.id}:$month:$year"

  def find()(implicit identity: Identity): Future[Seq[NumberingSeries]] = numberingSeriesRepository.find.map { ns => ns }

  def find(invoiceKind: InvoiceKind)(implicit identity: Identity): Future[Seq[NumberingSeries]] =
    numberingSeriesRepository.find(invoiceKind).map { ns => ns }

  def find(id: NumberingSeriesId)(implicit identity: Identity): Future[Option[NumberingSeries]] =
    numberingSeriesRepository.find(id).map { ns => ns }

  //TODO optimize me, get multiple keys in one shot
  def getNumberingInfo(numberingSeries: NumberingSeries): Future[Seq[NumberingInfo]] = Future.successful {
    numberingSeries.kind match {
      case NumberingSeriesKind.Custom =>
        Seq()
      case NumberingSeriesKind.Yearly =>
        sedisPool.withJedisClient { client =>
          (2015 to 2017) map { year =>
            client.get(key(numberingSeries.id, None, Some(year.toString))) match {
              case null => NumberingInfo(numberingSeries, Some("N/A"), Some(year.toString), Some(1), "")
              case string => NumberingInfo(numberingSeries, Some("N/A"), Some(year.toString), Some(string.toInt + 1), "")
            }
          }
        }
      case NumberingSeriesKind.Monthly =>
        sedisPool.withJedisClient { client =>
          (2015 to 2016) map { year =>
            (1 to 12) map { month =>
              client.get(key(numberingSeries.id, Some("%02d".format(month)), Some(year.toString))) match {
                case null => NumberingInfo(numberingSeries, Some("%02d".format(month)), Some(year.toString), Some(1), "")
                case string => NumberingInfo(numberingSeries, Some("%02d".format(month)), Some(year.toString), Some(string.toInt + 1), "")
              }
            }
          }
        }.flatten
    }
  }

  def setNumberingInfo(numberingInfo: NumberingInfo): Future[NumberingInfo] = Future.successful {
    numberingInfo.numberingSeries.kind match {
      case NumberingSeriesKind.Custom =>
        numberingInfo
      case NumberingSeriesKind.Yearly =>
        sedisPool.withJedisClient { client =>
          client.set(key(numberingInfo.numberingSeries.id, None, numberingInfo.year), (numberingInfo.idx.get - 1).toString)
          numberingInfo
        }
      case NumberingSeriesKind.Monthly =>
        sedisPool.withJedisClient { client =>
          client.set(key(numberingInfo.numberingSeries.id, numberingInfo.month, numberingInfo.year), (numberingInfo.idx.get - 1).toString)
          numberingInfo
        }
    }
  }

  def generateNumberingInfo(numberingSeriesId: NumberingSeriesId, nameOpt: Option[String], issueDate: DateTime)(implicit identity: Identity): Future[Either[ErrorWrapper, NumberingInfo]] = {
    numberingSeriesRepository.find(numberingSeriesId).flatMap {
      case Some(ns) if ns.kind == NumberingSeriesKind.Custom =>
        nameOpt match {
          case Some(name) =>
            val ni = NumberingInfo(
              numberingSeries = ns,
              month = None,
              year = None,
              idx = None,
              name = name)
            Future.successful(Right(ni))
          case None =>
            Future.successful(Left(ErrorWrapper("missingNumber", "Wymagana nazwa faktury")))
        }
      case Some(ns) if ns.kind == NumberingSeriesKind.Monthly =>
        val month = "%02d".format(issueDate.monthOfYear.get)
        val year = issueDate.year.get.toString
        val idxFut = getCounterAndIncrement(numberingSeriesId, Some(month), Some(year))
        idxFut.map { idx =>
          val ni = NumberingInfo(
            numberingSeries = ns,
            month = Some(month),
            year = Some(year),
            idx = Some(idx.toInt),
            name = ns.schema.replaceAll("""\{MM\}""", month).replaceAll("""\{RRRR\}""", year).replaceAll("""\{number\}""", idx.toString))
          Right(ni)
        }
      case Some(ns) if ns.kind == NumberingSeriesKind.Yearly =>
        val year = issueDate.year.get.toString
        val idxFut = getCounterAndIncrement(numberingSeriesId, None, Some(year))
        idxFut.map { idx =>
          val ni = NumberingInfo(
            numberingSeries = ns,
            month = None,
            year = Some(year),
            idx = Some(idx.toInt),
            name = ns.schema.replaceAll("""\{RRRR\}""", year).replaceAll("""\{number\}""", idx.toString))
          Right(ni)
        }
      case None =>
        Future.successful(Left(ErrorWrapper("invalidNumberingSeriesId", "Błąd obsługi żądania")))
    }
  }

  private def getCounterAndIncrement(numberingSeriesId: NumberingSeriesId, month: Option[String], year: Option[String]): Future[Long] = Future.successful {
    val key = s"numbering-counter::${numberingSeriesId.id}:$month:$year"
    sedisPool.withJedisClient(client => client.incr(key))
  }

  def initForCompany()(implicit companyId: CompanyId): Future[Seq[NumberingSeries]] = Future.sequence {
    seq.map { ns =>
      numberingSeriesRepository.insert(ns).map {
        NumberingSeries.fromEntity(_)
      }
    }
  }
  private def seq()(implicit companyId: CompanyId) = Seq(yearlyDefault, monthlyDefault, custom)

  private def monthlyDefault()(implicit companyId: CompanyId) = NumberingSeries(
    id = NumberingSeriesId.random,
    owner = companyId,
    name = "Podstawowa miesięczna",
    schema = "{number}/{MM}/{RRRR}",
    kind = NumberingSeriesKind.Monthly,
    primary = false,
    invoiceKind = InvoiceKind.FakturaVat)

  private def yearlyDefault()(implicit companyId: CompanyId) = NumberingSeries(
    id = NumberingSeriesId.random,
    owner = companyId,
    name = "Roczna",
    schema = "{number}/{RRRR}",
    kind = NumberingSeriesKind.Yearly,
    primary = true,
    invoiceKind = InvoiceKind.FakturaVat)

  private def custom()(implicit companyId: CompanyId) = NumberingSeries(
    id = NumberingSeriesId.random,
    owner = companyId,
    name = "Własna",
    schema = "",
    kind = NumberingSeriesKind.Custom,
    primary = false,
    invoiceKind = InvoiceKind.FakturaVat)

}
