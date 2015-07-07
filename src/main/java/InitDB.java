import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;

import util.ScriptRunner;

public class InitDB {
	public static void main(String[] args) throws Exception {
		Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/pm.db");
		ScriptRunner runner = new ScriptRunner(connection, false, true);
		runner.runScript(new BufferedReader(new FileReader("src/main/resources/db.q")));
	}
}
