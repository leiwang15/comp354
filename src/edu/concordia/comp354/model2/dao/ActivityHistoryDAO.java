package edu.concordia.comp354.model2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

import javax.sql.DataSource;

import edu.concordia.comp354.model2.model.Activity;
import edu.concordia.comp354.model2.model.ActivityHistory;

import org.joda.time.DateTime;

public class ActivityHistoryDAO extends SQLDAO {

	public ActivityHistoryDAO(DataSource ds) {
		super(ds);
	}

	public Collection<ActivityHistory> getHistory(Activity a) throws SQLException {
		final String sql = "SELECT UserID, Progress, Comment, Timestamp FROM ActivityProgressHistory WHERE activityID=? ORDER BY timestamp";
		try (Connection c = getConnection(); PreparedStatement st = c.prepareStatement(sql)) {
			st.setInt(1, a.activityID);
			try (ResultSet rs = st.executeQuery()) {
				final Collection<ActivityHistory> items = new LinkedList<>();
				while (rs.next()) {
					final int userID = rs.getInt(1);
					final int progress = rs.getInt(2);
					final String comment = rs.getString(3);
					final DateTime timestamp = new DateTime(rs.getDate(4));
					items.add(new ActivityHistory(userID, progress, comment, timestamp));
				}
				return items;
			}
		}
	}

	public ActivityHistory getLastModification(Activity a) throws SQLException {
		final String sql = "SELECT UserID, Progress, Comment, Timestamp FROM ActivityProgressHistory WHERE activityID=? ORDER BY timestamp DESC LIMIT 1";
		try (Connection c = getConnection(); PreparedStatement st = c.prepareStatement(sql)) {
			st.setInt(1, a.activityID);
			try (ResultSet rs = st.executeQuery()) {
				if (!rs.next()) return null;
				final int userID = rs.getInt(1);
				final int progress = rs.getInt(2);
				final String comment = rs.getString(3);
				final DateTime timestamp = new DateTime(rs.getDate(4));
				return new ActivityHistory(userID, progress, comment, timestamp);
			}
		}
	}

	public void  appendModification(Activity a, ActivityHistory modification) throws SQLException {
		final String sql = "INSERT INTO ActivityProgressHistory(UserID, ActivityID, Progress, Comment) VALUES (?, ?, ?, ?)";
		try (Connection c = getConnection(); PreparedStatement st = c.prepareStatement(sql)) {
			st.setInt(1,  modification.userID);
			st.setInt(2, a.activityID);
			st.setInt(3,  modification.progress);
			st.setString(4,  modification.comment);
			st.executeUpdate();
		}
	}
}