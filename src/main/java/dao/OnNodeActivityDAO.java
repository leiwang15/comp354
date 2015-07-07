package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

import javax.sql.DataSource;

import model.OnNodeActivity;
import model.PERTActivity;
import model.Project;

public class OnNodeActivityDAO extends SQLDAO {

	private ActivityDAO activityDAO;

	public OnNodeActivityDAO(DataSource ds, ActivityDAO activityDAO) {
		super(ds);
		this.activityDAO = activityDAO;
	}
	
	public Collection<OnNodeActivity> getPERTActivities(Project p) throws SQLException {
		final String sql = "SELECT t.ActivityID, Name, Desc, Duration FROM Activity t LEFT JOIN OnNodeActivity t2 USING (activityID)";
		try (Connection c = getConnection(); PreparedStatement st = c.prepareStatement(sql)) {
			st.setInt(1, p.projectID);
			final Collection<OnNodeActivity> a = new LinkedList<>();
			try (ResultSet rs = st.executeQuery()) {
				while (rs.next()) {
					final int activityID = rs.getInt(1);
					final String name = rs.getString(2);
					final String description = rs.getString(3);
					final int duration = rs.getInt(4);
					a.add(new OnNodeActivity(activityID, name, description, duration));
				}
				return a;
			}
		}
	}
	
	public Collection<OnNodeActivity> getPredecessors(PERTActivity a) throws SQLException {
		final String sql = ""
				+ "SELECT ActivityID, Name, Desc, Pessimistic, Optimistic, Estimated "
				+ "FROM ActivityPredecessor t "
				+ "JOIN PERTActivity t2 ON t.PredID=t2.ActivityID"
				+ "WHERE t.ActivityID=?";
		try (Connection c = getConnection(); PreparedStatement st = c.prepareStatement(sql)) {
			st.setInt(1, a.activityID);
			try (ResultSet rs = st.executeQuery()) {
				final Collection<OnNodeActivity> list = new LinkedList<>();
				while (rs.next()) {
					final int activityID = rs.getInt(1);
					final String name = rs.getString(2);
					final String description = rs.getString(3);
					final int duration = rs.getInt(4);
					list.add(new OnNodeActivity(activityID, name, description, duration));
				}
				return list;
			}
		}
	}
	
	public void update(OnNodeActivity a) throws SQLException {
		final String sql = "UPDATE OnNodeActivity SET duration=? WHERE ActivityID=?";
		try (Connection c = getConnection(); PreparedStatement st = c.prepareStatement(sql)) {
			st.setInt(1,  a.duration);
			st.setInt(2,  a.activityID);
			st.executeUpdate();
			activityDAO.update(a);
		}
	}
}
