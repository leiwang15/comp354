package edu.concordia.comp354.gui;

import javax.swing.*;

import edu.concordia.comp354.controller.ProjectController;
import edu.concordia.comp354.model.Project;

import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class MainDirectWindow extends MainWindow {

	protected JLabel lblPJName;
	protected JLabel lblStartDate;
	protected JLabel lblEndDate;
	protected JLabel lblDescription;

	protected JList list;
	private ActivityEntry activityEntry;

	public MainDirectWindow() {
		initialize();
	}

	protected void initialize() {
		super.initialize();

		initMenus();

		initPanel();
	}

	protected void initMenus() {
		JFrame parentJFrame = getParentJFrame();

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
							window.createPJ.setVisible(true);
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


		//Log out
		mntmLogOut = new JMenuItem("Log Out");
		mnFile.add(mntmLogOut);

		mntmLogOut.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				currentUser = null;
				currentProject = null;
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


//		Dimension dimension = new Dimension(200, 600);
//		panel.setMaximumSize(dimension);
//		panel.setMinimumSize(dimension);
//		panel.setPreferredSize(dimension);
////		panel.setBackground(Color.YELLOW);


//		parentContainer.add(panel, BorderLayout.WEST);

		activityEntry = new ActivityEntry(this);
		activityEntry.activityList.setStartDate(LocalDate.parse("2015-08-31"));

		parentContainer.add(activityEntry.panel1, BorderLayout.CENTER);
	}
}
