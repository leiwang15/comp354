package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.LinkedList;

import javax.sql.DataSource;

import model.Project;
import model.User;

public class ProjectDAO extends SQLDAO {

	public ProjectDAO(DataSource ds) {
		super(ds);
	}

	public Project add(Project p) throws SQLException {
		final String sql = "INSERT INTO Project(Name,Desc) VALUES (?, ?)";
		try (Connection c = getConnection(); PreparedStatement st = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			st.setString(1, p.name);
			st.setString(2, p.description);
			st.executeUpdate();
			try (ResultSet rs = st.getGeneratedKeys()) {
				rs.next();
				final int projectID = rs.getInt(1);
				return new Project(projectID, p.name, p.description);
			}
		}
	}
	
	public void assign(Project p, User u) throws SQLException {
		final String sql = "INSERT INTO ProjectUser(ProjectID, UserID) VALUES (?, ?)";
		try (Connection c = getConnection(); PreparedStatement st = c.prepareStatement(sql)) {
			st.setInt(1,  p.projectID);
			st.setInt(2, u.userID);
			st.executeUpdate();
		}
	}

	public void delete(Project p) throws SQLException {
		final String sql = "DELETE FROM Project WHERE ProjectID=?";
		try (Connection c = getConnection(); PreparedStatement st = c.prepareStatement(sql)) {
			st.setInt(1, p.projectID);
			st.executeUpdate();
		}
	}
	
	public Project get(int projectID) throws SQLException {
		final String sql = "SELECT Name, Desc FROM Project WHERE ProjectID=?";
		try (Connection c = getConnection(); PreparedStatement st = c.prepareStatement(sql)) {
			st.setInt(1, projectID);
			try (ResultSet rs = st.executeQuery()) {
				if (!rs.next()) return null;
				final String name = rs.getString(1);
				final String description = rs.getString(2);
				return new Project(projectID, name, description);
			}
		}
	}
	
	public Collection<Project> get(User u) throws SQLException {
		final String sql = "SELECT ProjectID, Name, Desc FROM Project JOIN ProjectUser USING (ProjectID) WHERE UserID=?";
		try (Connection c = getConnection(); PreparedStatement st = c.prepareStatement(sql)) {
			st.setInt(1, u.userID);
			try (ResultSet rs = st.executeQuery()) {
				final Collection<Project> p = new LinkedList<>();
				while (rs.next()) {
					final int projectID = rs.getInt(1);
					final String name = rs.getString(2);
					final String description = rs.getString(3);
					p.add(new Project(projectID, name, description));
				}
				return p;
			}
		}
	}
	
	public void update(Project p) throws SQLException {
		final String sql = "UPDATE Project SET Name=?, Desc=? WHERE ProjectID=?";
		try (Connection c = getConnection(); PreparedStatement st = c.prepareStatement(sql)) {
			st.setString(1,  p.name);
			st.setString(2,  p.description);
			st.setInt(3,  p.projectID);
			st.executeUpdate();
		}
	}
}
