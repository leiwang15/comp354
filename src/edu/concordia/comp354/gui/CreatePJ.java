package edu.concordia.comp354.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import edu.concordia.comp354.model.Project;
import edu.concordia.comp354.model.ProjectManager;
import org.jdesktop.swingx.JXDatePicker;

public class CreatePJ {

	private final ProjectManager projectManager;
	protected JDialog createPJ;
	private JTextField newPJName;
	private JTextField newStartDate;
	private JTextField newEndDate;


	public CreatePJ(ProjectManager projectManager) {
		this.projectManager = projectManager;
		initialize();
	}

	private void initialize() {
		createPJ = new JDialog();
		createPJ.setTitle("New Project");
		createPJ.setBounds(100, 100, 351, 328);
		createPJ.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		createPJ.setResizable(false);
		createPJ.getContentPane().setLayout(null);

		JLabel lblCreateNewProject = new JLabel("Create New Project");
		lblCreateNewProject.setFont(new Font("Arial", Font.PLAIN, 15));
		lblCreateNewProject.setBounds(105, 10, 132, 34);
		createPJ.getContentPane().add(lblCreateNewProject);

		JLabel lblProjectName = new JLabel("Project Name:");
		lblProjectName.setBounds(43, 63, 91, 22);
		createPJ.getContentPane().add(lblProjectName);

		newPJName = new JTextField();
		newPJName.setBounds(159, 63, 98, 22);
		createPJ.getContentPane().add(newPJName);
		newPJName.setColumns(10);

		JLabel lblNewLabel = new JLabel("Start Date:");
		lblNewLabel.setBounds(43, 95, 91, 22);
		createPJ.getContentPane().add(lblNewLabel);

		JXDatePicker newStartDate = new JXDatePicker();
		newStartDate.setDate(Calendar.getInstance().getTime());
		newStartDate.setFormats(new SimpleDateFormat("yyyy-MM-dd"));

//		newStartDate = new JTextField();
		newStartDate.setBounds(159, 95, 125, 22);
		createPJ.getContentPane().add(newStartDate);
//		newStartDate.setColumns(10);

//		JButton btnSelectDate = new JButton("...");
//		btnSelectDate.setBounds(267, 95, 31, 23);
//		createPJ.getContentPane().add(btnSelectDate);

//		btnSelectDate.addActionListener(new ActionListener(){
//			public void actionPerformed(ActionEvent e){
//				final JFrame f = new JFrame();
////				newStartDate.setText(new DatePicker(f).setPickedDate());
////				newStartDate.setText(new JXDatePicker().getDate().toString());
//
//			}
//		});

		JLabel lblNewLabel_1 = new JLabel("Description:");
		lblNewLabel_1.setBounds(43, 159, 91, 22);
		createPJ.getContentPane().add(lblNewLabel_1);

		final JTextArea description = new JTextArea();
		description.setBorder(new LineBorder(Color.LIGHT_GRAY));
		description.setBounds(159, 160, 98, 72);
		createPJ.getContentPane().add(description);
//
//		JLabel lblEndDate = new JLabel("End Date:");
//		lblEndDate.setBounds(43, 127, 98, 22);
//		createPJ.getContentPane().add(lblEndDate);
//
//		newEndDate = new JTextField();
//		newEndDate.setBounds(159, 127, 98, 21);
//		createPJ.getContentPane().add(newEndDate);
//		newEndDate.setColumns(10);

//		JButton btnSelectDate2 = new JButton("...");
//		btnSelectDate2.setBounds(267, 127, 31, 22);
//		createPJ.getContentPane().add(btnSelectDate2);

//		btnSelectDate2.addActionListener(new ActionListener(){
//			public void actionPerformed(ActionEvent e){
//				final JFrame f = new JFrame();
//				newEndDate.setText(new DatePicker(f).setPickedDate());
//			}
//		});

		JButton btnCreate = new JButton("Create");
		btnCreate.setBounds(55, 245, 93, 23);
		createPJ.getContentPane().add(btnCreate);

		btnCreate.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String projectName = newPJName.getText();
//				String startDate = newStartDate.getText();
				String projectDescription = description.getText();

				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				LocalDate start = null;
				LocalDate end = null;
//				start = LocalDate.parse(startDate);
				start = newStartDate.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

				projectManager.createProject(projectName, projectDescription, start, start);
//				Project p = new Project(MainDirectWindow.currentUser, projectName, projectDescription, start, start);

				//JOptionPane.showMessageDialog(null, "Project created successfully!");
//				MainDirectWindow.updateProjectList();

				createPJ.dispose();
			}
		});

		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(187, 245, 93, 23);
		createPJ.getContentPane().add(btnCancel);


		btnCancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				createPJ.dispose();
			}
		});
	}
}