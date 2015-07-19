package edu.concordia.comp354.gui;

import edu.concordia.comp354.controller.ActivityController;
import edu.concordia.comp354.controller.ProjectController;
import edu.concordia.comp354.controller.UserController;
import edu.concordia.comp354.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MainDirectWindow extends MainWindow implements IProjectRenderer {
    protected static Activity selectedAct;

    private JMenuItem mntmNewProject;
    private JMenuItem mntmSaveProject;
    private JMenuItem mntmDeleteProject;
    private JMenuItem mntmEditProject;
    private JMenuItem mntmLogOut;
    private JMenuItem mntmExit;
    private JMenuItem mntmNewAct;
    private JMenuItem mntmDeleteAct;
    private JMenuItem mntmEditAct;
    private JMenuItem mntmAssignUser;
    private JMenuItem mntmGANTTChart;
    private JMenuItem mntmPERT;
    private JMenuItem mntmCriticalPath;
    private JMenuItem mntmEarnedValue;

    protected JLabel lblPJName;
    protected JLabel lblStartDate;
    protected JLabel lblEndDate;
    protected JLabel lblDescription;

    protected JList list;
    private JLabel lblProjectList;

    private static JTable activityTable;
    private JLabel lblActivitieList;
    private ListSelectionModel lsm;
    private static String[] columnNames = {"Activity Name",
            "Duration",
            "Predecessors",
            "Progress",
            "Description"};

    protected JList listUser;
    protected static DefaultListModel lm2;
    protected static List<User> userList;
    protected JLabel lblOpt;
    protected JLabel lblPess;
    protected JLabel lblActValue;

    public MainDirectWindow(ProjectManager projectManager) {
        super(projectManager);

        projectManager.setProjectRenderer(this);
        projectManager.setActivityDetailRenderer(this);

        initialize();

        if (currentUser.getRole().equals("Project Manager")) {
//            this.projectManager.loadProjects();
        } else {
            updateActivityList();
        }
    }

    protected void updateActivityList() {
        NonEditableModel tableModel = (NonEditableModel) activityTable.getModel();
        tableModel.setRowCount(0);
        activityTable.repaint();
        List<Activity> actList = new ArrayList<Activity>();

        if (currentUser.getRole().equals("Project Manager")) {
            ActivityController ac = new ActivityController();
            actList = ac.getActByProjectId(projectManager.getCurrentProject().getProject_id());
            for (Activity a : actList) {
                String[] s = new String[5];
                s[0] = a.getActivity_name();
                s[1] = a.getDuration() + "";

                //getPredecessors
                ActivityController ac1 = new ActivityController();
                List<Integer> list = ac1.getActPredecessors(a.getActivity_id());
                String pre = "";
                if (!list.isEmpty()) {
                    for (Integer i : list) {
                        ActivityController ac2 = new ActivityController();
                        List<Activity> l = ac2.getActByActId(i);
                        for (Activity act : l) {
                            if (pre == "") {
                                pre = pre + act.getActivity_name();
                            } else {
                                pre = pre + ", " + act.getActivity_name();
                            }
                        }
                    }
                }
                s[2] = pre;
                s[3] = a.getProgress() + "%";
                s[4] = a.getActivity_desc();
                tableModel.addRow(s);
            }
        } else {
            //get act list
            ActivityController ac = new ActivityController();
            List<Integer> li = ac.getActByUser(currentUser.getUser_id());
            for (Integer i : li) {
                ActivityController ac1 = new ActivityController();
                actList.addAll(ac1.getActByActId(i));
            }

            //set data for table
            for (Activity a : actList) {
                String[] s = new String[6];
                ProjectController pc1 = new ProjectController();
                Project p = pc1.getProjectByID(a.getProject_id());

                s[0] = p.getProject_name();
                s[1] = a.getActivity_name();
                s[2] = a.getDuration() + "";

                //getPredecessors
                ActivityController ac1 = new ActivityController();
                List<Integer> list = ac1.getActPredecessors(a.getActivity_id());
                String pre = "";
                if (!list.isEmpty()) {
                    for (Integer i : list) {
                        ActivityController ac2 = new ActivityController();
                        List<Activity> l = ac2.getActByActId(i);
                        for (Activity act : l) {
                            if (pre == "") {
                                pre = pre + act.getActivity_name();
                            } else {
                                pre = pre + ", " + act.getActivity_name();
                            }
                        }
                    }
                }
                s[3] = pre;
                s[4] = a.getProgress() + "%";
                s[5] = a.getActivity_desc();
                tableModel.addRow(s);
            }
        }


        activityTable.setModel(tableModel);
        tableModel.fireTableDataChanged();
    }

    protected static void updateUserList() {
        //update user list
        lm.removeAllElements();
        ActivityController ac2 = new ActivityController();
        List<Integer> list = ac2.getUserByAssignment(selectedAct);
        for (Integer i : list) {
            UserController uc = new UserController();
            User u = uc.getUserByID(i);
            lm.addElement(u.getUserName());
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
                            Login loginWindow = new Login();
                            loginWindow.login.setVisible(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });


        //Exit
        mntmExit = new JMenuItem("Exit");
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
//            //New Activity
//            mntmNewAct = new JMenuItem("New Activity");
//            mnEdit.add(mntmNewAct);
//
//            mntmNewAct.addActionListener(new ActionListener() {
//                public void actionPerformed(ActionEvent e) {
//
//                    if (selectedProject != null) {
//                        EventQueue.invokeLater(new Runnable() {
//                            public void run() {
//                                try {
//                                    CreateAct window = new CreateAct();
//                                    window.createAct.setVisible(true);
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
//                    } else {
//                        JOptionPane.showMessageDialog(null, "Please select a project before add an activity!");
//                    }
//                }
//            });


            mntmDeleteAct = new JMenuItem("Delete Activity");
            mnEdit.add(mntmDeleteAct);

            mntmDeleteAct.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    if (selectedProject != null) {
                        if (selectedAct != null) {
                            int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure to delete the activity " + selectedAct.getActivity_name()
                                    + "?", "Warning", JOptionPane.YES_NO_OPTION);
                            if (dialogResult == JOptionPane.YES_OPTION) {
                                ActivityController ac = new ActivityController();
                                ac.deleteAct(selectedAct.getActivity_id());

                                ActivityController ac2 = new ActivityController();
                                ac2.severPredecessorsFromActivity(selectedAct.getDBID());

                                ActivityController ac3 = new ActivityController();
                                ac3.deleteAssign(selectedAct.getActivity_id());

                                selectedAct = null;
//                                JOptionPane.showMessageDialog(null, "Delete successfully!");
                                updateActivityList();
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Please select an activity to delete!");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Please select a project to delete an activity!");
                    }
                }
            });

//            mntmEditAct = new JMenuItem("Edit Activity");
//            mnEdit.add(mntmEditAct);
//
//            mntmEditAct.addActionListener(new ActionListener() {
//                public void actionPerformed(ActionEvent e) {
//
//                    if (selectedProject != null) {
//                        if (selectedAct != null) {
//                            EventQueue.invokeLater(new Runnable() {
//                                public void run() {
//                                    try {
//                                        EditAct window = new EditAct();
//                                        window.editAct.setVisible(true);
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            });
//                        } else {
//                            JOptionPane.showMessageDialog(null, "Please select an activity!");
//                        }
//                    } else {
//                        JOptionPane.showMessageDialog(null, "Please select a project!");
//                    }
//                }
//            });

            //assign user to activity
            mntmAssignUser = new JMenuItem("Assign User");
            mnEdit.add(mntmAssignUser);

            mntmAssignUser.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    if (!lsm.isSelectionEmpty()) {

                        EventQueue.invokeLater(new Runnable() {
                            public void run() {
                                try {
                                    UserAssign window = new UserAssign();
                                    window.userAssign.setVisible(true);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else {
                        JOptionPane.showMessageDialog(null, "Please select an activity to assign user!");
                    }
                }
            });


            JMenu mnAnalysis = new JMenu("Analysis");
            menuBar.add(mnAnalysis);

            mntmGANTTChart = new JMenuItem("GANTT Chart");
            mnAnalysis.add(mntmGANTTChart);

//            mntmGANTTChart.addActionListener(new ActionListener() {
//                public void actionPerformed(ActionEvent e) {
//                    if (selectedProject != null) {
//
//                        EventQueue.invokeLater(new Runnable() {
//                            public void run() {
//                                try {
//                                    final Gantt gantt = new Gantt();
//                                    gantt.gantt.setVisible(true);
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
//                    } else {
//                        JOptionPane.showMessageDialog(null, "Please select a project!");
//                    }
//                }
//            });

            mntmPERT = new JMenuItem("PERT Chart");
            mnAnalysis.add(mntmPERT);

            mntmCriticalPath = new JMenuItem("Critical Path Analysis");
            mnAnalysis.add(mntmCriticalPath);

            mntmEarnedValue = new JMenuItem("Earned Value Analysis");
            mnAnalysis.add(mntmEarnedValue);

        }// else {
//            mntmEditAct = new JMenuItem("Edit Activity");
//            mnEdit.add(mntmEditAct);
//
//            mntmEditAct.addActionListener(new ActionListener() {
//                public void actionPerformed(ActionEvent e) {
//                    if (selectedAct != null) {
//                        try {
//                            EditActByMember window = new EditActByMember();
//                            window.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//                            window.setVisible(true);
//                        } catch (Exception ex) {
//                            ex.printStackTrace();
//                        }
//                    } else {
//                        JOptionPane.showMessageDialog(null, "Please select an activity!");
//                    }
//                }
//            });
//        }
    }


    protected void initPanel() {

        JPanel parentContainer = getParentContainer("Direct Entry");

//		JPanel panel = new JPanel();
//		panel.setLayout(null);
//
//		panel.setBackground(Color.WHITE);
//		panel.setBorder(new LineBorder(Color.LIGHT_GRAY));
//		panel.setBounds(0, 0, 200, 525);
//
//		JLabel lblPJInfo = new JLabel("Project Info");
//		lblPJInfo.setBounds(59, 0, 78, 23);
//		panel.add(lblPJInfo);
//
//		lblPJName = new JLabel("Project Name:");
//		lblPJName.setBounds(10, 29, 180, 23);
//		panel.add(lblPJName);
//
//		lblStartDate = new JLabel("Start Date  :");
//		lblStartDate.setBounds(10, 62, 180, 23);
//		panel.add(lblStartDate);
//
//		lblEndDate = new JLabel("End Date    :");
//		lblEndDate.setBounds(10, 95, 180, 23);
//		panel.add(lblEndDate);
//
//		lblDescription = new JLabel("Description :");
//		lblDescription.setVerticalAlignment(SwingConstants.TOP);
//		lblDescription.setBounds(10, 127, 180, 39);
//		panel.add(lblDescription);
//
//		lblProjectList = new JLabel("Project List");
//		lblProjectList.setBackground(Color.GRAY);
//		lblProjectList.setHorizontalAlignment(SwingConstants.LEFT);
//		lblProjectList.setBounds(10, 172, 180, 15);
//		panel.add(lblProjectList);
//
//		lm = new DefaultListModel();
//
//		list = new JList(lm);
//		list.setBackground(Color.LIGHT_GRAY);
//		list.setBounds(10, 189, 180, 326);
//		panel.add(list);
//
//		list.addListSelectionListener(new ListSelectionListener() {
//
//			@Override
//			public void valueChanged(ListSelectionEvent e) {
//				if (!e.getValueIsAdjusting()) {
//					String s = list.getSelectedValue().toString();
//
//					for (Project p : pjList) {
//						if (p.getProject_name().equals(s)) {
//							currentProject = p;
//							DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//						}
//					}
//				}
//			}
//		});

        parentContainer.setLayout(new BorderLayout());


        Dimension dimension = new Dimension(150, 600);
        parentContainer.setMaximumSize(dimension);
        parentContainer.setMinimumSize(dimension);
        parentContainer.setPreferredSize(dimension);


//		parentContainer.add(panel, BorderLayout.WEST);


        parentContainer.add(activityEntry.panel1, BorderLayout.CENTER);
    }

    @Override
    public void setProjectList() {

        lm.removeAllElements();

        for (Project p : projectManager.getProjectList()) {
            lm.addElement(p.getProject_name());
        }

        projectList.setListData(lm.toArray());
    }

    @Override
    public void setCurrentProject(int i) {

        if (projectList.getSelectedIndex() != i) {
            projectList.setSelectedIndex(i);
        }

//        activityEntry.setActivities(projectManager.getCurrentProject().getActivitityList(), true);
//
//        for (int i = 0; i < pjList.size(); i++) {
//            Project p = pjList.get(i);
//            if (p.getProject_name().equals(s)) {
//                selectedProject = activityEntry.project = p;
//
//                projectList.setSelectedIndex(i);
//                ActivityList activityList = new ActivityList(activityEntry);
//                activityList.setActivities(selectedProject.getActivities());
//                activityList.setStartDate(selectedProject.getStart_date());
//
//                activityEntry.setActivities(activityList, true);
//            }
//        }
    }
}
