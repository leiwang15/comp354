package edu.concordia.comp354.model2.controller;

import edu.concordia.comp354.model2.dao.PERTActivityDAO;
import edu.concordia.comp354.model2.model.PERTActivity;
import edu.concordia.comp354.model2.model.Project;

import java.sql.SQLException;
import java.util.Collection;

public class PERTController {

	public final PERTActivityDAO pertDAO;

	public PERTController(PERTActivityDAO pertDAO) {
		this.pertDAO = pertDAO;
	}

	public Collection<PERTActivity> getPERTActivities(Project p) throws SQLException {
		return pertDAO.getPERTActivities(p);
	}
}
