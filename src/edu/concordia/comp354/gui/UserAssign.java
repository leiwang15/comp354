package edu.concordia.comp354.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;

import edu.concordia.comp354.controller.ActivityController;
import edu.concordia.comp354.controller.UserController;
import edu.concordia.comp354.model.User;

public class UserAssign {

	protected JDialog userAssign;
	private JList assignList;
	private JList userList;
	private JLabel lblAssign;
	private JLabel lblUserList;
	private JLabel lblAssignedList;
	private List<User> list;


	public UserAssign() {
		initialize();
	}


	private void initialize() {
		//get all project members
		UserController uc = new UserController();
		String s = "Project Member";
		list = uc.getUserByRole(s);

		userAssign = new JDialog();
		userAssign.setTitle("Assign User to Activity");
		userAssign.setBounds(100, 100, 333, 358);
		userAssign.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		userAssign.setResizable(false);
		userAssign.getContentPane().setLayout(null);

		final DefaultListModel lm1 = new DefaultListModel<>();
		userList = new JList(lm1);
		userList.setBackground(Color.LIGHT_GRAY);
		userList.setBounds(10, 67, 93, 197);
		userAssign.getContentPane().add(userList);

		final DefaultListModel lm2 = new DefaultListModel<>();
		assignList = new JList(lm2);
		assignList.setBackground(Color.LIGHT_GRAY);
		assignList.setBounds(214, 67, 93, 197);
		userAssign.getContentPane().add(assignList);

		//add users to the two lists
		for(User u : list){
			ActivityController ac2 = new ActivityController();
		    List<Integer> list1 = ac2.getUserByAssignment(MainDialogWindow.selectedAct);
		    if(list1.contains(u.getUser_id())){
		    	lm2.addElement(u.getUserName());
		    }
		    else{
		    	lm1.addElement(u.getUserName());
		    }
		}

		//choose users to assign
		JButton btnAdd = new JButton("=>");
		btnAdd.setBounds(129, 111, 54, 22);
		userAssign.getContentPane().add(btnAdd);

		btnAdd.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(userList.getSelectedValue() != null){
            		String s = userList.getSelectedValue().toString();
                	lm2.addElement(s);
                	lm1.removeElement(userList.getSelectedValue());
            	}
			}
		});

		JButton btnDelete = new JButton("<=");
		btnDelete.setBounds(129, 196, 54, 22);
		userAssign.getContentPane().add(btnDelete);

		btnDelete.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(assignList.getSelectedValue() != null){
            		String s = assignList.getSelectedValue().toString();
                	lm1.addElement(s);
                	lm2.removeElement(assignList.getSelectedValue());
            	}
			}
		});

		//labels
		lblAssign = new JLabel("Activity ");
		lblAssign.setHorizontalAlignment(SwingConstants.CENTER);
		lblAssign.setFont(new Font("Arial", Font.PLAIN, 15));
		lblAssign.setBounds(10, 10, 297, 22);
		lblAssign.setText("Activity " + MainDialogWindow.selectedAct.getActivity_name());
		userAssign.getContentPane().add(lblAssign);

		lblUserList = new JLabel("User List");
		lblUserList.setHorizontalAlignment(SwingConstants.CENTER);
		lblUserList.setBounds(10, 42, 93, 15);
		userAssign.getContentPane().add(lblUserList);

		lblAssignedList = new JLabel("Assign List");
		lblAssignedList.setHorizontalAlignment(SwingConstants.CENTER);
		lblAssignedList.setBounds(214, 42, 93, 15);
		userAssign.getContentPane().add(lblAssignedList);

		//confirm user assign
		JButton btnConfirm = new JButton("Confirm");
		btnConfirm.setBounds(37, 287, 93, 23);
		userAssign.getContentPane().add(btnConfirm);

		btnConfirm.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//get all selected usernames
				ListModel m = assignList.getModel();
				List<String> s = new ArrayList<String>();
				for(int i = 0; i < m.getSize(); i++){
				     s.add(m.getElementAt(i).toString());
				}

				//get all users by their names

				List<User> l = new ArrayList<User>();
				for(String str : s){
					UserController uc = new UserController();
					l.add(uc.getUserByUserName(str));					}

				//assign the users to the activity
				ActivityController ac2 = new ActivityController();
				List<Integer> list1 = ac2.getUserByAssignment(MainDialogWindow.selectedAct);

				for(Integer i : list1){
					ActivityController ac = new ActivityController();
					ac.removeAssignment(MainDialogWindow.selectedAct, i);
				}

				for(User u : l){
					ActivityController ac = new ActivityController();
					ac.assignUserToActivity(u, MainDialogWindow.selectedAct);
				}


				userAssign.dispose();

				MainDialogWindow.updateUserList();
			}
		});

		//cancel user assign
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(180, 287, 93, 23);
		userAssign.getContentPane().add(btnCancel);

		btnCancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				userAssign.dispose();
			}
		});

	}
}
