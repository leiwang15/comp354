package comp354;

import java.io.IOException;
import java.nio.file.*;
import java.sql.*;



public class Driver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Connection c = null;
		Statement stmt = null;
		try {
			
			try {
			    Files.delete(Paths.get("test.db"));
			} catch (NoSuchFileException x) {
			    System.err.format("%s: no such" + " file or directory%n");
			} catch (DirectoryNotEmptyException x) {
			    System.err.format("%s not empty%n");
			} catch (IOException x) {
			    // File permission problems are caught here.
			    System.err.println(x);
			}
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			System.out.println("Opened database successfully");

			//Create table User
			stmt = c.createStatement();
			String sql = 
					"CREATE TABLE User "
					+ "(UserID		INTEGER 	PRIMARY KEY     AUTOINCREMENT,"
					+ " FirstName	TEXT    NOT NULL, "
					+ " LastName	TEXT	NOT NULL, "
					+ " Role		TEXT	NOT NULL )";
			stmt.executeUpdate(sql);
			stmt.close();
			
			//Create table Project
			stmt = c.createStatement();
			sql = 
					"CREATE TABLE Project "
					+ "(ProjectID	INTEGER 	PRIMARY KEY     AUTOINCREMENT,"
					+ " Name		TEXT    NOT NULL, "
					+ " Desc		TEXT	NOT NULL, "
					+ " StartDate	DATE	NOT NULL, "
					+ " EndDate		DATE	NOT NULL, "
					+ " Finished	INT		DEFAULT 0)";
			stmt.executeUpdate(sql);
			
			//Create table Activity
			stmt = c.createStatement();
			sql = 
					"CREATE TABLE Activity "
					+ "(ActivityID	INTEGER 	PRIMARY KEY     AUTOINCREMENT,"
					+ " Name		TEXT    NOT NULL, "
					+ " Desc		TEXT	NOT NULL, "
					+ " Duration	INT		NOT NULL, "
					+ " Finished	INT		DEFAULT 0)";
			
			
			stmt.executeUpdate(sql);
			
			//Create table Project_Assign
			stmt = c.createStatement();
			sql = 
					"CREATE TABLE Project_Assign ("
					+ "	PA_ID		INTEGER 	PRIMARY KEY     AUTOINCREMENT,"
					+ " Project_ID	INTEGER		NOT NULL, "
					+ " User_ID		INTEGER		NOT NULL, "
					+ " FOREIGN KEY(Project_ID) 		REFERENCES Project(ProjectID),"
					+ " FOREIGN KEY(User_ID) 			REFERENCES User(UserID))";
			
			
			stmt.executeUpdate(sql);
			
			
			//Create table Activity_Assign
			stmt = c.createStatement();
			sql = 
					"CREATE TABLE Activity_Assign ("
					+ "	AA_ID		INTEGER 	PRIMARY KEY     AUTOINCREMENT,"
					+ " Activity_ID	INTEGER		NOT NULL, "
					+ " User_ID		INTEGER		NOT NULL, "
					+ " FOREIGN KEY(Activity_ID) 	REFERENCES Activity(ActivityID),"
					+ " FOREIGN KEY(User_ID) 		REFERENCES User(UserID))";
			
	
			stmt.executeUpdate(sql);
			
			
			//Create table Project_Activity
			stmt = c.createStatement();
			sql = 
					"CREATE TABLE Project_Activity ("
					+ "	PA_ID			INTEGER 	PRIMARY KEY     AUTOINCREMENT,"
					+ " Project_ID		INTEGER		NOT NULL, "
					+ " Activity_ID		INTEGER		NOT NULL, "
					+ " FOREIGN KEY(Project_ID) 	REFERENCES Project(ProjectID),"
					+ " FOREIGN KEY(Activity_ID) 	REFERENCES Activity(ActivityID))";
			
	
			stmt.executeUpdate(sql);
			
			//Create table Activity_Prereq
			stmt = c.createStatement();
			sql = 
					"CREATE TABLE Activity_Prereq ("
					+ "	AP_ID				INTEGER 	PRIMARY KEY     AUTOINCREMENT,"
					+ " Activity_ID1		INTEGER		NOT NULL, "
					+ " Activity_ID2		INTEGER		NOT NULL, "
					+ " FOREIGN KEY(Activity_ID1) 	REFERENCES Activity(ActivityID),"
					+ " FOREIGN KEY(Activity_ID2) 	REFERENCES Activity(ActivityID))";
			
	
			stmt.executeUpdate(sql);
			
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Table created successfully");

	}
}
