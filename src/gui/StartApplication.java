package gui;

import java.awt.EventQueue;
import java.io.File;

import Controller.CreateDB;

public class StartApplication {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				File f = new File("project.db");
				if(!f.exists()){
					CreateDB.initializeDB();
				}
				
				try {
					Login loginWindow = new Login();
					loginWindow.login.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
