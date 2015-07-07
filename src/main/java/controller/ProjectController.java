package controller;

import java.sql.SQLException;
import java.util.Collection;

import dao.ProjectDAO;
import model.Project;
import model.User;

public class ProjectController {

	private ProjectDAO projectDAO;

	public ProjectController(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}
	
	public Project add(Project p) throws SQLException {
		return projectDAO.add(p);
	}
	
	public void delete(Project p) throws SQLException {
		projectDAO.delete(p);
	}
	
	public Collection<Project> get(User u) throws SQLException {
		return projectDAO.get(u);
	}
	
	public void update(Project p) throws SQLException {
		projectDAO.update(p);
	}
}
