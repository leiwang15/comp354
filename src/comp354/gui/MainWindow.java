package comp354.gui;

import comp354.Model.Project;
import comp354.Model.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Created by joao on 15.07.01.
 */
public class MainWindow {
    protected static User currentUser;
    protected static Project currentProject;
    protected static List<Project> pjList;
    protected JMenuItem mntmNewProject;
    protected JMenuItem mntmLoad;
    protected JMenuItem mntmSave;
    protected JMenuItem mntmLogOut;
    protected JMenuItem mntmExit;
    protected static JFrame frmProjectManagementSystem;
    protected JLabel lblProjectList;
    static JMenuBar menuBar;
    static JTabbedPane tabbedPane;

    protected void initialize() {

        if (frmProjectManagementSystem == null) {
            frmProjectManagementSystem = new JFrame();
            frmProjectManagementSystem.setTitle("Project Management System");
            frmProjectManagementSystem.setBounds(0, 0, 1024, 800);
            frmProjectManagementSystem.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            menuBar = new JMenuBar();
            frmProjectManagementSystem.setJMenuBar(menuBar);
            frmProjectManagementSystem.getContentPane().setLayout(new GridLayout(1, 1));

            tabbedPane = new JTabbedPane();

            //Add the tabbed pane to this panel.
            frmProjectManagementSystem.getContentPane().add(tabbedPane);

            //The following line enables to use scrolling tabs.
            tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        }
    }

    protected Container getParentContainer(String title) {

        JPanel panel = new JPanel();
        tabbedPane.addTab(title, null, panel, null);
//        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        return panel;
    }

    public JFrame getParentJFrame() {
        return frmProjectManagementSystem;
    }

}
