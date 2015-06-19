package comp354.pm.view

import scala.concurrent._, duration._, ExecutionContext.Implicits._
import scala.swing._, Swing._
import scala.util.Failure
import comp354.pm.model.Tables._

import comp354.pm.controller.UserController

class RegisterFrame extends Frame {
  title = "Register"
  peer.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE)
  contents = new FlowPanel {
    contents += labels
    contents += fields
    contents += new Button(Action("Register") {
      val (id, first, last, role, uname, pword, cpword) = (None, fn.text, ln.text, r.selection.item, un.text, pw.password.mkString, cpw.password.mkString)
      val u = UserRow(id, first, last, role, uname, pword)
      for {
        userFuture <- UserController.validateAndRegister(u, cpword) leftMap { case errs => println(s"fail validating: $errs") }
        user <- userFuture andThen { case Failure(errs) => println(s"fail registering: $errs") }
      } Swing.onEDT {
        new MainFrame(user).visible = true
        dispose()
      }
    })
    contents += new Button(Action("Cancel") {
      new LoginFrame().visible = true; dispose()
    })
    border = EmptyBorder(5, 5, 5, 5)
    preferredSize = new Dimension(300, 400)
    resizable = false

    lazy val (un, ln, fn) = (new TextField(10), new TextField(10), new TextField(10))
    lazy val (pw, cpw) = (new PasswordField(10), new PasswordField(10))
    lazy val r = new ComboBox(Await.result(UserController.getRoles, Duration.Inf))

    lazy val labels = {
      val l = List("Username:", "Last Name:", "First Name:", "Password:", "Confirm Password:", "Role:")
      new GridPanel(l.length, 1) {
        contents ++= l.map(new Label(_) {
          xAlignment = Alignment.Right
        })
        vGap = 20
      }
    }

    lazy val fields = {
      val f = List(un, ln, fn, pw, cpw, r)
      new GridPanel(f.length, 1) {
        contents ++= f
        vGap = 10
      }
    }
  }
}