package edu.concordia.comp354.controller;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;


public class DB_Controller {
	protected static Connection c = null;
	protected static Statement st = null;
    protected static String DBpath = "project.db";

    public DB_Controller() {
        open(DBpath);
    }

    public DB_Controller(String dbPath) {
        open(dbPath);
    }

    private void open(String dbPath) {
        try {

            String filename = StringUtils.defaultIfEmpty(dbPath, DBpath);

            File f = new File(DBpath);
            if (f.exists()) {
                c = DriverManager.getConnection("jdbc:sqlite:" + filename);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}