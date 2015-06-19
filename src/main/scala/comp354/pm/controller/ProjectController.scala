package comp354.pm.controller

import comp354.pm.model.Tables._
import comp354.pm.model.ProjectDAO

object ProjectController {

  def add(p: ProjectRow) = ProjectDAO.add(p)
  def get(u:UserRow) = ProjectDAO.get(u)
  def get(id: Int) = ProjectDAO.get(id)
}