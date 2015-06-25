package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import Model.Project;

public class CreatePJ {

	protected JFrame frame;
	private JTextField newPJName;
	private JTextField newStartDate;
	private JTextField newEndDate;

	
	public CreatePJ() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setTitle("New Project");
		frame.setBounds(100, 100, 351, 328);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setResizable(false);
		frame.getContentPane().setLayout(null);
		
		JLabel lblCreateNewProject = new JLabel("Create New Project");
		lblCreateNewProject.setFont(new Font("Arial", Font.PLAIN, 15));
		lblCreateNewProject.setBounds(105, 10, 132, 34);
		frame.getContentPane().add(lblCreateNewProject);
		
		JLabel lblProjectName = new JLabel("Project Name:");
		lblProjectName.setBounds(43, 63, 91, 22);
		frame.getContentPane().add(lblProjectName);
		
		newPJName = new JTextField();
		newPJName.setBounds(159, 63, 98, 22);
		frame.getContentPane().add(newPJName);
		newPJName.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Start Date:");
		lblNewLabel.setBounds(43, 95, 91, 22);
		frame.getContentPane().add(lblNewLabel);
		
		newStartDate = new JTextField();
		newStartDate.setBounds(159, 95, 98, 22);
		frame.getContentPane().add(newStartDate);
		newStartDate.setColumns(10);
		
		JButton btnSelectDate = new JButton("...");
		btnSelectDate.setBounds(267, 95, 31, 23);
		frame.getContentPane().add(btnSelectDate);
		
		btnSelectDate.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				final JFrame f = new JFrame();				
				newStartDate.setText(new DatePicker(f).setPickedDate());
			}
		});
		
		JLabel lblNewLabel_1 = new JLabel("Description:");
		lblNewLabel_1.setBounds(43, 159, 91, 22);
		frame.getContentPane().add(lblNewLabel_1);
		
		final JTextArea description = new JTextArea();
		description.setBorder(new LineBorder(Color.LIGHT_GRAY));
		description.setBounds(159, 160, 98, 72);
		frame.getContentPane().add(description);
		
		JLabel lblEndDate = new JLabel("End Date:");
		lblEndDate.setBounds(43, 127, 98, 22);
		frame.getContentPane().add(lblEndDate);
		
		newEndDate = new JTextField();
		newEndDate.setBounds(159, 127, 98, 21);
		frame.getContentPane().add(newEndDate);
		newEndDate.setColumns(10);
		
		JButton btnSelectDate2 = new JButton("...");
		btnSelectDate2.setBounds(267, 127, 31, 22);
		frame.getContentPane().add(btnSelectDate2);
		
		btnSelectDate2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				final JFrame f = new JFrame();				
				newEndDate.setText(new DatePicker(f).setPickedDate());
			}
		});
		
		JButton btnCreate = new JButton("Create");
		btnCreate.setBounds(55, 245, 93, 23);
		frame.getContentPane().add(btnCreate);
		
		btnCreate.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String projectName = newPJName.getText();
				String startDate = newStartDate.getText();
				String endDate = newEndDate.getText();
				String projectDescription = description.getText();

				DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
				Date start = null;
				Date end = null;
				try {
					start = df.parse(startDate);
					end = df.parse(endDate);
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				
				Project p = new Project(MainWindow.currentUser, projectName, projectDescription, start, end);
				
				JOptionPane.showMessageDialog(null, "Project created successfully!");
				MainWindow.updateProjectList();
				
				frame.dispose();
			}
		});
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(187, 245, 93, 23);
		frame.getContentPane().add(btnCancel);
		
		
		btnCancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				frame.dispose();
			}
		});
	}
}
