package GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import dbControl.UserController;
import Model.User;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPasswordField;

public class Login {

	protected JFrame frmLogin;
	private JTextField inputUsername;
	private JButton btnLogin;
	private JPasswordField inputPassword;

	public static void main(String[] args) {
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

	public Login() {
		initialize();
	}

	private void initialize() {
		frmLogin = new JFrame();
		frmLogin.setTitle("Login");
		frmLogin.setBounds(100, 100, 202, 212);
		frmLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblProjectManagementSystem = new JLabel("Project Management System");
		lblProjectManagementSystem.setBounds(10, 10, 155, 15);
		frmLogin.getContentPane().add(lblProjectManagementSystem);
		
		JLabel lblNewLabel = new JLabel("Username:");
		lblNewLabel.setBounds(10, 48, 62, 15);
		
		inputUsername = new JTextField();
		inputUsername.setBounds(77, 45, 80, 21);
		inputUsername.setColumns(10);
		frmLogin.getContentPane().setLayout(null);
		frmLogin.getContentPane().add(lblNewLabel);
		frmLogin.getContentPane().add(inputUsername);
		
		JLabel lblNewLabel_1 = new JLabel("Password:");
		lblNewLabel_1.setBounds(10, 73, 62, 15);
		frmLogin.getContentPane().add(lblNewLabel_1);
			
		inputPassword = new JPasswordField();
		inputPassword.setBounds(77, 73, 80, 21);
		frmLogin.getContentPane().add(inputPassword);
		
		btnLogin = new JButton("Login");
		btnLogin.setBounds(10, 131, 69, 23);
		btnLogin.setVerticalAlignment(SwingConstants.BOTTOM);
		frmLogin.getContentPane().add(btnLogin);
		
		//actionListener for Login button
		btnLogin.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//get user input
				String inputUN = inputUsername.getText();
				String inputPW = new String(inputPassword.getPassword());
				
				//retrieve user from database
				UserController uc = new UserController();
				User u = uc.getUserByID(inputUN);
				
				//check password if user exists
				if(u != null){
					if(u.getPassword().equals(inputPW)){
						//pop up a message prompt user that login successfully
						JOptionPane.showMessageDialog(null, "Login successfully!");
						
						//close login window
						frmLogin.dispose();
						
						//store user to currentUser
						main.currentUser = u;
						
						//open main window
						EventQueue.invokeLater(new Runnable() {
							public void run() {
								try {
									main mainWindow = new main();
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
		frmLogin.getContentPane().add(btnRegister);
		
		//actionListener for Register button
		btnRegister.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				frmLogin.dispose();
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							Register window = new Register();
							window.frmRegister.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
	}
}
