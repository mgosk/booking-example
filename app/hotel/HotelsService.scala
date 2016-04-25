package hotel

import com.google.inject.{ Inject, Singleton }
import core.ErrorWrapper
import hotel.model.{ Hotel, HotelId, Room }
import hotel.protocol.RoomsResponse
import play.api.libs.json.Json
import play.api.mvc.Action

import scala.concurrent.{ ExecutionContext, Future }

@Singleton
class HotelsService @Inject() (hotelsRepository: HotelsRepository)(implicit executionContext: ExecutionContext) {

  def create(hotel: Hotel): Future[Hotel] = {
    hotelsRepository.create(hotel)
  }

  def get(id: HotelId): Future[Option[Hotel]] = {
    hotelsRepository.get(id)
  }

  def searchForRoom(city: String, minPrice: Option[Long], maxPrice: Option[Long]): Future[Seq[RoomsResponse]] = {
    hotelsRepository.searchForRooms(city, minPrice, maxPrice).map { pairs =>
      pairs.map {
        case (hid: HotelId, rooms: Seq[Room]) =>
          rooms.map { room => RoomsResponse(hid, room.number, room.price) }
      }.flatten.toSeq
    }
  }

  def addRoom(hotelId: HotelId, room: Room): Future[Either[ErrorWrapper, Hotel]] = {
    hotelsRepository.get(hotelId).flatMap {
      case Some(hotel) =>
        hotel.rooms.find(room => room.number == room.number) match {
          case None =>
            val withNewRoom = hotel.copy(rooms = (hotel.rooms :+ room))
            hotelsRepository.update(withNewRoom).map {
              case Some(updated) => Right(withNewRoom)
              case None => throw new RuntimeException()
            }
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
            val withoutRoom = hotel.copy(rooms = (hotel.rooms.filter(_.number != roomNr)))
            hotelsRepository.update(withoutRoom).map {
              case Some(updated) => Right(withoutRoom)
              case None => throw new RuntimeException()
            }
        }
        Future.successful(Left(ErrorWrapper("notFound", s"Hotel not found")))
    }
  }

}