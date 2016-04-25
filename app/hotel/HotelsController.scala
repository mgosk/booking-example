package hotel

import javax.inject.{ Singleton, Inject }
import core.ErrorWrapper
import hotel.model.{ Reservation, Room, HotelId }
import hotel.protocol.{ RoomsResponse, CreateHotelRequest }
import play.api.libs.json.{ Json, JsError, JsSuccess }
import play.api.mvc.{ Action, Controller }

import scala.concurrent.{ ExecutionContext, Future }

@Singleton
class HotelsController @Inject() (reservationsService: ReservationsService, hotelsService: HotelsService)(implicit ec: ExecutionContext) extends Controller {

  def create() = Action.async(parse.json) { implicit request =>
    request.body.validate[CreateHotelRequest] match {
      case s: JsSuccess[CreateHotelRequest] =>
        hotelsService.create(s.get).map { hotel =>
          Ok(Json.toJson(hotel))
        }
      case e: JsError =>
        Future.successful(BadRequest(Json.toJson(ErrorWrapper.invalidJson(e))))
    }
  }

  def get(id: HotelId) = Action.async { implicit request =>
    hotelsService.get(id).map {
      case Some(hotel) => Ok(Json.toJson(hotel))
      case None => BadRequest(Json.toJson(ErrorWrapper.notFound("Hotel not found")))
    }
  }

  def search(city: String, minPrice: Option[Long], maxPrice: Option[Long]) = Action.async { implicit request =>
    hotelsService.searchForRoom(city, minPrice, maxPrice).map { seq =>
      Ok(Json.toJson(seq))
    }
  }

  def addRoom(id: HotelId) = Action.async(parse.json) { implicit request =>
    request.body.validate[Room] match {
      case s: JsSuccess[Room] =>
        hotelsService.addRoom(id, s.get).map {
          case Right(e) => Ok(Json.toJson(e))
          case Left(error) => BadRequest(Json.toJson(error))
        }
      case e: JsError =>
        Future.successful(BadRequest(Json.toJson(ErrorWrapper.invalidJson(e))))
    }
  }

  def removeRoom(id: HotelId, roomNr: Int) = Action.async { implicit request =>
    hotelsService.removeRoom(id, roomNr).map {
      case Right(e) => Ok(Json.toJson(e))
      case Left(error) => BadRequest(Json.toJson(error))
    }
  }

  def makeReservation() = Action.async(parse.json) { implicit request =>
    request.body.validate[Reservation] match {
      case s: JsSuccess[Reservation] =>
        reservationsService.create(s.get).map {
          case Right(reservation) => Ok(Json.toJson(reservation))
          case Left(error) => BadRequest(Json.toJson(error))
        }
      case e: JsError =>
        Future.successful(BadRequest(Json.toJson(ErrorWrapper.invalidJson(e))))
    }
  }

}
