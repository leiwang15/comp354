package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.ListModel;

import Controller.ActivityController;
import Model.Activity;
import javax.swing.JList;

import java.awt.Color;

import javax.swing.SwingConstants;

public class CreateAct {

	protected JDialog createAct;
	private JTextField newActName;
	private JTextField newActDuration;
	private JList listPre;
	private JList listAct;
	private JTextField newPessi;
	private JTextField newOpt;
	private JTextField newActValue;

	public CreateAct() {
		initialize();
	}

	private void initialize() {
		createAct = new JDialog();
		createAct.setTitle("New Activity");
		createAct.setBounds(200, 300, 509, 359);
		createAct.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		createAct.setResizable(false);
		createAct.getContentPane().setLayout(null);
		
		JLabel lblProject = new JLabel("New activity for project ");
		lblProject.setBounds(10, 10, 206, 15);
		createAct.getContentPane().add(lblProject);
		lblProject.setText("New activity for project " + MainWindow.selectedProject.getProject_name());
		
		JLabel lblActivityName = new JLabel("Activity name     :");
		lblActivityName.setBounds(10, 46, 114, 22);
		createAct.getContentPane().add(lblActivityName);
		
		JLabel lblActivityDuration = new JLabel("Activity duration :");
		lblActivityDuration.setBounds(10, 78, 114, 22);
		createAct.getContentPane().add(lblActivityDuration);
		
		JLabel lblPessi = new JLabel("Pessimistic       :");
		lblPessi.setBounds(10, 110, 114, 22);
		createAct.getContentPane().add(lblPessi);
		
		JLabel lblOpt = new JLabel("Optimistic        :");
		lblOpt.setBounds(10, 142, 114, 22);
		createAct.getContentPane().add(lblOpt);
		
		JLabel lblAvtivityValue = new JLabel("Avtivity value    :");
		lblAvtivityValue.setBounds(10, 174, 114, 22);
		createAct.getContentPane().add(lblAvtivityValue);
		
		JLabel lblActivityDescription = new JLabel("Activity description:");
		lblActivityDescription.setBounds(10, 206, 126, 22);
		createAct.getContentPane().add(lblActivityDescription);
		
		newActName = new JTextField();
		newActName.setBounds(134, 46, 82, 22);
		createAct.getContentPane().add(newActName);
		newActName.setColumns(10);
		
		newActDuration = new JTextField();
		newActDuration.setBounds(134, 78, 82, 22);
		createAct.getContentPane().add(newActDuration);
		newActDuration.setColumns(10);
		
		newPessi = new JTextField();
		newPessi.setBounds(134, 110, 82, 22);
		createAct.getContentPane().add(newPessi);
		newPessi.setColumns(10);
		
		newOpt = new JTextField();
		newOpt.setBounds(134, 143, 82, 22);
		createAct.getContentPane().add(newOpt);
		newOpt.setColumns(10);
		
		newActValue = new JTextField();
		newActValue.setBounds(134, 175, 82, 21);
		createAct.getContentPane().add(newActValue);
		newActValue.setColumns(10);
		
		final JTextArea newActDes = new JTextArea();
		newActDes.setBounds(10, 238, 206, 50);
		createAct.getContentPane().add(newActDes);
		
		//create activity
		JButton btnCreate = new JButton("Create");
		btnCreate.setBounds(10, 298, 93, 23);
		createAct.getContentPane().add(btnCreate);
		
		btnCreate.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String newName = newActName.getText();
				int newDuration = Integer.parseInt(newActDuration.getText());
				String newDes = newActDes.getText();
				int projectID = MainWindow.selectedProject.getProject_id();
				ArrayList<Integer> newPredecessors = new ArrayList<Integer>();
				int pessimistic = Integer.parseInt(newPessi.getText());
				int optimistic = Integer.parseInt(newOpt.getText());
				int value = Integer.parseInt(newActValue.getText());
				
				//get all selected acitivities
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
				Activity act = new Activity(projectID, newName, newDes, newDuration, newPredecessors, pessimistic, optimistic, value);
				
				//JOptionPane.showMessageDialog(null, "Activity created successfully!");
				MainWindow.updateActivityList();
				createAct.dispose();
			}
		});
		
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(123, 298, 93, 23);
		createAct.getContentPane().add(btnCancel);
		
		btnCancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				createAct.dispose();
			}
		});
		
		JLabel lblNewLabel = new JLabel("Activity List");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(232, 10, 93, 30);
		createAct.getContentPane().add(lblNewLabel);
		
		JLabel lblPredecessor = new JLabel("Predecessors");
		lblPredecessor.setHorizontalAlignment(SwingConstants.CENTER);
		lblPredecessor.setBounds(399, 10, 93, 30);
		createAct.getContentPane().add(lblPredecessor);
		
		final DefaultListModel lm1 = new DefaultListModel<>();
		listAct = new JList(lm1);
		listAct.setBackground(Color.LIGHT_GRAY);
		listAct.setBounds(232, 49, 93, 257);
		createAct.getContentPane().add(listAct);
		
		final DefaultListModel lm2 = new DefaultListModel<>();
		listPre = new JList(lm2);
		listPre.setBackground(Color.LIGHT_GRAY);
		listPre.setBounds(399, 49, 93, 257);
		createAct.getContentPane().add(listPre);
		
		//add available acts to list
		ActivityController ac = new ActivityController();
		List<Activity> l = ac.getActByProjectId(MainWindow.selectedProject.getProject_id());
		
		for(Activity a : l){
			lm1.addElement(a.getActivity_name());
		}
		
		JButton btnAdd = new JButton("=>");
		btnAdd.setBounds(335, 110, 54, 22);
		createAct.getContentPane().add(btnAdd);
		
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
		btnDelete.setBounds(335, 238, 54, 23);
		createAct.getContentPane().add(btnDelete);
		
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
