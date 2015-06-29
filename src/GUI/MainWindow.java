package gui;

import javax.swing.JFrame;

import Controller.ActivityController;
import Controller.ProjectController;
import Controller.UserController;
import Model.Activity;
import Model.Project;
import Model.User;

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
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import org.jfree.ui.RefineryUtilities;

import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JList;
import javax.swing.JTable;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

public class MainWindow {
	
	protected static User currentUser;
	protected static Project selectedProject;
	protected static Activity selectedAct;
	
	private JMenuItem mntmNewProject;
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
	
	protected JFrame frmProjectManagementSystem;
	protected JList list;
	protected static DefaultListModel lm;
	protected static List<Project> pjList;
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
	
	
	public MainWindow() {
		initialize();
		
		if(currentUser.getRole().equals("Project Manager")){
			updateProjectList();
		}
		else{
			updateActivityList();
		}
	}

	protected static void updateProjectList() {		
		ProjectController pc = new ProjectController();
		pjList = pc.getProjectsByUser(MainWindow.currentUser);
		
		lm.removeAllElements();
		for(Project p : pjList){
			lm.addElement(p.getProject_name());
		}
	}
	
	protected static void updateActivityList(){
		NonEditableModel tableModel = (NonEditableModel) activityTable.getModel();
		tableModel.setRowCount(0);
		activityTable.repaint();
		List<Activity> actList = new ArrayList<Activity>();
		
		if(currentUser.getRole().equals("Project Manager")){
			ActivityController ac = new ActivityController();
			actList = ac.getActByProjectId(selectedProject.getProject_id());
			for(Activity a : actList){
				String[] s = new String[5];
				s[0] = a.getActivity_name();
				s[1] = a.getDuration() + "";
				
				//getPredecessors
				ActivityController ac1 = new ActivityController();
				List<Integer> list = ac1.getActPrecedence(a.getActivity_id());
				String pre = "";
				if(!list.isEmpty()){
					for(Integer i : list){
						ActivityController ac2 = new ActivityController();
						List<Activity> l = ac2.getActByActId(i);
						for(Activity act : l){
							if(pre == ""){
								pre = pre + act.getActivity_name();
							}
							else{
								pre = pre + ", " + act.getActivity_name();
							}
						}
					}
				}
				s[2] = pre;
				s[3] = a.getProgress() + "";
				s[4] = a.getActivity_desc();
				tableModel.addRow(s);
			}	
		}
		else{
			//get act list
			ActivityController ac = new ActivityController();
			List<Integer> li = ac.getActByUser(currentUser.getUser_id());
			for(Integer i : li){
				ActivityController ac1 = new ActivityController();
				actList.addAll(ac1.getActByActId(i));
			}
			
			//set data for table
			for(Activity a : actList){
				String[] s = new String[6];
				ProjectController pc1 = new ProjectController();
				Project p = pc1.getProjectByID(a.getProject_id());
				
				s[0] = p.getProject_name();
				s[1] = a.getActivity_name();
				s[2] = a.getDuration() + "";
				
				//getPredecessors
				ActivityController ac1 = new ActivityController();
				List<Integer> list = ac1.getActPrecedence(a.getActivity_id());
				String pre = "";
				if(!list.isEmpty()){
					for(Integer i : list){
						ActivityController ac2 = new ActivityController();
						List<Activity> l = ac2.getActByActId(i);
						for(Activity act : l){
							if(pre == ""){
								pre = pre + act.getActivity_name();
							}
							else{
								pre = pre + ", " + act.getActivity_name();
							}
						}
					}
				}
				s[3] = pre;
				s[4] = a.getProgress() + "";
				s[5] = a.getActivity_desc();
				tableModel.addRow(s);
			}
		}
		
		
		
		activityTable.setModel(tableModel);
		tableModel.fireTableDataChanged();
	}
	
	protected static void updateUserList() {
	    //update user list
	    lm2.removeAllElements();
	    ActivityController ac2 = new ActivityController();
	    List<Integer> list = ac2.getUserByAssignment(selectedAct);
	    for(Integer i : list){
	    	UserController uc = new UserController();
	    	User u = uc.getUserByID(i);
	    	lm2.addElement(u.getUserName());
	    }
		
	}
	
	private void initialize() {
		frmProjectManagementSystem = new JFrame();
		frmProjectManagementSystem.setTitle("Project Management System");
		frmProjectManagementSystem.setBounds(100, 100, 988, 591);
		frmProjectManagementSystem.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmProjectManagementSystem.setResizable(false);
		frmProjectManagementSystem.getContentPane().setLayout(null);
		
		JMenuBar menuBar = new JMenuBar();
		frmProjectManagementSystem.setJMenuBar(menuBar);
		
		//menu file
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		if(currentUser.getRole().equals("Project Manager")){
			//New project
			mntmNewProject = new JMenuItem("New Project");
			mnFile.add(mntmNewProject);
			
			mntmNewProject.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {
								CreatePJ window = new CreatePJ();
								window.createPJ.setVisible(true);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				}
			});		
			
			mntmEditProject = new JMenuItem("Edit Project");
			mnFile.add(mntmEditProject);
			
			//Delete		
			mntmDeleteProject = new JMenuItem("Delete Project");
			mnFile.add(mntmDeleteProject);
			
			mntmDeleteProject.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					
					if(list.getSelectedValue() != null){            		            				
	            		int dialogResult = JOptionPane.showConfirmDialog (null, "Are you sure to delete the project " + selectedProject.getProject_name()          																	+ "?","Warning",JOptionPane.YES_NO_OPTION);
	            		if(dialogResult == JOptionPane.YES_OPTION){
	            			
	            			List<Activity> la = new ArrayList<Activity>();
	            			ActivityController ac = new ActivityController();
	            			la = ac.getActByProjectId(selectedProject.getProject_id());
	            			
	            			Iterator<Activity> it = la.iterator();
	            			while(it.hasNext()){
	            				Activity act = it.next();
	            				
	            				ActivityController ac1 = new ActivityController();
								ac.deleteAct(act.getActivity_id());
								
								ActivityController ac2 = new ActivityController();
								ac2.deletePre(act.getActivity_id());
								
								ActivityController ac3 = new ActivityController();
								ac3.deleteAssign(act.getActivity_id());
	            			}
	            			
	            			ProjectController pc = new ProjectController();
		           			pc.deleteProject(selectedProject.getProject_id());
		           			
		            		JOptionPane.showMessageDialog(null, "Delete successfully!");
		            		selectedProject = null;
		            		updateProjectList();
		            		updateActivityList();
	            		}            			
	            	}
					else{
						JOptionPane.showMessageDialog(null, "Please select a project to delete!");
					}
				}
			});
		}
		
		//Log out
		mntmLogOut = new JMenuItem("Log Out");
		mnFile.add(mntmLogOut);
		
		mntmLogOut.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				currentUser = null;
				selectedProject = null;
				frmProjectManagementSystem.dispose();
				
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
		
		mntmExit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				frmProjectManagementSystem.dispose();
			}
		});
		
		//Menu Edit
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		if(currentUser.getRole().equals("Project Manager")){
			//New Activity
			mntmNewAct = new JMenuItem("New Activity");
			mnEdit.add(mntmNewAct);
			
			mntmNewAct.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					
					if(selectedProject != null){
						EventQueue.invokeLater(new Runnable() {
							public void run() {
								try {
									CreateAct window = new CreateAct();
									window.createAct.setVisible(true);
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
			
			mntmDeleteAct.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					
					if(selectedProject != null){
						if(selectedAct != null){
							int dialogResult = JOptionPane.showConfirmDialog (null, "Are you sure to delete the activity " + selectedAct.getActivity_name()
							+ "?","Warning",JOptionPane.YES_NO_OPTION);
							if(dialogResult == JOptionPane.YES_OPTION){
								ActivityController ac = new ActivityController();
								ac.deleteAct(selectedAct.getActivity_id());
								
								ActivityController ac2 = new ActivityController();
								ac2.deletePre(selectedAct.getActivity_id());
								
								ActivityController ac3 = new ActivityController();
								ac3.deleteAssign(selectedAct.getActivity_id());
								
								selectedAct = null;
								JOptionPane.showMessageDialog(null, "Delete successfully!");
								updateActivityList();
							}
						}
						else{
							JOptionPane.showMessageDialog(null, "Please select an activity to delete!");
						}
					}
					else{
						JOptionPane.showMessageDialog(null, "Please select a project to delete an activity!");
					}
				}
			});
			
			mntmEditAct = new JMenuItem("Edit Activity");
			mnEdit.add(mntmEditAct);
			
			mntmEditAct.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					
					if(selectedProject != null){
						if(selectedAct != null){
							EventQueue.invokeLater(new Runnable() {
								public void run() {
									try {
										EditAct window = new EditAct();
										window.editAct.setVisible(true);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							});
						}
						else{
							JOptionPane.showMessageDialog(null, "Please select an activity!");
						}
					}
					else{
						JOptionPane.showMessageDialog(null, "Please select a project!");
					}
				}
			});
			
			//assign user to activity
			mntmAssignUser = new JMenuItem("Assign User");
			mnEdit.add(mntmAssignUser);
			
			mntmAssignUser.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					
					if(!lsm.isSelectionEmpty()){
					
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
					}
					else{
						JOptionPane.showMessageDialog(null, "Please select an activity to assign user!");
					}
				}
			});
			
		
			JMenu mnAnalysis = new JMenu("Analysis");
			menuBar.add(mnAnalysis);
			
			mntmGANTTChart = new JMenuItem("GANTT Chart");
			mnAnalysis.add(mntmGANTTChart);
			
			mntmGANTTChart.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					if(selectedProject != null){
					
						EventQueue.invokeLater(new Runnable() {
							public void run() {
								try {
									final Gantt gantt = new Gantt();
							        gantt.gantt.setVisible(true);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
					}
					else{
						JOptionPane.showMessageDialog(null, "Please select a project!");
					}
				}
			});
			
			mntmPERT = new JMenuItem("PERT Chart");
			mnAnalysis.add(mntmPERT);
			
			mntmCriticalPath = new JMenuItem("Critical Path Analysis");
			mnAnalysis.add(mntmCriticalPath);
			
			mntmEarnedValue = new JMenuItem("Earned Value Analysis");
			mnAnalysis.add(mntmEarnedValue);
		
		}
		else{
			mntmEditAct = new JMenuItem("Edit Activity");
			mnEdit.add(mntmEditAct);
		}
		
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
		lblPJInfo.setFont(new Font("Arial", Font.PLAIN, 15));
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
		lblProjectList.setFont(new Font("Arial", Font.PLAIN, 15));
		lblProjectList.setBackground(Color.GRAY);
		lblProjectList.setHorizontalAlignment(SwingConstants.CENTER);
		lblProjectList.setBounds(10, 164, 180, 23);
		panel.add(lblProjectList);
		
		list = new JList(lm);
		list.setBackground(Color.LIGHT_GRAY);
		list.setBounds(10, 189, 180, 326);
		panel.add(list);
		
		//project list info update
		list.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                	if(list.getSelectedValue() != null){
                		String s = list.getSelectedValue().toString();
                    	
                    	for(Project p : pjList){
                			if (p.getProject_name().equals(s)){
                				selectedProject = p;
                				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                				
            	                lblPJName.setText("Project Name: " + p.getProject_name());
            	                lblStartDate.setText("Start Date  : " + df.format(p.getStart_date()));
            	                lblEndDate.setText("End Date    : " + df.format(p.getEnd_date()));
            	                lblDescription.setText("Description : " + p.getProject_desc());
                			}
                		}
                    	
                    	updateActivityList();
                	}   	
                }
            }
        });
		
		lblActivitieList = new JLabel("Activity List");
		lblActivitieList.setHorizontalAlignment(SwingConstants.CENTER);
		lblActivitieList.setFont(new Font("Arial", Font.PLAIN, 15));
		lblActivitieList.setBounds(307, 0, 350, 32);
		panel.add(lblActivitieList);
		
		String[][] data = null;
		NonEditableModel model;
		if(currentUser.getRole().equals("Project Manager")){
			model = new NonEditableModel(data, columnNames);
		}
		else{
			String[] columnNames = {"Project Name","Activity Name",
		            "Duration",
		            "Predecessors",
		            "Progress",
		            "Description"};
			model = new NonEditableModel(data, columnNames);
		}
		activityTable = new JTable(model);
		activityTable.setBackground(Color.LIGHT_GRAY);
		activityTable.setBounds(196, 35, 409, 480);
		JScrollPane scrollPane = new JScrollPane(activityTable);
		scrollPane.setBounds(196, 35, 578, 480);
		panel.add(scrollPane);
		
		activityTable.setCellSelectionEnabled(true);
		lsm = activityTable.getSelectionModel();
		lsm.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		lsm.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if(activityTable.getSelectedRow() != -1){
					//get selected activity name
					int selectedRow = activityTable.getSelectedRow();
				    selectedRow = activityTable.convertRowIndexToModel(selectedRow);
				    String s = (String)activityTable.getValueAt(selectedRow, 0);
				    
				    //get selected activity by name
				    ActivityController ac = new ActivityController();
				    selectedAct = ac.getActByActName(s);
				    
				    //update activity details
				    lblActValue.setText("Activity Value               :   " + selectedAct.getValue());
				    lblPess.setText("Pessimistic Duration :   " + selectedAct.getPessimistic());
				    lblOpt.setText("Optimistic Duration    :   " + selectedAct.getOptimistic());
				    
				    updateUserList();
				}
			}

			
		});


		
		
		JLabel lblNewLabel = new JLabel("Activity Details");
		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 15));
		lblNewLabel.setBounds(838, 9, 94, 23);
		panel.add(lblNewLabel);
		
		lblActValue = new JLabel("Activity Value       :");
		lblActValue.setBounds(784, 42, 190, 23);
		panel.add(lblActValue);
		
		lblPess = new JLabel("Pessimistic Duration :");
		lblPess.setBounds(784, 75, 190, 23);
		panel.add(lblPess);
		
		lblOpt = new JLabel("Optimistic Duration  :");
		lblOpt.setBounds(784, 108, 190, 23);
		panel.add(lblOpt);
		
		JLabel lblUser = new JLabel("User Assigned");
		lblUser.setFont(new Font("Arial", Font.PLAIN, 15));
		lblUser.setHorizontalAlignment(SwingConstants.CENTER);
		lblUser.setBounds(818, 151, 114, 36);
		panel.add(lblUser);
		
		lm2 = new DefaultListModel();
		listUser = new JList(lm2);
		listUser.setBackground(Color.LIGHT_GRAY);
		listUser.setBounds(794, 189, 180, 326);
		panel.add(listUser);
		
		
	}
}
