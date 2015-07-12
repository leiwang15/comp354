package edu.concordia.comp354.model2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;

import edu.concordia.comp354.model2.util.ScriptRunner;

public class InitDB {
	public static void main(String[] args) throws Exception {
		Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/pm.db");
		ScriptRunner runner = new ScriptRunner(connection, false, true);
		runner.runScript(new BufferedReader(new FileReader("src/resources/db.q")));
	}
}
