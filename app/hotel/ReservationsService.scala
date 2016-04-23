package hotel

import javax.inject.Inject

import com.google.inject.Singleton
import core.ErrorWrapper
import hotel.model.Reservation

import scala.concurrent.{ ExecutionContext, Future }

@Singleton
class ReservationsService @Inject() (reservationsRepository: ReservationsRepository)(implicit executionContext: ExecutionContext) {

  def create(reservation: Reservation): Future[Either[ErrorWrapper, Reservation]] = {
    reservationsRepository.create(reservation).map { x =>
      Right(reservation)
    }
  }

  def getForUser(login: String): Future[Seq[Reservation]] = {
    reservationsRepository.getForUser(login)
  }

}
