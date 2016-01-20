package controllers

import javax.inject.Inject

import infrastracture.UserDao
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._

class Application @Inject()(val messagesApi: MessagesApi) extends Controller with I18nSupport {

  val userDao = new UserDao()

  def index = Action {
    val users = userDao.findAll()
    Ok(views.html.index(users))
  }

}
