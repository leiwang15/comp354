package edu.concordia.comp354.gui;

import edu.concordia.comp354.controller.CreateDB;
import edu.concordia.comp354.controller.UserController;

import java.io.File;

/**
 * Created by joao on 15-06-02.
 */
public class Application {

    public static void main(String[] args) {

        File f = new File("project.db");
        if(!f.exists()){
            CreateDB.initializeDB();
        }

        UserController uc = new UserController();
        MainWindow.currentUser = uc.getUserByUserName("1");
        MainWindow mainDialogWindow = new MainDialogWindow();
        MainWindow mainDirectWindow = new MainDirectWindow();

        MainWindow.frmProjectManagementSystem.setVisible(true);
    }
}
