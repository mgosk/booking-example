package hotel

import java.util.Date

import com.google.inject.Singleton
import hotel.model.{HotelId, Reservation}

import scala.concurrent.Future

// function bodies are wrapped in Future.successful to easy be replaced by reactive database driver
@Singleton
class ReservationsRepository {

  var set = scala.collection.mutable.Set[Reservation]()

  def create(reservation: Reservation): Future[Boolean] = Future.successful {
    set add reservation
    println(set)
    true
  }

  def getForUser(login: String): Future[Seq[Reservation]] = Future.successful {
    set.filter(_.login == login).toSeq
  }

  def checkForConflicts(hotelId: HotelId, roomNr: Int, dateFrom: Date, dateTo: Date): Future[Option[Reservation]] = Future.successful {
    set.find(res => res.hotelId == hotelId && res.roomNr == roomNr && !(dateTo.before(res.dateFrom) || dateFrom.after(res.dateTo)))
  }

}
