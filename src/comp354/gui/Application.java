package comp354.gui;

import comp354.Controller.UserController;
import comp354.Model.User;

import javax.swing.*;

/**
 * Created by joao on 15-06-02.
 */
public class Application {

    public static void main(String[] args) {
        UserController uc = new UserController();
        MainWindow.currentUser = uc.getUserByUserName("a");
        MainWindow mainWindow = new MainWindow();
        mainWindow.frmProjectManagementSystem.setVisible(true);
    }
}
