package edu.concordia.comp354.gui;

import edu.concordia.comp354.controller.CreateDB;
import edu.concordia.comp354.controller.UserController;
import edu.concordia.comp354.model.ProjectManager;

import java.awt.*;
import java.io.File;

/**
 * Created by joao on 15-06-02.
 */
public class Application {

    public static void main(String[] args) {

        ProjectManager projectManager = new ProjectManager();

        File f = new File("project.db");
        if (!f.exists()) {
            CreateDB.initializeDB();
            projectManager.createTestUsers();
            try {

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        projectManager.initialize();
        MainWindow.currentUser = new UserController().getUserByUserName("John");
        projectManager.setCurrentUser(MainWindow.currentUser);

        new MainDirectWindow(projectManager);

        if (true) {
            MainWindow.frmProjectManagementSystem.setVisible(true);
        } else {
            EventQueue.invokeLater(new Runnable() {
                public void run() {

                    try {
                        Login loginWindow = new Login(projectManager);
                        loginWindow.login.setVisible(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
