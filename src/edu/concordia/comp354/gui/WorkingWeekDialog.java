package edu.concordia.comp354.gui;

import edu.concordia.comp354.model.ProjectManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by joao on 15.08.11.
 */
public class WorkingWeekDialog extends JDialog {

    private final JPanel contentPanel = new JPanel();

    /**
     * Create the dialog.
     */
    public WorkingWeekDialog() {
        setBounds(100, 100, 282, 243);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        setTitle("Configure Working Week");
        contentPanel.setLayout(null);
        {
            JLabel lblWorkingWeek = new JLabel("Working Week:");
            lblWorkingWeek.setBounds(6, 84, 93, 16);
            contentPanel.add(lblWorkingWeek);
        }

        JCheckBox[] dayBoxes = new JCheckBox[7];

        dayBoxes[0] = new JCheckBox("Monday");
        dayBoxes[0].setBounds(123, 34, 93, 23);
        contentPanel.add(dayBoxes[0]);

        dayBoxes[1] = new JCheckBox("Tuesday");
        dayBoxes[1].setBounds(123, 56, 93, 23);
        contentPanel.add(dayBoxes[1]);

        dayBoxes[2] = new JCheckBox("Wednesday");
        dayBoxes[2].setBounds(123, 80, 117, 23);
        contentPanel.add(dayBoxes[2]);

        dayBoxes[3] = new JCheckBox("Thursday");
        dayBoxes[3].setBounds(123, 104, 93, 23);
        contentPanel.add(dayBoxes[3]);

        dayBoxes[4] = new JCheckBox("Friday");
        dayBoxes[4].setBounds(123, 126, 93, 23);
        contentPanel.add(dayBoxes[4]);

        dayBoxes[5] = new JCheckBox("Saturday");
        dayBoxes[5].setBounds(123, 149, 93, 23);
        contentPanel.add(dayBoxes[5]);

        dayBoxes[6] = new JCheckBox("Sunday");
        dayBoxes[6].setBounds(123, 13, 93, 23);
        contentPanel.add(dayBoxes[6]);

        List<String> workingDays = ProjectManager.getWorkingWeek();
        for (int i = 0; i < 7; i++) {
            if (workingDays.contains(dayBoxes[i].getText().toUpperCase())) {
                dayBoxes[i].setSelected(true);
            } else {
                dayBoxes[i].setSelected(false);
            }
        }

        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("OK");
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
                okButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ArrayList<String> workingDays = new ArrayList<>();
                        for (int i = 0; i < 7; i++) {
                            if (dayBoxes[i].isSelected()) {
                                workingDays.add(dayBoxes[i].getText());
                            }
                        }

                        ProjectManager.setWorkingWeek(workingDays);
                        WorkingWeekDialog.this.dispose();
                    }
                });
            }
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        WorkingWeekDialog.this.dispose();
                    }
                });
            }
        }

    }

}
