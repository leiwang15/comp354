package edu.concordia.comp354.model2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import edu.concordia.comp354.model2.model.Activity;
import edu.concordia.comp354.model2.model.User;

public class ActivityDAO extends SQLDAO {

	public ActivityDAO(DataSource ds) {
		super(ds);
	}

	public void update(Activity a) throws SQLException {
		final String sql = "UPDATE Activity SET Name=?, Desc=? WHERE ActivityID=?";
		try (Connection c = getConnection(); PreparedStatement st = c.prepareStatement(sql)) {
			st.setString(1,  a.name);
			st.setString(2,  a.description);
			st.setInt(3,  a.activityID);
			st.executeUpdate();
		}
	}

	public void assignToUser(Activity a, User u) throws SQLException {
		final String sql = "INSERT INTO UserActivity(UserID, ActivityID) VALUES (?, ?)";
		try (Connection c = getConnection(); PreparedStatement st = c.prepareStatement(sql)) {
			st.setInt(1, u.userID);
			st.setInt(2, a.activityID);
			st.executeUpdate();
		}
	}

	public void removeUser(Activity a, User u) throws SQLException {
		final String sql = "DELETE FROM UserActivity WHERE UserID=? AND ActivityID=?";
		try (Connection c = getConnection(); PreparedStatement st = c.prepareStatement(sql)) {
			st.setInt(1, u.userID);
			st.setInt(2, a.activityID);
			st.executeUpdate();
		}
	}

	public void addPredecessor(Activity a, Activity pred) throws SQLException {
		final String sql = "INSERT INTO ActivityPredecessor(ActivityID, PredID) VALUES (?, ?)";
		try (Connection c = getConnection(); PreparedStatement st = c.prepareStatement(sql)) {
			st.setInt(1, a.activityID);
			st.setInt(2, pred.activityID);
			st.executeUpdate();
		}
	}
}