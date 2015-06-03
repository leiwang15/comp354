package pm.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ActivityController {
	private static Connection c = null;
	private static Statement st = null;
	private static String DBpath = "project.db";

	public ActivityController() {
		try {
			File f = new File(DBpath);
			if (f.exists()) {
				c = DriverManager.getConnection("jdbc:sqlite:" + DBpath);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
	
	
	public int addActivity(int project_id, String name, String desc, int duration) {

		int id = 0;

		String sql = "INSERT INTO Activity (Project_ID,Name,Desc,Duration,Progress,Finished) " 
				+ "VALUES("
				+ project_id + ", '" 
				+ name + "', '" 
				+ desc +"', " 
				+ duration + ", " 
				+ 0 + ", "
				+ 0 +");";

		String sql2 = "SELECT last_insert_rowid() FROM Activity;";

		try {
			st = c.createStatement();
			st.executeUpdate(sql);
			st = c.createStatement();
			ResultSet result = st.executeQuery(sql2);
			result.next();
			id = result.getInt(1);
			c.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}
	

}
