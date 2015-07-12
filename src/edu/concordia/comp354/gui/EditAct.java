package edu.concordia.comp354.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.ListModel;

import edu.concordia.comp354.controller.ActivityController;
import edu.concordia.comp354.model.Activity;
import javax.swing.JList;

import java.awt.Color;

import javax.swing.SwingConstants;

public class EditAct {

	protected JDialog editAct;
	private JTextField newActName;
	private JTextField newActDuration;
	private JList listPre;
	private JList listAct;
	private JTextField newPessi;
	private JTextField newOpt;
	private JTextField newActValue;
	private JTextArea newActDes;
	private final DefaultListModel lm1 = new DefaultListModel<>();
	private final DefaultListModel lm2 = new DefaultListModel<>();
	private static boolean cycleExist = false;

	public EditAct() {
		initialize();
		loadAct();
	}
	public void loadAct(){
		Activity a = MainDialogWindow.selectedAct;
		ActivityController ac = new ActivityController();
		List<Integer> li = ac.getActPrecedence(a.getActivity_id());
		ArrayList<Integer> ai = new ArrayList<Integer>();
		ai.addAll(li);
		a.setPredecessors(ai);

		newActName.setText(a.getActivity_name());
		newActDuration.setText(a.getDuration() + "");
		newPessi.setText(a.getPessimistic() + "");
		newOpt.setText(a.getOptimistic() + "");
		newActValue.setText(a.getValue() + "");
		newActDes.setText(a.getActivity_desc());

		//add predecessors to listPre
		ActivityController ac3 = new ActivityController();
		List<Integer> list3 = ac3.getActPrecedence(MainDialogWindow.selectedAct.getActivity_id());
		for(Integer i : list3){
			ActivityController ac4 = new ActivityController();
			Activity act = ac4.getActByActId(i).get(0);
			lm2.addElement(act.getActivity_name());
		}


		//add available acts to listAct
		ActivityController ac1 = new ActivityController();
		List<Activity> l = ac1.getActByProjectId(MainDialogWindow.selectedProject.getProject_id());

		Iterator<Activity> it = l.iterator();
		while(it.hasNext()){
			Activity act = it.next();

			if(!lm2.contains(act.getActivity_name())){
				cycleExist = false;
				//activity which will cause cycle will not be added to the list
				cycleDetection(act);
				if(!cycleExist){

					lm1.addElement(act.getActivity_name());

				}
			}
		}
		lm1.removeElement(MainDialogWindow.selectedAct.getActivity_name());
	}

	//cycle detection
	private static void cycleDetection(Activity a){

		ActivityController ac2 = new ActivityController();
		List<Integer> pre = ac2.getActPrecedence(a.getActivity_id());

		if(!pre.isEmpty() && !cycleExist){
			Iterator<Integer> it = pre.iterator();
			while(it.hasNext()){
				Integer i = it.next();

				if(i.intValue() == MainDialogWindow.selectedAct.getActivity_id()){
					cycleExist = true;
					return;
				}
				else{
					ActivityController ac = new ActivityController();
					Activity b = ac.getActByActId(i).get(0);
					cycleDetection(b);
				}
			}
		}
	}

	private void initialize() {
		editAct = new JDialog();
		editAct.setTitle("Edit Activity");
		editAct.setBounds(200, 300, 509, 359);
		editAct.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		editAct.setResizable(false);
		editAct.getContentPane().setLayout(null);

		JLabel lblProject = new JLabel("Edit activity for project ");
		lblProject.setBounds(10, 10, 206, 15);
		editAct.getContentPane().add(lblProject);
		lblProject.setText("Edit activity for project " + MainDialogWindow.selectedProject.getProject_name());

		JLabel lblActivityName = new JLabel("Activity name     :");
		lblActivityName.setBounds(10, 46, 114, 22);
		editAct.getContentPane().add(lblActivityName);

		JLabel lblActivityDuration = new JLabel("Activity duration :");
		lblActivityDuration.setBounds(10, 78, 114, 22);
		editAct.getContentPane().add(lblActivityDuration);

		JLabel lblPessi = new JLabel("Pessimistic       :");
		lblPessi.setBounds(10, 110, 114, 22);
		editAct.getContentPane().add(lblPessi);

		JLabel lblOpt = new JLabel("Optimistic        :");
		lblOpt.setBounds(10, 142, 114, 22);
		editAct.getContentPane().add(lblOpt);

		JLabel lblAvtivityValue = new JLabel("Avtivity value    :");
		lblAvtivityValue.setBounds(10, 174, 114, 22);
		editAct.getContentPane().add(lblAvtivityValue);

		JLabel lblActivityDescription = new JLabel("Activity description:");
		lblActivityDescription.setBounds(10, 206, 126, 22);
		editAct.getContentPane().add(lblActivityDescription);

		newActName = new JTextField();
		newActName.setBounds(134, 46, 82, 22);
		editAct.getContentPane().add(newActName);
		newActName.setColumns(10);

		newActDuration = new JTextField();
		newActDuration.setBounds(134, 78, 82, 22);
		editAct.getContentPane().add(newActDuration);
		newActDuration.setColumns(10);

		newPessi = new JTextField();
		newPessi.setBounds(134, 110, 82, 22);
		editAct.getContentPane().add(newPessi);
		newPessi.setColumns(10);

		newOpt = new JTextField();
		newOpt.setBounds(134, 143, 82, 22);
		editAct.getContentPane().add(newOpt);
		newOpt.setColumns(10);

		newActValue = new JTextField();
		newActValue.setBounds(134, 175, 82, 21);
		editAct.getContentPane().add(newActValue);
		newActValue.setColumns(10);

		newActDes = new JTextArea();
		newActDes.setBounds(10, 238, 206, 50);
		editAct.getContentPane().add(newActDes);

		//create activity
		JButton btnCreate = new JButton("Confirm");
		btnCreate.setBounds(10, 298, 93, 23);
		editAct.getContentPane().add(btnCreate);

		btnCreate.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int actID = MainDialogWindow.selectedAct.getActivity_id();
				String newName = newActName.getText();
				int newDuration = Integer.parseInt(newActDuration.getText());
				String newDes = newActDes.getText();
				int projectID = MainDialogWindow.selectedProject.getProject_id();
				ArrayList<Integer> newPredecessors = new ArrayList<Integer>();
				int pessimistic = Integer.parseInt(newPessi.getText());
				int optimistic = Integer.parseInt(newOpt.getText());
				int value = Integer.parseInt(newActValue.getText());
				int progress = MainDialogWindow.selectedAct.getProgress();
				int finished = MainDialogWindow.selectedAct.getFinished();

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
				Activity act = new Activity(actID, projectID, newName, newDes, newDuration,progress,finished, newPredecessors, pessimistic, optimistic, value);
				ActivityController ac = new ActivityController();
				ac.updateActivity(act);
				ActivityController ac1 = new ActivityController();
				ac1.removePrecedence(actID);
				if(!newPredecessors.isEmpty()){
					for(Integer i : newPredecessors){
						ActivityController ac2 = new ActivityController();
			    		ac2.setActPrecedence(actID, i);
					}
				}

				//JOptionPane.showMessageDialog(null, "Activity saved successfully!");
				MainDialogWindow.updateActivityList();
				editAct.dispose();
			}
		});


		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(123, 298, 93, 23);
		editAct.getContentPane().add(btnCancel);

		btnCancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				editAct.dispose();
			}
		});

		JLabel lblNewLabel = new JLabel("Activity List");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(232, 10, 93, 30);
		editAct.getContentPane().add(lblNewLabel);

		JLabel lblPredecessor = new JLabel("Predecessors");
		lblPredecessor.setHorizontalAlignment(SwingConstants.CENTER);
		lblPredecessor.setBounds(399, 10, 93, 30);
		editAct.getContentPane().add(lblPredecessor);


		listAct = new JList(lm1);
		listAct.setBackground(Color.LIGHT_GRAY);
		listAct.setBounds(232, 49, 93, 257);
		editAct.getContentPane().add(listAct);

		listPre = new JList(lm2);
		listPre.setBackground(Color.LIGHT_GRAY);
		listPre.setBounds(399, 49, 93, 257);
		editAct.getContentPane().add(listPre);



		JButton btnAdd = new JButton("=>");
		btnAdd.setBounds(335, 110, 54, 22);
		editAct.getContentPane().add(btnAdd);

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
		editAct.getContentPane().add(btnDelete);

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
