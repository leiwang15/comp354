package gui;

import javax.swing.JFrame;

import Controller.ActivityController;
import Controller.ProjectController;
import Model.Activity;
import Model.Project;
import Model.User;
import gui.ActivityEntry;

import javax.swing.DefaultListModel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import java.awt.EventQueue;

import javax.swing.SwingConstants;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.JList;
import javax.swing.JTable;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

public class MainWindow {
	
	protected static User currentUser;
	protected static Project currentProject;
	
	private JMenuItem mntmNewProject;
	private JMenuItem mntmDeleteProject;
	private JMenuItem mntmSave;
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
	
	protected JFrame frmProjectManagementSystem;
	protected JList list;
	protected static DefaultListModel lm;
	protected static List<Project> pjList;
	private JLabel lblProjectList;
	
//	private static JTable activityTable;
//	private JLabel lblActivitieList;
//	private static String[] columnNames = {"Activity Name",
//            "Duration",
//            "Predecessors",
//            "Progress",
//            "Description"};
	
	private ActivityEntry activityEntry;
	
	public MainWindow() {
		initialize();
		updateProjectList();
	}

	protected static void updateProjectList() {		
		ProjectController pc = new ProjectController();
		pjList = pc.getProjectsByUser(MainWindow.currentUser);
		
		lm.removeAllElements();
		for(Project p : pjList){
			lm.addElement(p.getProject_name());
		}
	}
	
//	protected static void updateActivityList(){
//		NonEditableModel tableModel = (NonEditableModel) activityTable.getModel();
//		tableModel.setRowCount(0);
//				
////		activityTable.repaint();
//		ActivityController ac = new ActivityController();
//		List<Activity> actList = ac.getActByProjectId(currentProject.getProject_id());
//		
//		for(Activity a : actList){
//			String[] s = new String[5];
//			s[0] = a.getActivity_name();
//			s[1] = a.getDuration() + "";
//			
//			//getPredecessors
//			ActivityController ac1 = new ActivityController();
//			List<Integer> list = ac1.getActPrecedence(a.getActivity_id());
//			String pre = "";
//			if(!list.isEmpty()){
//				for(Integer i : list){
//					ActivityController ac2 = new ActivityController();
//					List<Activity> l = ac2.getActByActId(i);
//					for(Activity act : l){
//						if(pre == ""){
//							pre = pre + act.getActivity_name();
//						}
//						else{
//							pre = pre + ", " + act.getActivity_name();
//						}
//					}
//				}
//			}
//			s[2] = pre;
//			s[3] = a.getProgress() + "";
//			s[4] = a.getActivity_desc();
//			tableModel.addRow(s);
//		}
//		activityTable.setModel(tableModel);
//		tableModel.fireTableDataChanged();
//	}

	private void initialize() {
		frmProjectManagementSystem = new JFrame();
		frmProjectManagementSystem.setTitle("Project Management System");
		frmProjectManagementSystem.setBounds(100, 100, 1000, 600);
		frmProjectManagementSystem.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frmProjectManagementSystem.setResizable(false);
		
		JMenuBar menuBar = new JMenuBar();
		frmProjectManagementSystem.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		//New project
		mntmNewProject = new JMenuItem("New Project");
		mnFile.add(mntmNewProject);
		
		mntmNewProject.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							CreatePJ window = new CreatePJ();
							window.frame.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
		
		//Save
		mntmSave = new JMenuItem("Save");
		mnFile.add(mntmSave);
		
		mntmSave.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
			
			}
		});
		
		
		//Delete		
		mntmDeleteProject = new JMenuItem("Delete Project");
		mnFile.add(mntmDeleteProject);
		
		mntmDeleteProject.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				if(list.getSelectedValue() != null){
            		String s = list.getSelectedValue().toString();
                	
                	for(Project p : pjList){
            			if (p.getProject_name().equals(s)){
            				
            				int dialogResult = JOptionPane.showConfirmDialog (null, "Are you sure to delete the project " + p.getProject_name()
            																	+ "?","Warning",JOptionPane.YES_NO_OPTION);
            				if(dialogResult == JOptionPane.YES_OPTION){
            				
	            				ProjectController pc = new ProjectController();
	            				pc.deleteProject(p);
	            				JOptionPane.showMessageDialog(null, "Delete successfully!");
	            				updateProjectList();
            				}
            			}
            		}
            	}
				else{
					JOptionPane.showMessageDialog(null, "Please select a project to delete!");
				}
			}
		});
		
		
		//Log out
		mntmLogOut = new JMenuItem("Log Out");
		mnFile.add(mntmLogOut);
		
		mntmLogOut.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				currentUser = null;
				currentProject = null;
				frmProjectManagementSystem.dispose();
				
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							Login loginWindow = new Login();
							loginWindow.frmLogin.setVisible(true);
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
		
		mntmExit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				frmProjectManagementSystem.dispose();
			}
		});

		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		//New Activity
		mntmNewAct = new JMenuItem("New Activity");
		mnEdit.add(mntmNewAct);
		
		mntmNewAct.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				if(currentProject != null){
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {
								CreateAct window = new CreateAct();
								window.frmNewActivity.setVisible(true);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				}
				else{
					JOptionPane.showMessageDialog(null, "Please select a project before add an activity!");
				}
			}
		});
		

		mntmDeleteAct = new JMenuItem("Delete Activity");
		mnEdit.add(mntmDeleteAct);
		
		mntmEditAct = new JMenuItem("Edit Activity");
		mnEdit.add(mntmEditAct);
		
		mntmAssignUser = new JMenuItem("Assign User");
		mnEdit.add(mntmAssignUser);
		
		JMenu mnAnalysis = new JMenu("Analysis");
		menuBar.add(mnAnalysis);
		
		mntmGANTTChart = new JMenuItem("GANTT Chart");
		mnAnalysis.add(mntmGANTTChart);
		
		mntmPERT = new JMenuItem("PERT Chart");
		mnAnalysis.add(mntmPERT);
		
		mntmCriticalPath = new JMenuItem("Critical Path Analysis");
		mnAnalysis.add(mntmCriticalPath);
		
		mntmEarnedValue = new JMenuItem("Earned Value Analysis");
		mnAnalysis.add(mntmEarnedValue);
		
		frmProjectManagementSystem.getContentPane().setLayout(null);
		
		JLabel lblWelcomeBack = new JLabel("Welcome back " + currentUser.getUserName() + "    User type: " + currentUser.getRole());
		lblWelcomeBack.setBounds(0, 0, 984, 541);
		lblWelcomeBack.setVerticalAlignment(SwingConstants.BOTTOM);
		frmProjectManagementSystem.getContentPane().add(lblWelcomeBack);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel.setBounds(0, 0, 984, 525);
		frmProjectManagementSystem.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblPJInfo = new JLabel("Project Info");
		lblPJInfo.setBounds(59, 0, 78, 23);
		panel.add(lblPJInfo);
		
		lblPJName = new JLabel("Project Name:");
		lblPJName.setBounds(10, 29, 180, 23);
		panel.add(lblPJName);
		
		lblStartDate = new JLabel("Start Date  :");
		lblStartDate.setBounds(10, 62, 180, 23);
		panel.add(lblStartDate);
		
		lblEndDate = new JLabel("End Date    :");
		lblEndDate.setBounds(10, 95, 180, 23);
		panel.add(lblEndDate);
		
		lblDescription = new JLabel("Description :");
		lblDescription.setVerticalAlignment(SwingConstants.TOP);
		lblDescription.setBounds(10, 127, 180, 39);
		panel.add(lblDescription);
				
		lm = new DefaultListModel();
		
		lblProjectList = new JLabel("Project List");
		lblProjectList.setBackground(Color.GRAY);
		lblProjectList.setHorizontalAlignment(SwingConstants.CENTER);
		lblProjectList.setBounds(10, 172, 180, 15);
		panel.add(lblProjectList);
		
		list = new JList(lm);
		list.setBackground(Color.LIGHT_GRAY);
		list.setBounds(10, 189, 180, 326);
		panel.add(list);
		
		list.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                	if(list.getSelectedValue() != null){
                		String s = list.getSelectedValue().toString();
                    	
                    	for(Project p : pjList){
                			if (p.getProject_name().equals(s)){
                				currentProject = p;
                				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                				
            	                lblPJName.setText("Project Name: " + p.getProject_name());
            	                lblStartDate.setText("Start Date  : " + df.format(p.getStart_date()));
            	                lblEndDate.setText("End Date    : " + df.format(p.getEnd_date()));
            	                lblDescription.setText("Description : " + p.getProject_desc());
                			}
                		}
                    	
//                    	updateActivityList();
                	}   	
                }
            }
        });
		
//		lblActivitieList = new JLabel("Activity List");
//		lblActivitieList.setHorizontalAlignment(SwingConstants.CENTER);
//		lblActivitieList.setFont(new Font("Arial", Font.PLAIN, 15));
//		lblActivitieList.setBounds(307, 0, 350, 32);
//		panel.add(lblActivitieList);
//		
//		String[][] data = null;
//		NonEditableModel model = new NonEditableModel(data, columnNames);
//		
//		activityTable = new JTable(model);
//		activityTable.setBackground(Color.LIGHT_GRAY);
//		activityTable.setBounds(196, 35, 409, 480);
//		JScrollPane scrollPane = new JScrollPane(activityTable);
//		scrollPane.setBounds(196, 35, 578, 480);
//		panel.add(scrollPane);
//		
//		JLabel lblNewLabel = new JLabel("Activity Details");
//		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 15));
//		lblNewLabel.setBounds(838, 9, 94, 23);
//		panel.add(lblNewLabel);
		
		
		frmProjectManagementSystem.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0.3;
		gbc.weighty = 1.0;
		gbc.gridx = 0;
		gbc.gridy = 0;

		frmProjectManagementSystem.getContentPane().add(panel, gbc);
		gbc.weightx = 0.7;
		gbc.gridx = 1;

		activityEntry = new ActivityEntry(frmProjectManagementSystem);

		frmProjectManagementSystem.add(activityEntry.panel1, gbc);
	}
}
