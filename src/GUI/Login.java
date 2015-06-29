package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.SwingConstants;

import Controller.UserController;
import Model.User;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPasswordField;

public class Login {

	protected JDialog login;
	private JTextField inputUsername;
	private JButton btnLogin;
	private JPasswordField inputPassword;

	public Login() {
		initialize();
	}

	private void initialize() {
		login = new JDialog();
		login.setTitle("Login");
		login.setBounds(100, 100, 202, 212);
		login.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		login.setResizable(false);
		
		JLabel lblProjectManagementSystem = new JLabel("Project Management System");
		lblProjectManagementSystem.setBounds(10, 10, 155, 15);
		login.getContentPane().add(lblProjectManagementSystem);
		
		JLabel lblNewLabel = new JLabel("Username:");
		lblNewLabel.setBounds(10, 48, 62, 15);
		
		inputUsername = new JTextField();
		inputUsername.setBounds(77, 45, 80, 21);
		inputUsername.setColumns(10);
		login.getContentPane().setLayout(null);
		login.getContentPane().add(lblNewLabel);
		login.getContentPane().add(inputUsername);
		
		JLabel lblNewLabel_1 = new JLabel("Password:");
		lblNewLabel_1.setBounds(10, 73, 62, 15);
		login.getContentPane().add(lblNewLabel_1);
			
		inputPassword = new JPasswordField();
		inputPassword.setBounds(77, 73, 80, 21);
		login.getContentPane().add(inputPassword);
		
		btnLogin = new JButton("Login");
		btnLogin.setBounds(10, 131, 69, 23);
		btnLogin.setVerticalAlignment(SwingConstants.BOTTOM);
		login.getContentPane().add(btnLogin);
		
		//actionListener for Login button
		btnLogin.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//get user input
				String inputUN = inputUsername.getText();
				String inputPW = new String(inputPassword.getPassword());
				
				//retrieve user from database
				UserController uc = new UserController();
				User u = uc.getUserByName(inputUN);
				
				//check password if user exists
				if(u != null){
					if(u.getPassWord().equals(inputPW)){
						//pop up a message prompt user that login successfully
						JOptionPane.showMessageDialog(null, "Login successfully!");
						
						//close login window
						login.dispose();
						
						//store user to currentUser
						MainWindow.currentUser = u;
						
						//open main window
						EventQueue.invokeLater(new Runnable() {
							public void run() {
								try {
									MainWindow mainWindow = new MainWindow();
									mainWindow.frmProjectManagementSystem.setVisible(true);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
					}
					else{
						JOptionPane.showMessageDialog(null, "Incorrect Password!");
						
					}
				}
				//pop up a message if user doesn't exist
				else{
					JOptionPane.showMessageDialog(null, "User doesn't exit!");
				}
			}
		});
		
		JButton btnRegister = new JButton("Register");
		btnRegister.setBounds(85, 131, 91, 23);
		login.getContentPane().add(btnRegister);
		
		//actionListener for Register button
		btnRegister.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				login.dispose();
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							Register window = new Register();
							window.userRegister.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
	}
}
