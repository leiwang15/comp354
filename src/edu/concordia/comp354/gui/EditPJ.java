package edu.concordia.comp354.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import edu.concordia.comp354.controller.ProjectController;
import edu.concordia.comp354.model.Project;
import edu.concordia.comp354.model.ProjectManager;

public class EditPJ {

	private final ProjectManager projectManager;
	protected JDialog editPJ;
	private JTextField newPJName;
	private JTextField newStartDate;
	private JTextField newEndDate;
	private JTextArea description;


	public EditPJ(ProjectManager projectManager) {
		this.projectManager = projectManager;
		initialize();
		loadPJ();
	}

	private void loadPJ(){
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		newPJName.setText(projectManager.getCurrentProject().getProject_name());
		newStartDate.setText(projectManager.getCurrentProject().getStart_date().format(df));
//		newEndDate.setText(projectManager.getCurrentProject().getEnd_date().format(df));
		description.setText(projectManager.getCurrentProject().getProject_desc());
	}

	private void initialize() {
		editPJ = new JDialog();
		editPJ.setTitle("New Project");
		editPJ.setBounds(100, 100, 351, 328);
		editPJ.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		editPJ.setResizable(false);
		editPJ.getContentPane().setLayout(null);

		JLabel lblCreateNewProject = new JLabel("Edit Project");
		lblCreateNewProject.setFont(new Font("Arial", Font.PLAIN, 15));
		lblCreateNewProject.setBounds(105, 10, 132, 34);
		editPJ.getContentPane().add(lblCreateNewProject);

		JLabel lblProjectName = new JLabel("Project Name:");
		lblProjectName.setBounds(43, 63, 91, 22);
		editPJ.getContentPane().add(lblProjectName);

		newPJName = new JTextField();
		newPJName.setBounds(159, 63, 98, 22);
		editPJ.getContentPane().add(newPJName);
		newPJName.setColumns(10);

		JLabel lblNewLabel = new JLabel("Start Date:");
		lblNewLabel.setBounds(43, 95, 91, 22);
		editPJ.getContentPane().add(lblNewLabel);

		newStartDate = new JTextField();
		newStartDate.setBounds(159, 95, 98, 22);
		editPJ.getContentPane().add(newStartDate);
		newStartDate.setColumns(10);

		JButton btnSelectDate = new JButton("...");
		btnSelectDate.setBounds(267, 95, 31, 23);
		editPJ.getContentPane().add(btnSelectDate);

		btnSelectDate.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				final JFrame f = new JFrame();
				newStartDate.setText(new DatePicker(f).setPickedDate());
			}
		});

		JLabel lblNewLabel_1 = new JLabel("Description:");
		lblNewLabel_1.setBounds(43, 159, 91, 22);
		editPJ.getContentPane().add(lblNewLabel_1);

		description = new JTextArea();
		description.setBorder(new LineBorder(Color.LIGHT_GRAY));
		description.setBounds(159, 160, 98, 72);
		editPJ.getContentPane().add(description);
//
//		JLabel lblEndDate = new JLabel("End Date:");
//		lblEndDate.setBounds(43, 127, 98, 22);
//		editPJ.getContentPane().add(lblEndDate);
//
//		newEndDate = new JTextField();
//		newEndDate.setBounds(159, 127, 98, 21);
//		editPJ.getContentPane().add(newEndDate);
//		newEndDate.setColumns(10);
//
//		JButton btnSelectDate2 = new JButton("...");
//		btnSelectDate2.setBounds(267, 127, 31, 22);
//		editPJ.getContentPane().add(btnSelectDate2);
//
//		btnSelectDate2.addActionListener(new ActionListener(){
//			public void actionPerformed(ActionEvent e){
//				final JFrame f = new JFrame();
//				newEndDate.setText(new DatePicker(f).setPickedDate());
//			}
//		});

		JButton btnCreate = new JButton("Save");
		btnCreate.setBounds(55, 245, 93, 23);
		editPJ.getContentPane().add(btnCreate);

		btnCreate.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int id = projectManager.getCurrentProject().getProject_id();
				String projectName = newPJName.getText();
				String startDate = newStartDate.getText();
				String projectDescription = description.getText();

				DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
				LocalDate start = null;
				LocalDate end = null;
				start = LocalDate.parse(startDate);

				Project p = new Project(id, projectName, projectDescription, start, start, null);
				ProjectController pc = new ProjectController();
				pc.updateProject(p);

				projectManager.loadProjects();
				projectManager.setCurrentProject(p.getProject_name());

				editPJ.dispose();
			}
		});

		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(187, 245, 93, 23);
		editPJ.getContentPane().add(btnCancel);


		btnCancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				editPJ.dispose();
			}
		});
	}
}
