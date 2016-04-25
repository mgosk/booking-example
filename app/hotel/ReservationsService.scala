package hotel

import javax.inject.Inject

import com.google.inject.Singleton
import core.ErrorWrapper
import customer.CustomerRepository
import hotel.model.Reservation

import scala.concurrent.{ ExecutionContext, Future }

@Singleton
class ReservationsService @Inject() (reservationsRepository: ReservationsRepository, customerRepository: CustomerRepository, hotelRepository: HotelRepository)(implicit executionContext: ExecutionContext) {

  def create(reservation: Reservation): Future[Either[ErrorWrapper, Reservation]] = {
    customerRepository.find(reservation.login).flatMap {
      case Some(user) =>
        hotelRepository.findRoom(reservation.hotelId, reservation.roomNr).flatMap {
          case Some(room) =>
            reservationsRepository.checkForConflicts(reservation.hotelId, reservation.roomNr, reservation.dateFrom, reservation.dateTo).flatMap {
              case None =>
                reservationsRepository.create(reservation).map { x =>
                  Right(reservation)
                }
              case Some(res) =>
                Future.successful(Left(ErrorWrapper("alreadyReserved", "Requested room is already reserved")))
            }
          case None =>
            Future.successful(Left(ErrorWrapper("invalidRoom", "Requested room don't exist")))
        }
      case None =>
        Future.successful(Left(ErrorWrapper("invalidLogin", "Provided user don't exist")))
    }
  }

  def getReservationsForUser(login: String): Future[Seq[Reservation]] = {
    reservationsRepository.getForUser(login)
  }

}
