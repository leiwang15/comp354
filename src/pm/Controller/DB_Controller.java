package pm.Controller;

import java.io.File;
import java.sql.*;


public class DB_Controller {
	protected static Connection c = null;
	protected static Statement st = null;
	protected static String DBpath = "project.db";

	public DB_Controller() {
		try {
			File f = new File(DBpath);
			if (f.exists()) {
				c = DriverManager.getConnection("jdbc:sqlite:" + DBpath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
