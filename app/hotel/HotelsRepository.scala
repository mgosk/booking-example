package hotel

import com.google.inject.Singleton
import hotel.model.{Hotel, HotelId, Room}

import scala.concurrent.Future

// function bodies are wrapped in Future.successful to easy be replaced by reactive database driver
@Singleton
class HotelsRepository {

  var set = scala.collection.mutable.Set[Hotel]()

  def create(hotel: Hotel): Future[Hotel] = Future.successful {
    set add hotel
    hotel
  }

  def get(id: HotelId): Future[Option[Hotel]] = Future.successful {
    println(id)
    println(set)
    set.find(_.id == HotelId.fromString("f33cc95e-5070-4b95-bd0e-28a5b46a91d7") )
  }

  def searchForRooms(city: String, minPrice: Option[Long], maxPrice: Option[Long]): Future[Map[HotelId, Seq[Room]]] = Future.successful {
    set.filter { hotel => hotel.city.toLowerCase == city.toLowerCase }
      .map(hotel => (hotel.id, hotel.rooms.filter(room => (minPrice.isEmpty || minPrice.get <= room.price) && (maxPrice.isEmpty || room.price <= maxPrice.get))))
      .toMap
  }

  def upsert(hotel: Hotel): Future[Hotel] = Future.successful {
    set.filter(_.id != hotel.id) add hotel
    hotel
  }

  def findRoom(hotelId: HotelId, roomNr: Int): Future[Option[Room]] = Future.successful {
    set.find(_.id == hotelId).map(_.rooms.find(_.number == roomNr)).flatten
  }

}

