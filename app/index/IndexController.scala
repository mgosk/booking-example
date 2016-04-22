package index

import javax.inject.Inject

import com.typesafe.config.ConfigFactory
import controllers.WebJarAssets
import init.Initializer
import play.api.Play
import play.api.mvc.{ Action, Controller }

class IndexController @Inject() (initializer: Initializer, webJarAssets: WebJarAssets) extends Controller {
  val conf = ConfigFactory.load()
  val clientId = conf.getString("facebook.appId")

  def getIndex = Action { request =>
    Ok(index.html.index(webJarAssets))
  }

  def getAngularApp = Action { request =>
    Ok(index.js.app(clientId, Play.current.configuration.getString(path = "application.url").get))
  }

  def other(url: String) = Action { request =>
    Ok(index.html.index(webJarAssets))
  }

  def invalidApiCall(url: String) = Action { request =>
    NotFound("")
  }

}
