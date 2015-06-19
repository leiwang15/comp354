package comp354.pm.view

import scala.swing._
import comp354.pm.model.Tables._
import comp354.pm.controller.ProjectController
import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent._
import duration._

class MainFrame(user: UserRow) extends Frame {
  title = s"Project Management System: Welcome${user.firstname}: ${user.lastname} "

  val l = new ListView[ProjectRow] {
    listData = Await.result(ProjectController.get(user), Duration.Inf)
  }

  menuBar = new MenuBar {
    contents += new Menu("File") {
      contents += new MenuItem(Action("New Project...") {
        new NewProjectFrame(user).visible = true
      })
      contents += new MenuItem(Action("Save") {
        println("Save")
      })
      contents += new MenuItem(Action("Logout") {
        println("Logout")
      })
      contents += new MenuItem(Action("Exit") {
        println("Exit")
      })
    }
  }

  contents = new FlowPanel {
    contents += info
  }

  lazy val info = new FlowPanel {

  }
}