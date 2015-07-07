package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

import javax.sql.DataSource;

import model.PERTActivity;
import model.Project;

public class PERTActivityDAO extends SQLDAO {

	private ActivityDAO activityDAO;

	public PERTActivityDAO(DataSource ds, ActivityDAO activityDAO) {
		super(ds);
		this.activityDAO = activityDAO;
	}
	
	public Collection<PERTActivity> getPERTActivities(Project p) throws SQLException {
		final String sql = "SELECT t.ActivityID, Name, Desc, Pessimistic, Optimistic, Estimated FROM Activity t LEFT JOIN PERTActivity t2 USING (activityID)";
		try (Connection c = getConnection(); PreparedStatement st = c.prepareStatement(sql)) {
			st.setInt(1, p.projectID);
			final Collection<PERTActivity> a = new LinkedList<>();
			try (ResultSet rs = st.executeQuery()) {
				while (rs.next()) {
					final int activityID = rs.getInt(1);
					final String name = rs.getString(2);
					final String description = rs.getString(3);
					final int pessimistic = rs.getInt(4);
					final int optimistic = rs.getInt(5);
					final int estimated = rs.getInt(6);
					a.add(new PERTActivity(activityID, name, description, pessimistic, optimistic, estimated));
				}
				return a;
			}
		}
	}
	
	public Collection<PERTActivity> getPredecessors(PERTActivity a) throws SQLException {
		final String sql = ""
				+ "SELECT ActivityID, Name, Desc, Pessimistic, Optimistic, Estimated "
				+ "FROM ActivityPredecessor t "
				+ "JOIN PERTActivity t2 ON t.PredID=t2.ActivityID"
				+ "WHERE t.ActivityID=?";
		try (Connection c = getConnection(); PreparedStatement st = c.prepareStatement(sql)) {
			st.setInt(1, a.activityID);
			try (ResultSet rs = st.executeQuery()) {
				final Collection<PERTActivity> list = new LinkedList<>();
				while (rs.next()) {
					final int activityID = rs.getInt(1);
					final String name = rs.getString(2);
					final String description = rs.getString(3);
					final int pessimistic = rs.getInt(4);
					final int optimistic = rs.getInt(5);
					final int estimated = rs.getInt(6);
					list.add(new PERTActivity(activityID, name, description, pessimistic, optimistic, estimated));
				}
				return list;
			}
		}
	}
	
	public void update(PERTActivity a) throws SQLException {
		final String sql = "UPDATE PERTActivity SET Pessimistic=?, Optimistic=?, Estimated=? WHERE ActivityID=?";
		try (Connection c = getConnection(); PreparedStatement st = c.prepareStatement(sql)) {
			st.setInt(1,  a.pessimistic);
			st.setInt(2,  a.optimistic);
			st.setInt(3,  a.estimated);
			st.setInt(4,  a.activityID);
			st.executeUpdate();
			activityDAO.update(a);
		}
	}
}
