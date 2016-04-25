package hotel

import java.util.Date

import com.google.inject.{ Inject, Singleton }
import core.ErrorWrapper
import hotel.model.{ Hotel, HotelId, Room }
import hotel.protocol.RoomsResponse

import scala.concurrent.{ ExecutionContext, Future }
import scala.util.{ Failure, Success, Try }

@Singleton
class HotelsService @Inject() (hotelsRepository: HotelsRepository, reservationsRepository: ReservationsRepository)(implicit executionContext: ExecutionContext) {

  def create(hotel: Hotel): Future[Hotel] = {
    hotelsRepository.create(hotel)
  }

  def get(id: HotelId): Future[Option[Hotel]] = {
    hotelsRepository.get(id)
  }

  def searchForRoom(city: String, minPrice: Option[Long], maxPrice: Option[Long], dateFrom: Date, dateTo: Date): Future[Seq[RoomsResponse]] = {
    hotelsRepository.searchForRooms(city, minPrice, maxPrice).flatMap { pairs =>
      val rooms = pairs.map {
        case (hid: HotelId, rooms: Seq[Room]) =>
          rooms.map { room => RoomsResponse(hid, room.number, room.price) }
      }.flatten.toSeq
      val filtered = rooms.map { room =>
        reservationsRepository.checkForConflicts(room.hotelId, room.roomNr, dateFrom, dateTo).map {
          case Some(res) => None
          case None => Some(room)
        }
      }
      Future.sequence(filtered).map { _.flatten }
    }
  }

  def addRoom(hotelId: HotelId, room: Room): Future[Either[ErrorWrapper, Hotel]] = {
    hotelsRepository.get(hotelId).flatMap {
      case Some(hotel) =>
        hotel.rooms.find(room => room.number == room.number) match {
          case None =>
            val withNewRoom = hotel.copy(rooms = (hotel.rooms :+ room))
            hotelsRepository.upsert(withNewRoom).map(Right(_))
          case Some(x) =>
            Future.successful(Left(ErrorWrapper("alreadyExist", s"Room nr ${room.number} already exist")))
        }
      case None => Future.successful(Left(ErrorWrapper("notFound", "Hotel not found")))
    }
  }

  def removeRoom(id: HotelId, roomNr: Int): Future[Either[ErrorWrapper, Hotel]] = {
    hotelsRepository.get(id).flatMap {
      case Some(hotel) =>
        hotel.rooms.find(_.number == roomNr) match {
          case None =>
            Future.successful(Left(ErrorWrapper("notFound", s"Room nr ${roomNr} not exist")))
          case Some(roomToDelete) =>
            reservationsRepository.isReservationExist(id, roomNr).flatMap {
              case false =>
                val withoutRoom = hotel.copy(rooms = (hotel.rooms.filter(_.number != roomNr)))
                hotelsRepository.upsert(withoutRoom).map(Right(_))
              case true =>
                Future.successful(Left(ErrorWrapper("reservationsExist", s"Can't delete room. Room have active reservations")))
            }
        }
      case None =>
        Future.successful(Left(ErrorWrapper("notFound", s"Hotel not found")))
    }
  }

}
