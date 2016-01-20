package infrastracture

import anorm.SqlParser._
import anorm._
import models.User
import play.api.Play.current
import play.api.db._

import scala.util.Try

class UserDao {

  private val simple: RowParser[User] = {
    (long("id") ~ str("name") ~ str("mail")).map {
      case id ~ name ~ mail =>
        User(id = Option(id), name = name, mail = mail)
    }
  }

  def findAll(): Try[Seq[User]] = Try {
    DB.withConnection() { implicit conn =>
      SQL("SELECT * FROM user").as(simple *)
    }
  }

  def findById(id: Long): Try[Option[User]] = Try {
    DB.withConnection() { implicit conn =>
      SQL("SELECT * FROM user WHERE id = {id}").on('id -> id).as(simple.singleOpt)
    }
  }

  def insert(user: User): Try[Int] = Try {
    DB.withConnection() { implicit conn =>
      SQL("INSERT INTO user(name, mail) values({name}, {mail})")
        .on('name -> user.name, 'mail -> user.mail)
        .executeUpdate()
    }
  }

  def update(user: User): Try[Int] = Try {
    DB.withConnection() { implicit conn =>
      SQL("UPDATE user SET name = {name}, mail = {mail} WHERE id = {id}")
        .on('id -> user.id.get, 'name -> user.name, 'mail -> user.mail)
        .executeUpdate()
    }
  }

  def delete(id: Long): Try[Int] = Try {
    DB.withConnection() { implicit conn =>
      SQL("DELETE FROM user WHERE id = {id}").on('id -> id).executeUpdate()
    }
  }

}
