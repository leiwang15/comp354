package comp354.gui;

import javax.swing.JFrame;

import comp354.Controller.ProjectController;
import comp354.Model.Project;
import comp354.Model.User;

import javax.swing.DefaultListModel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JLabel;

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
	protected JMenuItem mntmLoad;
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
		updateList();
	}

	protected static void updateList() {
		ProjectController pc = new ProjectController();
		pjList = pc.getProjectsByUser(MainWindow.currentUser);

		for(Project p : pjList){
			lm.addElement(p.getProject_name());
		}
	}

	private void initialize() {
		frmProjectManagementSystem = new JFrame();
		frmProjectManagementSystem.setTitle("Project Management System");
		frmProjectManagementSystem.setBounds(100, 100, 800, 600);
		frmProjectManagementSystem.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//		frmProjectManagementSystem.addFocusListener(new FocusAdapter() {
//
//	        @Override
//	        public void focusGained(FocusEvent aE) {
//	        	ProjectController pc = new ProjectController();
//	    		pjList = pc.getProjectsByUser(MainWindow.currentUser);
//
//	    		for(Project p : pjList){
//	    			lm.addElement(p.getProject_name());
//	    		}
//	        }
//	    });

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


		//Load
//		mntmLoad = new JMenuItem("Load");
//		mnFile.add(mntmLoad);
//
//		mntmLoad.addActionListener(new ActionListener(){
//			public void actionPerformed(ActionEvent e){
//				JFileChooser fc = new JFileChooser("D:\\my projects\\COMP354-Yixuan\\");
//				fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
//
//				int returnVal = fc.showOpenDialog(frmProjectManagementSystem);
//
//	            if (returnVal == JFileChooser.APPROVE_OPTION) {
//	                File file = fc.getSelectedFile();
//	                String path = file.getPath();
//	                ProjectController pc = new ProjectController(path);
//	                Project p = pc.getProjectFromFile(file);
//
//	                currentProject = p;
//	                lblPJName.setText(p.getProject_name());
//	                lblPJOwner.setText(p.getOwner());
//	                lblStartDate.setText(p.getStart_date());
//	                lblDescription.setText(p.getProject_desc());
//	            }
//			}
//		});



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
        });

	}
}
