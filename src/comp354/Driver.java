package comp354;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Driver {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            System.out.println("Opened database successfully");

            //Create table User
            stmt = c.createStatement();
            String sql =
                    "CREATE TABLE User "
                            + "(UserID		INT 	PRIMARY KEY     NOT NULL,"
                            + " FirstName	TEXT    NOT NULL, "
                            + " LastName	TEXT	NOT NULL, "
                            + " Role		TEXT	NOT NULL )";
            stmt.executeUpdate(sql);
            stmt.close();

            //Create table Project
            stmt = c.createStatement();
            sql =
                    "CREATE TABLE Project "
                            + "(ProjectID	INT 	PRIMARY KEY     NOT NULL,"
                            + " Name		TEXT    NOT NULL, "
                            + " Desc		TEXT	NOT NULL, "
                            + " StartDate	DATE	NOT NULL, "
                            + " EndDate		DATE	NOT NULL )";
            stmt.executeUpdate(sql);

            //Create table Activity
            stmt = c.createStatement();
            sql =
                    "CREATE TABLE Activity "
                            + "(ActivityID	INT 	PRIMARY KEY     NOT NULL,"
                            + " Name		TEXT    NOT NULL, "
                            + " Desc		TEXT	NOT NULL, "
                            + " Duration	INT		NOT NULL )";


            stmt.executeUpdate(sql);

            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Table created successfully");

    }
}
