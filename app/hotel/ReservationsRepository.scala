package hotel

import hotel.model.Reservation
import scala.concurrent.Future

class ReservationsRepository {

  var set = scala.collection.mutable.Set[Reservation]()

  def create(reservation: Reservation): Future[Boolean] = Future.successful {
    set add reservation
  }

  def getForUser(login: String): Future[Seq[Reservation]] = Future.successful {
    set.filter(_.login == login).toSeq
  }

}
