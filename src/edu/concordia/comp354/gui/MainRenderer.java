package edu.concordia.comp354.gui;

import com.mxgraph.view.mxGraph;
import edu.concordia.comp354.model.*;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by joao on 15.07.01.
 */
public class MainRenderer implements IActivityEntryRenderer, IActivityDetailRenderer, IProjectRenderer {
    protected static User currentUser;
    protected static List<Project> pjList;
    protected static Project selectedProject;
    protected static JFrame frmProjectManagementSystem;
    static JMenuBar menuBar;
    public static JTabbedPane tabbedPane;
    protected ActivityEntry activityEntry;
    protected JMenuItem mntmEditProject;
    protected JMenuItem mntmLogOut;
    protected JMenuItem mntmExit;
    protected JMenuItem mntmDeleteAct;
    protected JMenuItem mntmGANTTChart;
    protected JMenuItem mntmPERT;
    protected JMenuItem mntmCriticalPath;
    protected JMenuItem mntmEarnedValue;
    protected JMenuItem mntmNewProject;
    protected JMenuItem mntmSaveProject;
    protected JMenuItem mntmDeleteProject;
    protected JList list;
    JSplitPane splitPane;

    //  project list
    protected static DefaultListModel lm;
    protected ProjectManager projectManager;
    static JList projectList;

    //  activity details
    JComboBox<String> userFilter;
    private JTextField activityName;
    private JTextArea projectDescription;
    private CheckBoxList users;
    private JSlider progressSlider;
    private JTextField value;
    private HashMap<String, JCheckBox> checkboxMap;
    private JTextField login;
    public static DecimalFormat DF = new DecimalFormat("#,###");
    public static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,###.##");
    public static DecimalFormat DOLLAR_FORMAT = new DecimalFormat("'$'#,###");
    public static DecimalFormat PERCENT_FORMAT = new DecimalFormat("#%");
    public JPanel evaPanel;
    public JComboBox<String> evaDateSelector;
    public JTextField pvFld;
    public JTextField evFld;
    public JTextField acFld;
    public JTextField bacFld;
    public JTextField svFld;
    public JTextField cvFld;
    public JTextField cpiFld;
    public JTextField spiFld;
    public JTextField eacFld;
    public JTextField etcFld;
    public JTextField vacFld;
    public JTextField completedFld;
    JPanel leftPanel;
    public JPanel activityPanel;
    JButton btnSetEV;

    {
        DF.setRoundingMode(RoundingMode.HALF_UP);
        DECIMAL_FORMAT.setRoundingMode(RoundingMode.HALF_UP);
        DOLLAR_FORMAT.setRoundingMode(RoundingMode.HALF_UP);
    }
    public MainRenderer(ProjectManager projectManager) {
        this.projectManager = projectManager;

//        DF.setRoundingMode(RoundingMode.UP);
//        DECIMAL_FORMAT.setRoundingMode(RoundingMode.UP);
//        DOLLAR_FORMAT.setRoundingMode(RoundingMode.UP);

        projectManager.setProjectRenderer(this);
        projectManager.setActivityDetailRenderer(this);
        projectManager.setActivityEntryRenderer(this);

        checkboxMap = new HashMap<>();

        if (frmProjectManagementSystem == null) {
            frmProjectManagementSystem = new JFrame();
            frmProjectManagementSystem.setTitle("Project Management System");
            frmProjectManagementSystem.setBounds(0, 0, 1024, 800);
            frmProjectManagementSystem.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            menuBar = new JMenuBar();
            frmProjectManagementSystem.setJMenuBar(menuBar);
            frmProjectManagementSystem.getContentPane().setLayout(new GridLayout(1, 1));

            //  set up split panes
            JPanel panel = new JPanel();
            panel.setBorder(new EmptyBorder(5, 5, 5, 5));
            panel.setLayout(new BorderLayout(0, 0));

            splitPane = new JSplitPane();
            splitPane.setOneTouchExpandable(true);
            panel.add(splitPane);
            frmProjectManagementSystem.add(panel);

            tabbedPane = new JTabbedPane();
            ChangeListener changeListener = new ChangeListener() {
                public void stateChanged(ChangeEvent changeEvent) {
                    JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
                    int index = sourceTabbedPane.getSelectedIndex();
//                    System.out.println("Tab changed to: " + sourceTabbedPane.getTitleAt(index));

                    tabSelected((ActivityEntry) sourceTabbedPane.getSelectedComponent());
                }
            };
            tabbedPane.addChangeListener(changeListener);

            //Add the tabbed pane to this panel.
            splitPane.setRightComponent(tabbedPane);

            //The following line enables to use scrolling tabs.
            tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

            initializeLeftPanel();
            updateList();

            initMenus();
        }
    }

    private void tabSelected(ActivityEntry activityEntry) {
        setActivityEntry(activityEntry);
        if (activityEntry.getCurrentProject() != null) {
            selectProject(activityEntry.getCurrentProject().getProject_name());
        }
        activityEntry.gainFocus();

        activityEntry.autoResizeColumns();
    }

    protected void setActivityEntry(ActivityEntry activityEntry) {
        this.activityEntry = activityEntry;

//        projectManager.setActivityEntryRenderer(this.activityEntry);
        projectManager.setActivityEntryRenderer(this);
    }

    protected JPanel getParentContainer(JPanel panel) {

        JPanel parentPanel = new JPanel();
        tabbedPane.addTab(panel.getName(), null, parentPanel, null);
//        tabbedPane.addTab(panel.getName(), null, panel, null);
//        parentPanel.add(panel);

        return parentPanel;
    }


    protected void updateList() {
        pjList = projectManager.loadProjects();

        for (Project p : pjList) {
            lm.addElement(p.getProject_name());
        }
    }

    protected void initializeLeftPanel() {

        leftPanel = new JPanel();
        splitPane.setLeftComponent(leftPanel);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
        JLabel lblProjects = new JLabel("Projects");
        lblProjects.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(lblProjects, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setMaximumSize(new Dimension(32767, 4000));
        leftPanel.add(scrollPane);

        lm = new DefaultListModel();

        projectList = new JList<String>(lm);
        projectList.setSelectedIndex(0);
        projectList.setAlignmentY(Component.TOP_ALIGNMENT);
        projectList.setPreferredSize(new Dimension(34, 100));
        projectList.setMaximumSize(new Dimension(34, 150));
        projectList.setMinimumSize(new Dimension(34, 100));
        scrollPane.setViewportView(projectList);
        projectList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        projectList.setVisibleRowCount(-1);

        projectList.setBackground(Color.LIGHT_GRAY);
        projectList.setBounds(10, 189, 180, 326);

        projectList.addListSelectionListener(new ListSelectionListener() {

            /*
            project selection
             */
            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (projectManager.getCurrentProject() != null && projectManager.getCurrentProject().isDirty()) {
                    int dialogResult = JOptionPane.showConfirmDialog(null, "Save project " + projectManager.getCurrentProject().getProject_name()
                            + "?", "Warning", JOptionPane.YES_NO_OPTION);

                    switch (dialogResult) {
                        case JOptionPane.YES_OPTION:
                            projectManager.saveProject();
                            projectSelected(e);
                            break;

                        case JOptionPane.NO_OPTION:
                            projectManager.loadProject(projectManager.getCurrentProject());
                            projectSelected(e);
                            break;

                        case JOptionPane.CANCEL_OPTION:
                            break;
                    }
                } else {
                    projectSelected(e);
                }
            }
        });

        activityPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(200, 400));
        leftPanel.add(activityPanel, BorderLayout.SOUTH);
        activityPanel.setLayout(null);

        JSeparator separator = new JSeparator();
        separator.setBounds(5, 6, 189, 16);
        activityPanel.add(separator);

        JLabel lblUserFilter = new JLabel("User Filter");
        lblUserFilter.setBounds(12, 20, 90, 16);
        activityPanel.add(lblUserFilter);

        userFilter = new JComboBox<>();

        userFilter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                projectManager.userFilterSelected(((JComboBox<String>) e.getSource()).getSelectedItem().toString());
            }
        });

        userFilter.setBounds(5, 40, 177, 27);
        activityPanel.add(userFilter);
        userFilter.setRenderer(new UserComboBox());

        JSeparator separator_1 = new JSeparator();
        separator_1.setBounds(2, 70, 189, 16);
        activityPanel.add(separator_1);

        /*
            Activity panel
         */
        JLabel lblNewLabel = new JLabel("Activity Details");
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setBounds(43, 80, 109, 16);
        activityPanel.add(lblNewLabel);

        JLabel label = new JLabel("Name:");
        label.setBounds(5, 105, 40, 16);
        activityPanel.add(label);

        activityName = new JTextField();
        activityName.setBounds(43, 99, 134, 28);
        activityName.setColumns(10);
        activityName.setEnabled(false);
        activityPanel.add(activityName);

        JLabel label_1 = new JLabel("Description:");
        label_1.setBounds(5, 131, 83, 16);
        activityPanel.add(label_1);

        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        scrollPane_1.setBounds(15, 159, 179, 93);
        activityPanel.add(scrollPane_1);

        projectDescription = new JTextArea();
        projectDescription.setLineWrap(true);
        projectDescription.setWrapStyleWord(true);
        scrollPane_1.setViewportView(projectDescription);

        JLabel lblProgress = new JLabel("Progress:");
        lblProgress.setBounds(70, 260, 61, 16);
        activityPanel.add(lblProgress);

        progressSlider = new JSlider();
        progressSlider.setFont(new Font("Lucida Grande", Font.PLAIN, 9));
        progressSlider.setName("Progress");
        progressSlider.setSnapToTicks(true);
        progressSlider.setMinorTickSpacing(5);
        progressSlider.setMajorTickSpacing(10);
        progressSlider.setPaintTicks(true);
        progressSlider.setPaintLabels(true);
        progressSlider.setBounds(0, 283, 200, 38);
        progressSlider.setValue(0);


        progressSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                if (!source.getValueIsAdjusting()) {
                    projectManager.losingDetailFocus(activityEntry.getSelectedActivityRow());
                }
            }
        });

        activityPanel.add(progressSlider);

        JLabel label_2 = new JLabel("Users");
        label_2.setAlignmentX(0.5f);
        label_2.setBounds(81, 352, 50, 16);
        activityPanel.add(label_2);

        JScrollPane scrollPane_2 = new JScrollPane();
        scrollPane_2.setBounds(21, 377, 161, 144);
        activityPanel.add(scrollPane_2);

        users = new CheckBoxList();

        populateUsers();
        scrollPane_2.setViewportView(users);
        users.setSelectedIndex(10000);

        JLabel lblCost = new JLabel("Value:");
        lblCost.setBounds(5, 540, 40, 16);
        activityPanel.add(lblCost);

        value = new JTextField();
        value.setBounds(45, 536, 134, 28);
        value.setColumns(10);
        activityPanel.add(value);

        value.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                projectManager.losingDetailFocus(activityEntry.getSelectedActivityRow());
            }
        });

        separator = new JSeparator();
        separator.setBounds(5, 580, 189, 16);
        activityPanel.add(separator);

        label = new JLabel("Login:");
        label.setBounds(5, 605, 40, 16);
        activityPanel.add(label);

        login = new JTextField();
        login.setBounds(45, 600, 134, 28);
        login.setColumns(10);
        login.setEnabled(false);
        activityPanel.add(login);

        setCurrentUser(projectManager.getCurrentUser());

/*
EVAPanel
 */
        evaPanel = new JPanel();
        evaPanel.setBounds(5, 12, 195, 570);
        leftPanel.add(evaPanel);
        evaPanel.setLayout(null);

        evaDateSelector = new JComboBox<>();
        evaDateSelector.setBounds(21, 33, 155, 27);
        evaPanel.add(evaDateSelector);

        JLabel lblNewLabel_1 = new JLabel("Earned Value Analysis for:");
        lblNewLabel_1.setBounds(11, 5, 178, 16);
        evaPanel.add(lblNewLabel_1);

        JLabel lblProgressReview = new JLabel("Progress Review");
        lblProgressReview.setBounds(6, 61, 111, 16);
        evaPanel.add(lblProgressReview);

        JLabel lblPlannedValue = new JLabel("PV:");
        lblPlannedValue.setBounds(29, 95, 31, 16);
        evaPanel.add(lblPlannedValue);

        pvFld = new JTextField();
        pvFld.setBounds(51, 89, 100, 28);
        evaPanel.add(pvFld);
        pvFld.setColumns(10);

        JLabel lblEv = new JLabel("EV:");
        lblEv.setBounds(29, 129, 31, 16);
        evaPanel.add(lblEv);

        evFld = new JTextField();
        evFld.setBounds(51, 123, 100, 28);
        evaPanel.add(evFld);
        evFld.setColumns(10);

        JLabel lblAc = new JLabel("AC:");
        lblAc.setBounds(29, 161, 31, 16);
        evaPanel.add(lblAc);

        acFld = new JTextField();
        acFld.setBounds(51, 155, 100, 28);
        evaPanel.add(acFld);
        acFld.setColumns(10);

        JLabel lblBac = new JLabel("BAC:");
        lblBac.setBounds(21, 195, 39, 16);
        evaPanel.add(lblBac);

        bacFld = new JTextField();
        bacFld.setBounds(51, 189, 100, 28);
        evaPanel.add(bacFld);
        bacFld.setColumns(10);

        JLabel lblPerformanceIndicators = new JLabel("Performance Indicators");
        lblPerformanceIndicators.setBounds(21, 223, 160, 16);
        evaPanel.add(lblPerformanceIndicators);

        JLabel lblSv = new JLabel("SV:");
        lblSv.setBounds(29, 257, 31, 16);
        evaPanel.add(lblSv);

        svFld = new JTextField();
        svFld.setBounds(51, 251, 100, 28);
        evaPanel.add(svFld);
        svFld.setColumns(10);

        JLabel lblCv = new JLabel("CV:");
        lblCv.setBounds(29, 291, 31, 16);
        evaPanel.add(lblCv);

        cvFld = new JTextField();
        cvFld.setBounds(51, 285, 100, 28);
        evaPanel.add(cvFld);
        cvFld.setColumns(10);

        JLabel lblCpi = new JLabel("CPI:");
        lblCpi.setBounds(29, 323, 31, 16);
        evaPanel.add(lblCpi);

        cpiFld = new JTextField();
        cpiFld.setBounds(51, 317, 100, 28);
        evaPanel.add(cpiFld);
        cpiFld.setColumns(10);

        JLabel lblSpi = new JLabel("SPI:");
        lblSpi.setBounds(29, 357, 31, 16);
        evaPanel.add(lblSpi);

        spiFld = new JTextField();
        spiFld.setBounds(51, 351, 100, 28);
        evaPanel.add(spiFld);
        spiFld.setColumns(10);

        JLabel lblEac = new JLabel("EAC:");
        lblEac.setBounds(16, 390, 31, 16);
        evaPanel.add(lblEac);

        eacFld = new JTextField();
        eacFld.setBounds(51, 385, 100, 28);
        evaPanel.add(eacFld);
        eacFld.setColumns(10);

        JLabel lblEtc = new JLabel("ETC:");
        lblEtc.setBounds(16, 424, 31, 16);
        evaPanel.add(lblEtc);

        etcFld = new JTextField();
        etcFld.setBounds(51, 419, 100, 28);
        evaPanel.add(etcFld);
        etcFld.setColumns(10);

        JLabel lblVac = new JLabel("VAC:");
        lblVac.setBounds(16, 456, 31, 16);
        evaPanel.add(lblVac);

        vacFld = new JTextField();
        vacFld.setBounds(51, 451, 100, 28);
        evaPanel.add(vacFld);
        vacFld.setColumns(10);

        JLabel lblCompleted = new JLabel("Completed:");
        lblCompleted.setBounds(0, 490, 77, 16);
        evaPanel.add(lblCompleted);

        completedFld = new JTextField();
        completedFld.setBounds(77, 484, 53, 28);
        evaPanel.add(completedFld);
        completedFld.setColumns(10);

        btnSetEV = new JButton("Set EV");
        btnSetEV.setBounds(21, 524, 117, 29);
        evaPanel.add(btnSetEV);

        btnSetEV.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                projectManager.computeEVAforDate((String) evaDateSelector.getSelectedItem());
            }
        });

        activityPanel.setVisible(false);
        evaPanel.setVisible(true);
    }

    @Override
    public void setProjectList() {

        lm.removeAllElements();

        for (Project p : projectManager.getProjectList()) {
            lm.addElement(p.getProject_name());
        }

        projectList.setListData(lm.toArray());
    }

    public void populateUsers() {

        userFilter.addItem(ProjectManager.NO_FILTER);

        for (String userName : projectManager.getUserNames()) {
            JCheckBox checkbox = new JCheckBox(userName);

            checkboxMap.put(userName, checkbox);
            addCheckbox(checkbox);

            userFilter.addItem(userName);
        }
    }

    public void addCheckbox(JCheckBox checkBox) {
        ListModel currentList = users.getModel();
        JCheckBox[] newList = new JCheckBox[currentList.getSize() + 1];
        for (int i = 0; i < currentList.getSize(); i++) {
            newList[i] = (JCheckBox) currentList.getElementAt(i);
        }
        newList[newList.length - 1] = checkBox;
        users.setListData(newList);
    }

    private void projectSelected(ListSelectionEvent e) {
//        if (!e.getValueIsAdjusting()) {

        if (projectList.getSelectedValue() == null) {
            projectList.setSelectedIndex(0);
        }
        if (pjList.size() != 0 && projectList.getSelectedValue() != null) {
            String s = projectList.getSelectedValue().toString();

            selectProject(s);
        }
//        }
    }

    private void selectProject(String s) {

        projectManager.setCurrentProject(s);
    }

    public JFrame getParentJFrame() {
        return frmProjectManagementSystem;
    }

    public void setUIDetailsFromActivity(Activity activity) {
        activityName.setText(activity.getActivity_name());
        projectDescription.setText(activity.getActivity_desc());

        for (JCheckBox checkbox : checkboxMap.values()) {
            checkbox.setSelected(false);
        }

        List<User> actUsers = activity.getUsers();
        if (actUsers != null) {
            for (User user : actUsers) {
                JCheckBox checkbox = checkboxMap.get(user.getUserName());
                checkbox.setSelected(true);
            }
        }

        value.setText(DF.format(activity.getValue()));  //  call this before progressSlider.setValue()
        progressSlider.setValue(activity.getProgress());
//        users.setSelectionInterval(-1,-1);
        users.repaint();
    }

    public void getActivityDetailsFromUI(Activity activity) {

        activity.setActivity_desc(projectDescription.getText());

        ListModel<JCheckBox> model = users.getModel();

        List<String> actUsers = new ArrayList<>();

        for (int i = 0; i < model.getSize(); i++) {

            if (model.getElementAt(i).isSelected()) {
                actUsers.add(model.getElementAt(i).getText());
            }
        }

        activity.setUsers(projectManager.getUserList(actUsers));
        activity.setProgress(progressSlider.getValue());
        activity.setValue(Integer.parseInt(StringUtils.defaultIfEmpty(value.getText().replaceAll(",", ""), "0")));
    }

    @Override
    public void projectSelected(String projectName) {

        if (projectName != null) {
            for (int i = 0; i < projectList.getVisibleRowCount(); i++) {
                if (projectList.getModel().getElementAt(i).equals(projectName)) {
                    if (projectList.getSelectedIndex() != i) {
                        projectList.setSelectedIndex(i);
                    }
                    break;
                }
            }
            setEnabled(true);
        } else {
            setEnabled(false);
        }

//        if (projectList.getSelectedIndex() != i) {
//            projectList.setSelectedIndex(i);
//        }
    }

    @Override
    public void setCurrentUser(User currentUser) {
        login.setText(currentUser != null ? currentUser.getUserName() : "");
    }

    public void setEnabled(boolean enabled) {
        setDetailEnabled(enabled);

//       if ( activityEntry !=null) {
        activityEntry.setEnabled(enabled);
//       }
    }

    public void setDetailEnabled(boolean enabled) {
        userFilter.setEnabled(enabled);
        projectDescription.setEnabled(enabled);
        progressSlider.setEnabled(enabled);
        users.setEnabled(enabled);
        value.setEnabled(enabled);
    }

    protected void initMenus() {
        JFrame parentJFrame = getParentJFrame();

        //menu file
        JMenu mnFile = new JMenu("File");
        menuBar.add(mnFile);

        if (currentUser.getRole().equals("Project Manager")) {
            //New project
            mntmNewProject = new JMenuItem("New Project");
            mntmNewProject.setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
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
            mntmSaveProject.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
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
        mntmExit.setAccelerator(KeyStroke.getKeyStroke('Q', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
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

        }
    }

    public void addTab(BaseTab baseTab) {
        baseTab.setParentWindow(this);

        baseTab.initializeTab();
    }

    @Override
    public int getRowHeight() {
        return activityEntry.getRowHeight();
    }

    @Override
    public int getXScale() {
        return activityEntry.getXScale();
    }

    @Override
    public int getXGap() {
        return activityEntry.getXGap();
    }

    @Override
    public void setActivityList() {
        activityEntry.setActivityList();
    }

    @Override
    public void fillActivityList() {
        activityEntry.fillActivityList();
    }

    @Override
    public void autoLayout(mxGraph graph) {
        activityEntry.autoLayout(graph);

    }

    @Override
    public void setCPMData(Object[] objects, Object[] objects1) {
        activityEntry.setCPMData(objects, objects1);

    }

    @Override
    public void activitySelected(int id) {
        activityEntry.activitySelected(id);
    }

    @Override
    public void clear() {
        activityEntry.clear();
    }

    @Override
    public int getProjectID() {
        return activityEntry.getProjectID();
    }

    @Override
    public void filterByUser(String userName) {
        if (activityEntry != null) {
            activityEntry.filterByUser(userName);
        }
    }

    @Override
    public boolean isActiveActivity(int id) {
        return activityEntry.isActiveActivity(id);
    }

    @Override
    public void clearActivityDetails() {
        activityEntry.clearActivityDetails();
    }

    public void selectTab(int i) {
        tabbedPane.setSelectedIndex(i);
        tabSelected((ActivityEntry) tabbedPane.getSelectedComponent());
    }

    public void selectEVADate(String dateStr) {
        evaDateSelector.setSelectedItem(dateStr);
    }

    public class UserComboBox extends BasicComboBoxRenderer {
        protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

        private final int pmFont;

        public UserComboBox() {
            pmFont = Font.BOLD;
        }

        public UserComboBox(int style) {
            pmFont = style;
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            User user = MainRenderer.this.projectManager.getUser((String) value);

            if (user != null && user.getRole().equals(ProjectManager.ROLE_MANAGER)) {
                Font oldFont = getFont();
                Font newFont = new Font(oldFont.getName(), Font.BOLD, oldFont.getSize());
                renderer.setFont(newFont);
            } else {
                Font oldFont = getFont();
                Font newFont = new Font(oldFont.getName(), Font.PLAIN, oldFont.getSize());
                renderer.setFont(newFont);
            }

            return renderer;
        }
    }
}

class CheckBoxList extends JList {
    protected static Border noFocusBorder =
            new EmptyBorder(1, 1, 1, 1);

    public CheckBoxList() {
        setCellRenderer(new CellRenderer());

        addMouseListener(new MouseAdapter() {
                             public void mousePressed(MouseEvent e) {
                                 int index = locationToIndex(e.getPoint());

                                 if (index != -1) {
                                     JCheckBox checkbox = (JCheckBox)
                                             getModel().getElementAt(index);
                                     checkbox.setSelected(
                                             !checkbox.isSelected());
                                     repaint();
                                 }
                             }
                         }
        );

        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    protected class CellRenderer implements ListCellRenderer {
        public Component getListCellRendererComponent(
                JList list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            JCheckBox checkbox = (JCheckBox) value;
            checkbox.setBackground(isSelected ?
                    getSelectionBackground() : getBackground());
            checkbox.setForeground(isSelected ?
                    getSelectionForeground() : getForeground());
            checkbox.setEnabled(isEnabled());
            checkbox.setFont(getFont());
            checkbox.setFocusPainted(false);
            checkbox.setBorderPainted(true);
            checkbox.setBorder(isSelected ?
                    UIManager.getBorder(
                            "List.focusCellHighlightBorder") : noFocusBorder);
            return checkbox;
        }
    }
}
