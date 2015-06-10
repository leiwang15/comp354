package GUI;

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

import dbControl.UserController;
import Model.User;

public class Register {

	protected JFrame frmRegister;
	private JTextField newUsername;
	private JTextField newLastname;
	private JTextField newFirstname;
	private JPasswordField newPW;
	private JPasswordField newPWConfirm;


	public Register() {
		initialize();
	}


	private void initialize() {
		frmRegister = new JFrame();
		frmRegister.setTitle("Register");
		frmRegister.setBounds(100, 100, 230, 284);
		frmRegister.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmRegister.getContentPane().setLayout(null);
		
		JLabel lblUserRegistration = new JLabel("User Registration");
		lblUserRegistration.setBounds(57, 10, 102, 15);
		frmRegister.getContentPane().add(lblUserRegistration);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(22, 49, 76, 15);
		frmRegister.getContentPane().add(lblUsername);
		
		JLabel lblLastName = new JLabel("Last name:");
		lblLastName.setBounds(22, 74, 71, 15);
		frmRegister.getContentPane().add(lblLastName);
		
		JLabel lblFirstName = new JLabel("First name:");
		lblFirstName.setBounds(22, 99, 71, 15);
		frmRegister.getContentPane().add(lblFirstName);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(22, 124, 76, 15);
		frmRegister.getContentPane().add(lblPassword);
		
		JLabel lblConfirm = new JLabel("Confirm PW:");
		lblConfirm.setBounds(22, 149, 76, 15);
		frmRegister.getContentPane().add(lblConfirm);		
		
		newUsername = new JTextField();
		newUsername.setBounds(96, 46, 97, 21);
		frmRegister.getContentPane().add(newUsername);
		newUsername.setColumns(10);
		
		newLastname = new JTextField();
		newLastname.setBounds(96, 71, 97, 21);
		frmRegister.getContentPane().add(newLastname);
		newLastname.setColumns(10);
		
		newFirstname = new JTextField();
		newFirstname.setBounds(96, 96, 97, 21);
		frmRegister.getContentPane().add(newFirstname);
		newFirstname.setColumns(10);
		
		newPW = new JPasswordField();
		newPW.setBounds(96, 121, 97, 21);
		frmRegister.getContentPane().add(newPW);
		newPW.setColumns(10);
		
		newPWConfirm = new JPasswordField();
		newPWConfirm.setBounds(96, 146, 97, 21);
		frmRegister.getContentPane().add(newPWConfirm);
		newPWConfirm.setColumns(10);
		
		JLabel lblRole = new JLabel("Role");
		lblRole.setBounds(22, 174, 54, 15);
		frmRegister.getContentPane().add(lblRole);
		
		final Choice choice = new Choice();
		choice.setBounds(96, 173, 97, 21);
		frmRegister.getContentPane().add(choice);
		choice.add("Project Manager");
		choice.add("Project Member");
		
		JButton btnRegister = new JButton("Register");
		btnRegister.setBounds(22, 212, 87, 23);
		frmRegister.getContentPane().add(btnRegister);
		
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
					User u = new User(userName, firstName, lastName, role, password);
					UserController uc = new UserController();
					int i = uc.addUser(u);
					if(i == 1){
						JOptionPane.showMessageDialog(null, "User created successfully!");
						
						frmRegister.dispose();
						
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
					else{
						JOptionPane.showMessageDialog(null, "Failed to create user!");
					}
				}
				else{
					JOptionPane.showMessageDialog(null, "Passwords do not match!");
				}
			}
		});
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(113, 212, 80, 23);
		frmRegister.getContentPane().add(btnCancel);
		
		//actionListener for Cancel button
		btnCancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				frmRegister.dispose();
				
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
	}
	
	
	
	
}
