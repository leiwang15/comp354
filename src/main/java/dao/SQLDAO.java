package dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public abstract class SQLDAO {
	private DataSource ds;

	public SQLDAO(DataSource ds) {
		this.ds = ds;
	}

	public Connection getConnection() throws SQLException {
		return ds.getConnection();
	}
}
