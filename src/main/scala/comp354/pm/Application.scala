package comp354.pm

import swing._
import comp354.pm.view.LoginFrame

object Application extends SimpleSwingApplication {
  def top = new LoginFrame
}