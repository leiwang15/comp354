package controller;

import java.sql.SQLException;
import java.util.Collection;

import model.PERTActivity;
import model.Project;
import dao.PERTActivityDAO;

public class PERTController {
	
	public final PERTActivityDAO pertDAO;
	
	public PERTController(PERTActivityDAO pertDAO) {
		this.pertDAO = pertDAO;
	}

	public Collection<PERTActivity> getPERTActivities(Project p) throws SQLException {
		return pertDAO.getPERTActivities(p);
	}
}
