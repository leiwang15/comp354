package comp354.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import comp354.Controller.ActivityController;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;
import javax.swing.JTextArea;

public class EditActByMember extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField newProgress;

	public EditActByMember() {
		setTitle("Edit Activity");
		setBounds(100, 100, 248, 301);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblNewLabel = new JLabel("Edit Activity");
		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 15));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 10, 212, 25);
		contentPanel.add(lblNewLabel);

		JLabel lblActivityProgress = new JLabel("Activity Progress (%):");
		lblActivityProgress.setBounds(20, 46, 142, 25);
		contentPanel.add(lblActivityProgress);

		JLabel lblActivityDescription = new JLabel("Activity Description:");
		lblActivityDescription.setBounds(20, 81, 142, 25);
		contentPanel.add(lblActivityDescription);

		newProgress = new JTextField();
		newProgress.setBounds(162, 45, 60, 25);
		contentPanel.add(newProgress);
		newProgress.setColumns(10);
		newProgress.setText(MainDialogWindow.selectedAct.getProgress() + "");

		final JTextArea newDes = new JTextArea();
		newDes.setBounds(20, 116, 202, 104);
		contentPanel.add(newDes);
		newDes.setText(MainDialogWindow.selectedAct.getActivity_desc());

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Confirm");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);

				okButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						int progress = Integer.parseInt(newProgress.getText());
						String des = newDes.getText();

						ActivityController ac = new ActivityController();
						ac.updateActivity(MainDialogWindow.selectedAct.getActivity_id(), progress, des);

						dispose();
						MainDialogWindow.updateActivityList();
					}
				});

			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);

				cancelButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						dispose();
					}
				});
			}
		}
	}
}
