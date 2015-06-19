package comp354.pm.view

import scala.swing._
import event._
import comp354.pm.controller.UserController
import scala.concurrent.ExecutionContext.Implicits._
import scalaz._

class LoginFrame extends Frame {
  title = "Login"
  peer.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE)
  contents = new FlowPanel {
    val un = new TextField(10)
    val pw = new PasswordField(10)
    contents += new Label("Username:")
    contents += un
    contents += new Label("Password:")
    contents += pw
    contents += new Button(Action("Login") {
      for {
        validation <- UserController.login(un.text, pw.password.mkString)
        user <- validation.leftMap { err =>
          Dialog.showMessage(this, s"There Was an Error!: $err", "Login Error", Dialog.Message.Error)
          un.requestFocus()
        }
      } Swing.onEDT {
        Dialog.showMessage(this, s"Yoou may login dawg!: $user")
        new MainFrame(user).visible = true
        dispose()
      }
    })
    contents += new Button(Action("Register") {
      new RegisterFrame().visible = true; dispose()
    })
    border = Swing.EmptyBorder(5, 5, 5, 5)
    preferredSize = new Dimension(250, 100)
    resizable = false
  }
}