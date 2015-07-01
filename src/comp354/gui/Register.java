package comp354.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import java.awt.Choice;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;

import comp354.Controller.UserController;
import comp354.Model.User;

public class Register {

	protected JDialog userRegister;
	private JTextField newUsername;
	private JTextField newLastname;
	private JTextField newFirstname;
	private JPasswordField newPW;
	private JPasswordField newPWConfirm;


	public Register() {
		initialize();
	}


	private void initialize() {
		userRegister = new JDialog();
		userRegister.setTitle("Register");
		userRegister.setBounds(100, 100, 230, 284);
		userRegister.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		userRegister.setResizable(false);
		userRegister.getContentPane().setLayout(null);

		JLabel lblUserRegistration = new JLabel("User Registration");
		lblUserRegistration.setBounds(57, 10, 102, 15);
		userRegister.getContentPane().add(lblUserRegistration);

		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(22, 49, 76, 15);
		userRegister.getContentPane().add(lblUsername);

		JLabel lblLastName = new JLabel("Last name:");
		lblLastName.setBounds(22, 74, 71, 15);
		userRegister.getContentPane().add(lblLastName);

		JLabel lblFirstName = new JLabel("First name:");
		lblFirstName.setBounds(22, 99, 71, 15);
		userRegister.getContentPane().add(lblFirstName);

		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(22, 124, 76, 15);
		userRegister.getContentPane().add(lblPassword);

		JLabel lblConfirm = new JLabel("Confirm PW:");
		lblConfirm.setBounds(22, 149, 76, 15);
		userRegister.getContentPane().add(lblConfirm);

		newUsername = new JTextField();
		newUsername.setBounds(96, 46, 97, 21);
		userRegister.getContentPane().add(newUsername);
		newUsername.setColumns(10);

		newLastname = new JTextField();
		newLastname.setBounds(96, 71, 97, 21);
		userRegister.getContentPane().add(newLastname);
		newLastname.setColumns(10);

		newFirstname = new JTextField();
		newFirstname.setBounds(96, 96, 97, 21);
		userRegister.getContentPane().add(newFirstname);
		newFirstname.setColumns(10);

		newPW = new JPasswordField();
		newPW.setBounds(96, 121, 97, 21);
		userRegister.getContentPane().add(newPW);
		newPW.setColumns(10);

		newPWConfirm = new JPasswordField();
		newPWConfirm.setBounds(96, 146, 97, 21);
		userRegister.getContentPane().add(newPWConfirm);
		newPWConfirm.setColumns(10);

		JLabel lblRole = new JLabel("Role");
		lblRole.setBounds(22, 174, 54, 15);
		userRegister.getContentPane().add(lblRole);

		final Choice choice = new Choice();
		choice.setBounds(96, 173, 97, 21);
		userRegister.getContentPane().add(choice);
		choice.add("Project Manager");
		choice.add("Project Member");

		JButton btnRegister = new JButton("Register");
		btnRegister.setBounds(22, 212, 87, 23);
		userRegister.getContentPane().add(btnRegister);

		//actionListener for Register button
		btnRegister.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String userName = newUsername.getText();
				String lastName = newLastname.getText();
				String firstName = newFirstname.getText();
				String password = new String(newPW.getPassword());
				String pwConfirm = new String(newPWConfirm.getPassword());
				String role = choice.getSelectedItem();


				if(password.equals(pwConfirm)){
					User u = new User(firstName, lastName, role, userName, password);

					JOptionPane.showMessageDialog(null, "User created successfully!");

					userRegister.dispose();

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
				else{
					JOptionPane.showMessageDialog(null, "Passwords do not match!");
				}
			}
		});

		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(113, 212, 80, 23);
		userRegister.getContentPane().add(btnCancel);

		//actionListener for Cancel button
		btnCancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				userRegister.dispose();

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
	}




}
