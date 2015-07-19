package edu.concordia.comp354.gui;

import edu.concordia.comp354.controller.CreateDB;
import edu.concordia.comp354.controller.UserController;
import edu.concordia.comp354.model.ProjectManager;

import java.io.File;

/**
 * Created by joao on 15-06-02.
 */
public class Application {

    public static void main(String[] args) {

        File f = new File("project.db");
        if(!f.exists()){
            CreateDB.initializeDB();
            try {
                Login loginWindow = new Login();
                loginWindow.login.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        UserController uc = new UserController();

        ProjectManager projectManager = new ProjectManager();
        MainWindow.currentUser = uc.getUserByUserName("1");
        projectManager.setCurrentUser(MainWindow.currentUser);

        MainDirectWindow mainDirectWindow = new MainDirectWindow(projectManager);

        MainWindow.frmProjectManagementSystem.setVisible(true);
    }
}
