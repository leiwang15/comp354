package edu.concordia.comp354.gui;

import edu.concordia.comp354.controller.ActivityController;
import edu.concordia.comp354.controller.ProjectController;
import edu.concordia.comp354.controller.UserController;
import edu.concordia.comp354.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class MainDirectWindow extends MainWindow  {

    private JMenuItem mntmNewProject;
    private JMenuItem mntmSaveProject;
    private JMenuItem mntmDeleteProject;
    private JMenuItem mntmEditProject;
    private JMenuItem mntmLogOut;
    private JMenuItem mntmExit;
    private JMenuItem mntmDeleteAct;
    private JMenuItem mntmGANTTChart;
    private JMenuItem mntmPERT;
    private JMenuItem mntmCriticalPath;
    private JMenuItem mntmEarnedValue;

    protected JList list;

    private JTable activityTable;
    private ListSelectionModel lsm;
    private static String[] columnNames = {"Activity Name",
            "Duration",
            "Predecessors",
            "Progress",
            "Description"};

    public MainDirectWindow(ProjectManager projectManager) {
        super(projectManager);

        initialize();

        if (currentUser.getRole().equals("Project Manager")) {
//            this.projectManager.loadProjects();
        }
    }

    protected void initialize() {
        super.initialize(new ActivityEntry(this, projectManager));

        initMenus();

        initPanel();
    }

    protected void initMenus() {
        JFrame parentJFrame = getParentJFrame();

        //menu file
        JMenu mnFile = new JMenu("File");
        menuBar.add(mnFile);

        if (currentUser.getRole().equals("Project Manager")) {
            //New project
            mntmNewProject = new JMenuItem("New Project");
            mntmNewProject.setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
            mnFile.add(mntmNewProject);

            mntmNewProject.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            try {
                                CreatePJ window = new CreatePJ(projectManager);
                                window.createPJ.setVisible(true);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });

            //Save project
            mntmSaveProject = new JMenuItem("Save Project");
            mntmSaveProject.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
            mnFile.add(mntmSaveProject);

            mntmSaveProject.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            try {
                                projectManager.saveProject();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });

            mntmEditProject = new JMenuItem("Edit Project");
            mnFile.add(mntmEditProject);

            mntmEditProject.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        EditPJ window = new EditPJ(projectManager);
                        window.editPJ.setVisible(true);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            //Delete
            mntmDeleteProject = new JMenuItem("Delete Project");
            mnFile.add(mntmDeleteProject);

            mntmDeleteProject.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    if (projectList.getSelectedValue() != null) {

                        assert (projectManager.getCurrentProject() != null);
                        int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure to delete the project " + projectManager.getCurrentProject().getProject_name() + "?", "Warning", JOptionPane.YES_NO_OPTION);
                        if (dialogResult == JOptionPane.YES_OPTION) {


                            projectManager.deleteProject();
//                            JOptionPane.showMessageDialog(null, "Delete successfully!");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Please select a project to delete!");
                    }
                }
            });
        }

        //Log out
        mntmLogOut = new JMenuItem("Log Out");
        mnFile.add(mntmLogOut);

        mntmLogOut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentUser = null;
                selectedProject = null;
                parentJFrame.dispose();

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
        });


        //Exit
        mntmExit = new JMenuItem("Quit");
        mntmExit.setAccelerator(KeyStroke.getKeyStroke('Q', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
        mnFile.add(mntmExit);

        mntmExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parentJFrame.dispose();
            }
        });

        //Menu Edit
        JMenu mnEdit = new JMenu("Edit");
        menuBar.add(mnEdit);

        if (currentUser.getRole().equals("Project Manager")) {

            mntmDeleteAct = new JMenuItem("Delete Activity");
            mnEdit.add(mntmDeleteAct);

            mntmDeleteAct.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    if (projectManager.getCurrentProject() != null) {
                        if (activityEntry.isActivitySelected()) {
                            String activityName = projectManager.getCurrentProject().getActivities().get(activityEntry.getSelectedActivityRow()).getActivity_name();
                            int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure to delete the activity " +
                                    activityName + "?", "Warning", JOptionPane.YES_NO_OPTION);
                            if (dialogResult == JOptionPane.YES_OPTION) {

                                activityEntry.deleteActivity();
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Please select an activity to delete!");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Please select a project to delete an activity!");
                    }
                }
            });

            JMenu mnAnalysis = new JMenu("Analysis");
            menuBar.add(mnAnalysis);

            mntmGANTTChart = new JMenuItem("Gantt Chart");
            mnAnalysis.add(mntmGANTTChart);


            mntmPERT = new JMenuItem("PERT Chart");
            mnAnalysis.add(mntmPERT);

            mntmCriticalPath = new JMenuItem("Critical Path Analysis");
            mnAnalysis.add(mntmCriticalPath);

            mntmEarnedValue = new JMenuItem("Earned Value Analysis");
            mnAnalysis.add(mntmEarnedValue);

        }    }


    protected void initPanel() {

        JPanel parentContainer = getParentContainer("Gantt Chart");

        parentContainer.setLayout(new BorderLayout());

        Dimension dimension = new Dimension(150, 600);
        parentContainer.setMaximumSize(dimension);
        parentContainer.setMinimumSize(dimension);
        parentContainer.setPreferredSize(dimension);

        parentContainer.add(activityEntry.panel1, BorderLayout.CENTER);
    }
}
