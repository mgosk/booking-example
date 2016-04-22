package documents

import javax.inject.Inject

import auth.services.AuthService
import core.AppController
import core.model.ErrorWrapper
import play.api.libs.json.Json
import play.api.mvc.Action

class DocumentsController @Inject() (override val authService: AuthService, documentsService: DocumentsService) extends AppController {

  def get(code: String) = Action.async { implicit request =>
    documentsService.getRawPdf(code).map {
      case Some(fileWrapper) => Ok(fileWrapper.content).as("application/pdf")
      case None => BadRequest(Json.toJson(ErrorWrapper("notFound", "Dokument nie znaleziony")))
    }
  }

  def getHtml(code: String) = Action.async { implicit request =>
    documentsService.getRawHtml(code).map {
      case Some(html) => Ok(html).as("text/html")
      case None => BadRequest(Json.toJson(ErrorWrapper("notFound", "Dokument nie znaleziony")))
    }
  }

}
