package gui;

import javax.swing.JFrame;

import Controller.ProjectController;
import Model.Project;
import Model.User;

import javax.swing.DefaultListModel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.EventQueue;

import javax.swing.SwingConstants;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.JList;

public class MainWindow {
	
	protected static User currentUser;
	protected static Project currentProject;
	
	protected JMenuItem mntmNewProject;
	protected JMenuItem mntmDelete;
	protected JMenuItem mntmSave;
	protected JMenuItem mntmLogOut;
	protected JMenuItem mntmExit;
	
	protected JLabel lblPJName;
	protected JLabel lblStartDate;
	protected JLabel lblEndDate;
	protected JLabel lblDescription;
	
	protected JFrame frmProjectManagementSystem;
	protected JList list;
	protected static DefaultListModel lm;
	protected static List<Project> pjList;
	private JLabel lblProjectList;
	
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

	private void initialize() {
		frmProjectManagementSystem = new JFrame();
		frmProjectManagementSystem.setTitle("Project Management System");
		frmProjectManagementSystem.setBounds(100, 100, 800, 600);
		frmProjectManagementSystem.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
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
		
		
		//Delete		
		mntmDelete = new JMenuItem("Delete");
		mnFile.add(mntmDelete);
		
		mntmDelete.addActionListener(new ActionListener(){
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
		
		
		
		//Save
		mntmSave = new JMenuItem("Save");
		mnFile.add(mntmSave);
		
		mntmSave.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
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

		
		
		frmProjectManagementSystem.getContentPane().setLayout(null);
		
		JLabel lblWelcomeBack = new JLabel("Welcome back " + currentUser.getUserName() + "    User type: " + currentUser.getRole());
		lblWelcomeBack.setBounds(0, 0, 784, 541);
		lblWelcomeBack.setVerticalAlignment(SwingConstants.BOTTOM);
		frmProjectManagementSystem.getContentPane().add(lblWelcomeBack);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel.setBounds(0, 0, 200, 525);
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
                	}   	
                }
            }
        });
		
	}
}
