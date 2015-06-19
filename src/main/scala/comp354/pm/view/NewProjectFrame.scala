package comp354.pm.view

import scala.swing._
import comp354.pm.model.Tables._
import comp354.pm.controller.ProjectController
import scala.concurrent.ExecutionContext.Implicits._
import comp354.gui.DatePicker
import java.sql.Date
import java.text.SimpleDateFormat

class NewProjectFrame(user: UserRow) extends Frame { self =>
  title = s"New Project"
  contents = new FlowPanel {
    val n = new TextField(10)
    val sd = new TextField(10)
    val ed = new TextField(10)
    val de = new TextArea(15, 40) { editable = true}
    contents += new Label("Project Name:")
    contents += n
    contents += new Label("Start Date:")
    contents += sd
    contents += new Button(Action("...") { sd.text = new DatePicker(NewProjectFrame.this.peer).setPickedDate() })
    contents += new Label("End Date:")
    contents += ed
    contents += new Button(Action("...") { ed.text = new DatePicker(NewProjectFrame.this.peer).setPickedDate() })
    contents += new Label("Description")
    contents += de
    contents += new Button(Action("Create") {

      val (id, name, desc, start, end, finished) = (None, n.text, de.text, new Date(sdf.parse(sd.text).getTime), new Date(sdf.parse(ed.text).getTime), None)
      val p = ProjectRow(id, name, desc, start, end, finished)
      val f = ProjectController.add(p)
      f.onComplete { x => println(s"YOYO$x") }
    })
    preferredSize = new Dimension(250, 500)
  }
  
  lazy val sdf = new java.text.SimpleDateFormat("yyyy-MM-dd")
}