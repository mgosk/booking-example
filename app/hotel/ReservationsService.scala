package hotel

import javax.inject.Inject

import com.google.inject.Singleton
import core.ErrorWrapper
import customer.CustomerRepository
import hotel.model.Reservation

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ReservationsService @Inject() (reservationsRepository: ReservationsRepository, customerRepository: CustomerRepository)(implicit executionContext: ExecutionContext) {

  def create(reservation: Reservation): Future[Either[ErrorWrapper, Reservation]] = {
    customerRepository.find(reservation.login).flatMap {
      case Some(user) =>
        reservationsRepository.create(reservation).map { x =>
          Right(reservation)
        }
      case None =>
        Future.successful(Left(ErrorWrapper("invalidLogin","Provided user don't exist")))
    }
  }

  def getForUser(login: String): Future[Seq[Reservation]] = {
    reservationsRepository.getForUser(login)
  }

}
