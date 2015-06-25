package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.ListModel;

import Controller.ActivityController;
import Model.Activity;
import Model.Project;

import javax.swing.JList;

import java.awt.Color;

import javax.swing.SwingConstants;

public class CreateAct {

	protected JFrame frmNewActivity;
	private JTextField newActName;
	private JTextField newActDuration;
	private JList listPre;


	public CreateAct() {
		initialize();
	}


	private void initialize() {
		frmNewActivity = new JFrame();
		frmNewActivity.setTitle("New Activity");
		frmNewActivity.setBounds(200, 300, 505, 269);
		frmNewActivity.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmNewActivity.setResizable(false);
		frmNewActivity.getContentPane().setLayout(null);
		
		JLabel lblProject = new JLabel("New activity for project ");
		lblProject.setBounds(10, 10, 206, 15);
		frmNewActivity.getContentPane().add(lblProject);
		lblProject.setText("New activity for project " + MainWindow.currentProject.getProject_name());
		
		JLabel lblActivityName = new JLabel("Activity name     :");
		lblActivityName.setBounds(10, 46, 114, 22);
		frmNewActivity.getContentPane().add(lblActivityName);
		
		JLabel lblActivityDuration = new JLabel("Activity duration :");
		lblActivityDuration.setBounds(10, 78, 114, 22);
		frmNewActivity.getContentPane().add(lblActivityDuration);
		
		JLabel lblActivityDescription = new JLabel("Activity description:");
		lblActivityDescription.setBounds(10, 110, 126, 22);
		frmNewActivity.getContentPane().add(lblActivityDescription);
		
		newActName = new JTextField();
		newActName.setBounds(134, 46, 82, 22);
		frmNewActivity.getContentPane().add(newActName);
		newActName.setColumns(10);
		
		newActDuration = new JTextField();
		newActDuration.setBounds(134, 78, 82, 22);
		frmNewActivity.getContentPane().add(newActDuration);
		newActDuration.setColumns(10);
		
		final JTextArea newActDes = new JTextArea();
		newActDes.setBounds(10, 138, 206, 50);
		frmNewActivity.getContentPane().add(newActDes);
		
		JButton btnCreate = new JButton("Create");
		btnCreate.setBounds(10, 198, 93, 23);
		frmNewActivity.getContentPane().add(btnCreate);
		
		btnCreate.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String newName = newActName.getText();
				int newDuration = Integer.parseInt(newActDuration.getText());
				String newDes = newActDes.getText();
				int projectID = MainWindow.currentProject.getProject_id();
				ArrayList<Integer> newPredecessors = new ArrayList<Integer>();;
				
				ListModel m = listPre.getModel();
				List<String> s = new ArrayList<String>();
				for(int i = 0; i < m.getSize(); i++){
				     s.add(m.getElementAt(i).toString());  
				}
				
				if(s != null){
					List<Activity> la = new ArrayList<Activity>();
					
					for(String str : s){
						ActivityController ac = new ActivityController();
						la.add(ac.getActByActName(str));
					}
					
					for(Activity act : la){
						newPredecessors.add(act.getActivity_id());
					}
				}
				Activity act = new Activity(projectID, newName, newDes, newDuration, newPredecessors);
				
				JOptionPane.showMessageDialog(null, "Activity created successfully!");
//				MainWindow.updateActivityList();
				frmNewActivity.dispose();
			}
		});
		
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(123, 198, 93, 23);
		frmNewActivity.getContentPane().add(btnCancel);
		
		btnCancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				frmNewActivity.dispose();
			}
		});
		
		JLabel lblNewLabel = new JLabel("Activity List");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(222, 10, 93, 30);
		frmNewActivity.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Predecessors");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(389, 10, 93, 30);
		frmNewActivity.getContentPane().add(lblNewLabel_1);
		
		final DefaultListModel lm1 = new DefaultListModel<>();
		final JList listAct = new JList(lm1);
		listAct.setBackground(Color.LIGHT_GRAY);
		listAct.setBounds(222, 50, 93, 171);
		frmNewActivity.getContentPane().add(listAct);
		
		final DefaultListModel lm2 = new DefaultListModel<>();
		listPre = new JList(lm2);
		listPre.setBackground(Color.LIGHT_GRAY);
		listPre.setBounds(389, 49, 93, 171);
		frmNewActivity.getContentPane().add(listPre);
		
		ActivityController ac = new ActivityController();
		List<Activity> l = ac.getActByProjectId(MainWindow.currentProject.getProject_id());
		for(Activity a : l){
			lm1.addElement(a.getActivity_name());
		}
		
		
		JButton btnAdd = new JButton("=>");
		btnAdd.setBounds(325, 78, 54, 22);
		frmNewActivity.getContentPane().add(btnAdd);
		
		btnAdd.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(listAct.getSelectedValue() != null){
            		String s = listAct.getSelectedValue().toString();
                	lm2.addElement(s);
                	lm1.removeElement(listAct.getSelectedValue());
            	}
			}
		});
		
		JButton btnDelete = new JButton("<=");
		btnDelete.setBounds(325, 165, 54, 23);
		frmNewActivity.getContentPane().add(btnDelete);
		
		btnDelete.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(listPre.getSelectedValue() != null){
            		String s = listPre.getSelectedValue().toString();
                	lm1.addElement(s);
                	lm2.removeElement(listPre.getSelectedValue());
            	}
			}
		});
		
	}
}
