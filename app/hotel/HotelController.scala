package hotel

import javax.inject.{Singleton, Inject}
import core.ErrorWrapper
import hotel.model.{Room, HotelId}
import hotel.protocol.CreateHotelRequest
import play.api.libs.json.{Json, JsError, JsSuccess}
import play.api.mvc.{Action, Controller}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class HotelController @Inject()(hotelRepository: HotelRepository)(implicit ec: ExecutionContext) extends Controller {

  def create() = Action.async(parse.json) { implicit request =>
    request.body.validate[CreateHotelRequest] match {
      case s: JsSuccess[CreateHotelRequest] =>
        hotelRepository.create(s.get).map { hotel =>
          Ok(Json.toJson(hotel))
        }
      case e: JsError =>
        Future.successful(BadRequest(Json.toJson(ErrorWrapper.invalidJson(e))))
    }
  }

  def get(id: HotelId) = Action.async { implicit request =>
    hotelRepository.get(id).map {
      case Some(hotel) => Ok(Json.toJson(hotel))
      case None => BadRequest(Json.toJson(ErrorWrapper.notFound("Hotel not found")))
    }
  }


  def search(city: String, minPrice: Long, maxPrice: Long) = Action.async { implicit request =>
    Future.successful(Ok("asd"))
  }

  def addRoom(id: HotelId) = Action.async(parse.json) { implicit request =>
    request.body.validate[Room] match {
      case s: JsSuccess[Room] =>
        hotelRepository.get(id).flatMap {
          case Some(hotel) =>
            hotel.rooms.find(room => room.number == s.get.number) match {
              case None =>
                val withNewRoom = hotel.copy(rooms = (hotel.rooms :+ s.get))
                hotelRepository.update(withNewRoom).map {
                  case Some(updated) => Ok(Json.toJson(withNewRoom))
                  // None is impossible here
                }
              case Some(x) =>
                Future.successful(BadRequest(Json.toJson(ErrorWrapper("alreadyExist", s"Room nr ${s.get.number} already exist"))))
            }
          case None => Future.successful(BadRequest(Json.toJson(ErrorWrapper("notFound", "Hotel not found"))))
        }
      case e: JsError =>
        Future.successful(BadRequest(Json.toJson(ErrorWrapper.invalidJson(e))))
    }
  }

  def removeRoom(id: HotelId, roomNr: Int) = Action.async { implicit request =>
    hotelRepository.get(id).flatMap {
      case Some(hotel) =>
        hotel.rooms.find(_.number == roomNr) match {
          case None =>
            Future.successful(BadRequest(Json.toJson(ErrorWrapper("notFound", s"Room nr ${roomNr} not exist"))))
          case Some(roomToDelete) =>
            val withoutRoom = hotel.copy(rooms = (hotel.rooms.filter(_.number != roomNr)))
            hotelRepository.update(withoutRoom).map {
              case Some(updated) => Ok(Json.toJson(withoutRoom))
              // None is impossible here
            }
        }
      case None => Future.successful(BadRequest(Json.toJson(ErrorWrapper("notFound", "Hotel not found"))))
    }
  }

  def makeReservation() = Action.async { implicit request =>
    Future.successful(Ok("asd"))
  }

}
