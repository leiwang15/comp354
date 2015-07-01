package comp354.gui;

import comp354.Controller.CreateDB;
import comp354.Controller.UserController;

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
        MainDirectWindow.currentUser = uc.getUserByUserName("1");
        MainDirectWindow mainWindow = new MainDirectWindow();
        mainWindow.frmProjectManagementSystem.setVisible(true);
    }
}
