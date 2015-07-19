package edu.concordia.comp354.model2.controller;

import edu.concordia.comp354.model2.dao.ProjectDAO;
import edu.concordia.comp354.model2.model.Project;
import edu.concordia.comp354.model2.model.User;

import java.sql.SQLException;
import java.util.Collection;

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
