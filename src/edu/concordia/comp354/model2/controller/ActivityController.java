package edu.concordia.comp354.model2.controller;

import edu.concordia.comp354.model2.dao.ActivityDAO;
import edu.concordia.comp354.model2.dao.ActivityHistoryDAO;
import edu.concordia.comp354.model2.model.Activity;
import edu.concordia.comp354.model2.model.ActivityHistory;
import edu.concordia.comp354.model2.model.User;

import java.sql.SQLException;
import java.util.Collection;

public class ActivityController {

	private final ActivityDAO activityDAO;
	private final ActivityHistoryDAO activityHistoryDAO;

	public ActivityController(ActivityDAO activityDAO, ActivityHistoryDAO activityHistoryDAO) {
		this.activityDAO = activityDAO;
		this.activityHistoryDAO = activityHistoryDAO;
	}

	public void update(Activity a, User u) throws SQLException {
		if (u.role.equals(User.Role.MANAGER)) {
			activityDAO.update(a);
		}
	}

	public Collection<ActivityHistory> getHistory(Activity a) throws SQLException {
		return activityHistoryDAO.getHistory(a);
	}

	public void assignToUser(Activity a, User u) throws SQLException {
		activityDAO.assignToUser(a, u);
	}

	public void addPredecessor(Activity a, Activity pred) throws SQLException {
		activityDAO.addPredecessor(a, pred);
	}
}
