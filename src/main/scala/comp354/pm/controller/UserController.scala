package comp354.pm.controller

case object EmptyField extends Exception
case class DifferentField(str1: String, str2: String) extends Exception
case object BadUser extends Exception

import scala.concurrent.ExecutionContext
import comp354.pm.model._, Tables._
import scalaz._, Scalaz._

object UserController {

  trait FieldException extends Exception

  def login(u: String, p: String)(implicit ec: ExecutionContext) =
    UserDAO.getUser(u, p).map { _.toSuccess(BadUser) }

  def register(u: UserRow) = UserDAO.add(u)

  def getRoles = UserDAO.getRoles

  def validate(u: String, p: String, p2: String) =
    (nonEmpty(p) |@| nonEmpty(p2) |@| matching(p, p2)) { (_, _, _) => p }

  def validateAndRegister(u: UserRow, cpwd: String)(implicit ec: ExecutionContext) =
    for {
      _ <- validate(u.username, u.password, cpwd)
      us <- register(u).success
    } yield us

  def nonEmpty(str: String) =
    str.nonEmpty ? str.successNel[Throwable] | EmptyField.failureNel[String]

  def matching(str1: String, str2: String) =
    (str1 == str2) ? str1.successNel[Throwable] | DifferentField(str1, str2).failureNel[String]
}