package edu.concordia.comp354.gui;

import edu.concordia.comp354.controller.ProjectController;
import edu.concordia.comp354.model.Activity;
import edu.concordia.comp354.model.IActivityDetailRenderer;
import edu.concordia.comp354.model.Project;
import edu.concordia.comp354.model.User;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by joao on 15.07.01.
 */
public class MainWindow implements IActivityDetailRenderer {
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
	JSplitPane splitPane;

	//  project list
	private DefaultListModel lm;
	JList projectList;

	//  activity details
	private JTextField activityName;
	private JTextArea projectDescription;
	private JList users;
	private JSlider progressSlider;
	private JTextField cost;

	protected void initialize() {

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

//            JSplitPane splitPane_1 = new JSplitPane();
//            splitPane.setRightComponent(splitPane_1);

//            JPanel panel_1 = new JPanel();
//            splitPane_1.setLeftComponent(panel_1);

			tabbedPane = new JTabbedPane();

			//Add the tabbed pane to this panel.
			splitPane.setRightComponent(tabbedPane);

			//The following line enables to use scrolling tabs.
			tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

			initializeLeftPanel();
			updateList();
		}
	}

	protected JPanel getParentContainer(String title) {

		JPanel panel = new JPanel();
		tabbedPane.addTab(title, null, panel, null);
//        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

		return panel;
	}

	protected void updateList() {
		ProjectController pc = new ProjectController();
		pjList = pc.getProjectsByUser(MainWindow.currentUser);

		for (Project p : pjList) {
			lm.addElement(p.getProject_name());
		}
	}

	protected void initializeLeftPanel() {

		JPanel panel = new JPanel();
		panel.setMaximumSize(new Dimension(189,640));
		splitPane.setLeftComponent(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		JLabel lblProjects = new JLabel("Projects");
		lblProjects.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(lblProjects, BorderLayout.NORTH);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setMaximumSize(new Dimension(32767, 4000));
		panel.add(scrollPane);

		lm = new DefaultListModel();

		projectList = new JList(lm);
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
//        panel.add(projectList);

		projectList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					String s = projectList.getSelectedValue().toString();

					for (Project p : pjList) {
						if (p.getProject_name().equals(s)) {
							currentProject = p;
							DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						}
					}
				}
			}
		});

		JPanel panelSouth = new JPanel();
		panel.setPreferredSize(new Dimension(200, 400));
		panel.add(panelSouth, BorderLayout.SOUTH);
		panelSouth.setLayout(null);

		JLabel lblNewLabel = new JLabel("Details");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(70, 18, 44, 16);
		panelSouth.add(lblNewLabel);

		JLabel label = new JLabel("Name:");
		label.setBounds(5, 40, 40, 16);
		panelSouth.add(label);

		activityName = new JTextField();
		activityName.setBounds(57, 34, 134, 28);
		activityName.setColumns(10);
		activityName.setEnabled(false);
		panelSouth.add(activityName);

		JLabel label_1 = new JLabel("Description:");
		label_1.setBounds(5, 66, 83, 16);
		panelSouth.add(label_1);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scrollPane_1.setBounds(15, 94, 179, 93);
		panelSouth.add(scrollPane_1);

		projectDescription = new JTextArea();
		projectDescription.setLineWrap(true);
		projectDescription.setWrapStyleWord(true);
		scrollPane_1.setViewportView(projectDescription);
		projectDescription.setText("ipsum lorem");

		JLabel lblProgress = new JLabel("Progress:");
		lblProgress.setBounds(70, 195, 61, 16);
		panelSouth.add(lblProgress);

		progressSlider = new JSlider();
		progressSlider.setFont(new Font("Lucida Grande", Font.PLAIN, 9));
		progressSlider.setName("Progress");
		progressSlider.setSnapToTicks(true);
		progressSlider.setMinorTickSpacing(5);
		progressSlider.setMajorTickSpacing(10);
		progressSlider.setPaintTicks(true);
		progressSlider.setPaintLabels(true);
		progressSlider.setBounds(0, 218, 200, 38);
		panelSouth.add(progressSlider);

		JSeparator separator = new JSeparator();
		separator.setBounds(5, 6, 189, 16);
		panelSouth.add(separator);

		JLabel label_2 = new JLabel("Users");
		label_2.setAlignmentX(0.5f);
		label_2.setBounds(81, 287, 50, 16);
		panelSouth.add(label_2);

		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(21, 312, 161, 144);
		panelSouth.add(scrollPane_2);

		users = new JList();
		users.setModel(new AbstractListModel() {
			String[] values = new String[]{"John", "Jane", "Fred"};

			public int getSize() {
				return values.length;
			}

			public Object getElementAt(int index) {
				return values[index];
			}
		});
		users.setSelectedIndex(1);
		scrollPane_2.setViewportView(users);


		JLabel lblCost = new JLabel("Cost:");
		lblCost.setBounds(6, 477, 40, 16);
		panelSouth.add(lblCost);

		cost = new JTextField();
		cost.setBounds(45, 471, 134, 28);
		cost.setColumns(10);
		cost.setText("$10,000");
		panelSouth.add(cost);

	}

	public JFrame getParentJFrame() {
		return frmProjectManagementSystem;
	}

	public void setActivityDetails(Activity activity) {
		activityName.setText(activity.getActivity_name());
		projectDescription.setText(activity.getActivity_desc());

		ArrayList<User> actUsers = activity.getUsers();
		users.setListData(actUsers.toArray());

		progressSlider.setValue(activity.getProgress());
		DecimalFormat df = new DecimalFormat("#,###");
		cost.setText(df.format(activity.getValue()));

	}

	public void fillActivityDetails(Activity activity) {

		activity.setActivity_name(activityName.getText());
		activity.setActivity_desc(projectDescription.getText());

		ListModel model = users.getModel();

		ArrayList<User> actUsers = new ArrayList<>();

		for (int i = 0; i < model.getSize(); i++) {
			actUsers.add((User) model.getElementAt(i));
		}

		activity.setUsers(actUsers);
		activity.setProgress(progressSlider.getValue());
		activity.setValue(Integer.parseInt(cost.getText().replaceAll(",","")));
	}
}
