package comp354;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JCheckBox;

public class WorkingWeek extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			WorkingWeek dialog = new WorkingWeek();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public WorkingWeek() {
		setBounds(100, 100, 282, 243);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblWorkingWeek = new JLabel("Working Week:");
			lblWorkingWeek.setBounds(6, 84, 93, 16);
			contentPanel.add(lblWorkingWeek);
		}
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("Sunday");
		chckbxNewCheckBox.setBounds(123, 13, 93, 23);
		contentPanel.add(chckbxNewCheckBox);
		
		JCheckBox checkBox = new JCheckBox("Monday");
		checkBox.setBounds(123, 34, 93, 23);
		contentPanel.add(checkBox);
		
		JCheckBox chckbxWednesday = new JCheckBox("Wednesday");
		chckbxWednesday.setBounds(123, 80, 117, 23);
		contentPanel.add(chckbxWednesday);
		
		JCheckBox chckbxTuesday = new JCheckBox("Tuesday");
		chckbxTuesday.setBounds(123, 56, 93, 23);
		contentPanel.add(chckbxTuesday);
		
		JCheckBox chckbxThursday = new JCheckBox("Thursday");
		chckbxThursday.setBounds(123, 104, 93, 23);
		contentPanel.add(chckbxThursday);
		
		JCheckBox chckbxFriday = new JCheckBox("Friday");
		chckbxFriday.setBounds(123, 126, 93, 23);
		contentPanel.add(chckbxFriday);
		
		JCheckBox chckbxSaturday = new JCheckBox("Saturday");
		chckbxSaturday.setBounds(123, 149, 93, 23);
		contentPanel.add(chckbxSaturday);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
