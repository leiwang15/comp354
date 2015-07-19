package edu.concordia.comp354.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;


public class CreateDB {

	private static String databasePath = "project.db";

	public static void initializeDB() {

        Connection c = null;
        Statement stmt = null;
        try {

//			try {
//				File f = new File(databasePath);
//				if(f.exists()){
//					Files.delete(Paths.get(databasePath));
//				}
//
//			} catch (NoSuchFileException x) {
//			    System.err.format("no such" + " file or directory%n");
//			} catch (DirectoryNotEmptyException x) {
//			    System.err.format("%s not empty%n");
//			} catch (IOException x) {
//			    // File permission problems are caught here.
//			    System.err.println(x);
//			}
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + databasePath);
            System.out.println("Opened database successfully");

            //Create table User
            stmt = c.createStatement();
            String sql =
                    "CREATE TABLE User "
                            + "(UserID		INTEGER 	PRIMARY KEY     AUTOINCREMENT,"
                            + " FirstName	TEXT    NOT NULL, "
                            + " LastName	TEXT	NOT NULL, "
                            + " Role	    TEXT	NOT NULL, "
					+ " UserName	TEXT	NOT NULL, "
					+ " Password	TEXT	NOT NULL )";
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
                            + " Project_ID	INTEGER	NOT NULL, "
                            + " Name		TEXT    NOT NULL, "
                            + " Desc		TEXT	NOT NULL, "
                            + " Duration	INT		NOT NULL, "
					+ " Pessimistic	INT		NOT NULL, "
					+ " Optimistic	INT		NOT NULL, "
					+ " Value		INT		NOT NULL, "
                            + " Progress	INT		DEFAULT 0, 	"
                            + " Finished	INT		DEFAULT 0,	"
					+ " FOREIGN KEY(Project_ID) 		REFERENCES Project(ProjectID) ON DELETE CASCADE);";


            stmt.executeUpdate(sql);

            //Create table Project_Assign
            stmt = c.createStatement();
            sql =
                    "CREATE TABLE Project_Assign ("
                            + "	PA_ID		INTEGER 	PRIMARY KEY     AUTOINCREMENT,"
                            + " Project_ID	INTEGER		NOT NULL, "
                            + " User_ID		INTEGER		NOT NULL, "
					+ " FOREIGN KEY(Project_ID) 		REFERENCES Project(ProjectID) ON DELETE CASCADE,"
					+ " FOREIGN KEY(User_ID) 			REFERENCES User(UserID) ON DELETE CASCADE)";


            stmt.executeUpdate(sql);


            //Create table Activity_Assign
            stmt = c.createStatement();
            sql =
                    "CREATE TABLE Activity_Assign ("
                            + "	AA_ID		INTEGER 	PRIMARY KEY     AUTOINCREMENT,"
                            + " Activity_ID	INTEGER		NOT NULL, "
                            + " User_ID		INTEGER		NOT NULL, "
					+ " FOREIGN KEY(Activity_ID) 	REFERENCES Activity(ActivityID) ON DELETE CASCADE,"
					+ " FOREIGN KEY(User_ID) 		REFERENCES User(UserID) ON DELETE CASCADE)";


            stmt.executeUpdate(sql);


//			//Create table Project_Activity
//			stmt = c.createStatement();
//			sql =
//					"CREATE TABLE Project_Activity ("
//					+ "	PA_ID			INTEGER 	PRIMARY KEY     AUTOINCREMENT,"
//					+ " Project_ID		INTEGER		NOT NULL, "
//					+ " Activity_ID		INTEGER		NOT NULL, "
//					+ " FOREIGN KEY(Project_ID) 	REFERENCES Project(ProjectID),"
//					+ " FOREIGN KEY(Activity_ID) 	REFERENCES Activity(ActivityID))";
//
//
//			stmt.executeUpdate(sql);

            //Create table Activity_Prereq
            stmt = c.createStatement();
            sql =
                    "CREATE TABLE Activity_Pre ("
                            + "	AP_ID				INTEGER 	PRIMARY KEY     AUTOINCREMENT,"
                            + " Activity_ID1		INTEGER		NOT NULL, "
                            + " Activity_ID2		INTEGER		NOT NULL, "
					+ " FOREIGN KEY(Activity_ID1) 	REFERENCES Activity(ActivityID) ON DELETE CASCADE,"
					+ " FOREIGN KEY(Activity_ID2) 	REFERENCES Activity(ActivityID) ON DELETE CASCADE)";


            stmt.executeUpdate(sql);

            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Table created successfully");

    }
}
