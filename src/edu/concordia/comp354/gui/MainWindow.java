package edu.concordia.comp354.gui;

import edu.concordia.comp354.model.*;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by joao on 15.07.01.
 */
public class MainWindow implements IActivityDetailRenderer, IProjectRenderer {
    protected static User currentUser;
    protected static List<Project> pjList;
    protected static Project selectedProject;
    protected static JFrame frmProjectManagementSystem;
    static JMenuBar menuBar;
    static JTabbedPane tabbedPane;
    protected ActivityEntry activityEntry;
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
    private JTextField cost;
    private HashMap<String, JCheckBox> checkboxMap;
    private JTextField login;

    public MainWindow(ProjectManager projectManager) {
        this.projectManager = projectManager;

        projectManager.setProjectRenderer(this);
        projectManager.setActivityDetailRenderer(this);

        checkboxMap = new HashMap<>();
    }

    protected void initialize(ActivityEntry activityEntry) {
        this.activityEntry = activityEntry;
        projectManager.setActivityEntryRenderer(this.activityEntry);

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

            //Add the tabbed pane to this panel.
            splitPane.setRightComponent(tabbedPane);

            //The following line enables to use scrolling tabs.
            tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

            initializeLeftPanel();
            updateList();

//            //  select first project by default
//            if (pjList.size() != 0) {
//                selectProject(pjList.get(0).getProject_name());
//            }
        }
    }

    protected JPanel getParentContainer(String title) {

        JPanel panel = new JPanel();
        tabbedPane.addTab(title, null, panel, null);

//        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        return panel;
    }

    protected void updateList() {
        pjList = projectManager.loadProjects();

        for (Project p : pjList) {
            lm.addElement(p.getProject_name());
        }
    }

    protected void initializeLeftPanel() {

        JPanel panel = new JPanel();
        splitPane.setLeftComponent(panel);
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        JLabel lblProjects = new JLabel("Projects");
        lblProjects.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblProjects, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setMaximumSize(new Dimension(32767, 4000));
        panel.add(scrollPane);

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

        JPanel panelSouth = new JPanel();
        panel.setPreferredSize(new Dimension(200, 400));
        panel.add(panelSouth, BorderLayout.SOUTH);
        panelSouth.setLayout(null);

        JSeparator separator = new JSeparator();
        separator.setBounds(5, 6, 189, 16);
        panelSouth.add(separator);

        JLabel lblUserFilter = new JLabel("User Filter");
        lblUserFilter.setBounds(12, 20, 90, 16);
        panelSouth.add(lblUserFilter);

        userFilter = new JComboBox<>();

        userFilter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                projectManager.userFilterSelected(((JComboBox<String>) e.getSource()).getSelectedItem().toString());
            }
        });

        userFilter.setBounds(5, 40, 177, 27);
        panelSouth.add(userFilter);
        userFilter.setRenderer(new UserComboBox());

        JSeparator separator_1 = new JSeparator();
        separator_1.setBounds(2, 70, 189, 16);
        panelSouth.add(separator_1);

        JLabel lblNewLabel = new JLabel("Activity Details");
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setBounds(43, 80, 109, 16);
        panelSouth.add(lblNewLabel);

        JLabel label = new JLabel("Name:");
        label.setBounds(5, 105, 40, 16);
        panelSouth.add(label);

        activityName = new JTextField();
        activityName.setBounds(43, 99, 134, 28);
        activityName.setColumns(10);
        activityName.setEnabled(false);
        panelSouth.add(activityName);

        JLabel label_1 = new JLabel("Description:");
        label_1.setBounds(5, 131, 83, 16);
        panelSouth.add(label_1);

        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        scrollPane_1.setBounds(15, 159, 179, 93);
        panelSouth.add(scrollPane_1);

        projectDescription = new JTextArea();
        projectDescription.setLineWrap(true);
        projectDescription.setWrapStyleWord(true);
        scrollPane_1.setViewportView(projectDescription);

        JLabel lblProgress = new JLabel("Progress:");
        lblProgress.setBounds(70, 260, 61, 16);
        panelSouth.add(lblProgress);

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
        panelSouth.add(progressSlider);

        JLabel label_2 = new JLabel("Users");
        label_2.setAlignmentX(0.5f);
        label_2.setBounds(81, 352, 50, 16);
        panelSouth.add(label_2);

        JScrollPane scrollPane_2 = new JScrollPane();
        scrollPane_2.setBounds(21, 377, 161, 144);
        panelSouth.add(scrollPane_2);

        users = new CheckBoxList();

        populateUsers();
        scrollPane_2.setViewportView(users);
        users.setSelectedIndex(10000);

        JLabel lblCost = new JLabel("Value:");
        lblCost.setBounds(5, 540, 40, 16);
        panelSouth.add(lblCost);

        cost = new JTextField();
        cost.setBounds(45, 536, 134, 28);
        cost.setColumns(10);
        panelSouth.add(cost);

        separator = new JSeparator();
        separator.setBounds(5, 580, 189, 16);
        panelSouth.add(separator);

        label = new JLabel("Login:");
        label.setBounds(5, 605, 40, 16);
        panelSouth.add(label);

        login = new JTextField();
        login.setBounds(45, 600, 134, 28);
        login.setColumns(10);
        login.setEnabled(false);
        panelSouth.add(login);

        setCurrentUser(projectManager.getCurrentUser());
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

        progressSlider.setValue(activity.getProgress());
        DecimalFormat df = new DecimalFormat("#,###");
        cost.setText(df.format(activity.getValue()));
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
        activity.setValue(Integer.parseInt(cost.getText().replaceAll(",", "")));
    }

    @Override
    public void projectSelected(String projectName) {

        if ( projectName != null ) {
            for (int i = 0; i < projectList.getVisibleRowCount(); i++) {
                if (projectList.getModel().getElementAt(i).equals(projectName)) {
                    if ( projectList.getSelectedIndex() != i) {
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
        userFilter.setEnabled(enabled);
        projectDescription.setEnabled(enabled);
        progressSlider.setEnabled(enabled);
        users.setEnabled(enabled);
        cost.setEnabled(enabled);

        activityEntry.setEnabled(enabled);
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
            JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index, isSelected,cellHasFocus);

            User user = MainWindow.this.projectManager.getUser((String)value);

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
