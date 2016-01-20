package controllers

import javax.inject.Inject

import infrastracture.UserDao
import models.User
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._

import scala.util.{Failure, Success}

class Application @Inject()(val messagesApi: MessagesApi) extends Controller with I18nSupport {

  val userDao = new UserDao()

  val form = Form(
    mapping(
      "id" -> optional(longNumber),
      "name" -> nonEmptyText,
      "mail" -> email
    )(User.apply)(User.unapply)
  )

  def index = Action {
    userDao.findAll() match {
      case Success(users) =>
        Ok(views.html.index(users))
      case Failure(_) =>
        InternalServerError
    }
  }

  def add = Action { implicit request =>
    Ok(views.html.add(form))
  }

  def edit(id: Long) = Action { implicit request =>
    userDao.findById(id) match {
      case Success(userOpt) =>
        userOpt.map { user =>
          Ok(views.html.edit(form.fill(user)))
        }.getOrElse(NotFound)
      case Failure(_) =>
        InternalServerError
    }
  }

  def create = Action { implicit request =>
    form.bindFromRequest.fold(
      errors =>
        BadRequest(views.html.add(errors)),
      user => {
        userDao.insert(user)
        Redirect(routes.Application.index())
      }
    )
  }
}
