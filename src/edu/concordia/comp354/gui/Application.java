package edu.concordia.comp354.gui;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import edu.concordia.comp354.controller.CreateDB;
import edu.concordia.comp354.controller.UserController;
import edu.concordia.comp354.gui.Gantt.GanttTab;
import edu.concordia.comp354.gui.PERT.PERTBoxShape;
import edu.concordia.comp354.model.PERT.PERTEdge;
import edu.concordia.comp354.gui.PERT.PERTTab;
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
        mxGraphics2DCanvas.putShape(PERTBoxShape.SHAPE_PERTBOX, new PERTBoxShape());
        mxGraphics2DCanvas.putShape(PERTEdge.SHAPE_PERTEDGE, new PERTEdge());

        projectManager.initialize();
        MainWindow.currentUser = new UserController().getUserByUserName("John");
        projectManager.setCurrentUser(MainWindow.currentUser);

        MainWindow mainWindow = new MainWindow(projectManager);

        mainWindow.addTab(new GanttTab(mainWindow));
        mainWindow.addTab(new PERTTab(mainWindow));
//        mainWindow.addTab(new EVATab());
        mainWindow.selectTab(0);

        mainWindow.setEnabled(false);

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
