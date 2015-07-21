package edu.concordia.comp354.gui;

import edu.concordia.comp354.controller.ProjectController;
import edu.concordia.comp354.model.Project;
import edu.concordia.comp354.model.ProjectManager;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class EditPJ {

    private final ProjectManager projectManager;
    protected JDialog editPJ;
    private JTextField newPJName;
    private JXDatePicker newStartDate;
    private JTextArea description;


    public EditPJ(ProjectManager projectManager) {
        this.projectManager = projectManager;
        initialize();
        loadPJ();
    }

    private void loadPJ() {

        newPJName.setText(projectManager.getCurrentProject().getProject_name());
        Instant instant = projectManager.getCurrentProject().getStart_date().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        newStartDate.setDate(Date.from(instant));

        description.setText(projectManager.getCurrentProject().getProject_desc());
    }

    private void initialize() {
        editPJ = new JDialog();
        editPJ.setTitle("New Project");
        editPJ.setBounds(100, 100, 351, 328);
        editPJ.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        editPJ.setResizable(false);
        editPJ.getContentPane().setLayout(null);

        JLabel lblCreateNewProject = new JLabel("Edit Project");
        lblCreateNewProject.setFont(new Font("Arial", Font.PLAIN, 15));
        lblCreateNewProject.setBounds(105, 10, 132, 34);
        editPJ.getContentPane().add(lblCreateNewProject);

        JLabel lblProjectName = new JLabel("Project Name:");
        lblProjectName.setBounds(43, 63, 91, 22);
        editPJ.getContentPane().add(lblProjectName);

        newPJName = new JTextField();
        newPJName.setBounds(159, 63, 98, 22);
        editPJ.getContentPane().add(newPJName);
        newPJName.setColumns(10);

        JLabel lblNewLabel = new JLabel("Start Date:");
        lblNewLabel.setBounds(43, 95, 91, 22);
        editPJ.getContentPane().add(lblNewLabel);

        newStartDate = new JXDatePicker();
        newStartDate.setDate(Calendar.getInstance().getTime());
        newStartDate.setFormats(new SimpleDateFormat("yyyy-MM-dd"));

        newStartDate.setBounds(159, 95, 125, 22);
        editPJ.getContentPane().add(newStartDate);

        JLabel lblNewLabel_1 = new JLabel("Description:");
        lblNewLabel_1.setBounds(43, 159, 91, 22);
        editPJ.getContentPane().add(lblNewLabel_1);

        description = new JTextArea();
        description.setBorder(new LineBorder(Color.LIGHT_GRAY));
        description.setBounds(159, 160, 98, 72);
        editPJ.getContentPane().add(description);

        JButton btnCreate = new JButton("Save");
        btnCreate.setBounds(55, 245, 93, 23);
        editPJ.getContentPane().add(btnCreate);

        btnCreate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int id = projectManager.getCurrentProject().getProject_id();
                String projectName = newPJName.getText();
                LocalDate start = newStartDate.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                String projectDescription = description.getText();

                Project p = new Project(id, projectName, projectDescription, start, start, null);
                ProjectController pc = new ProjectController();
                pc.updateProject(p);

                projectManager.loadProjects();
                projectManager.setCurrentProject(p.getProject_name());

                editPJ.dispose();
            }
        });

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setBounds(187, 245, 93, 23);
        editPJ.getContentPane().add(btnCancel);


        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editPJ.dispose();
            }
        });
    }
}
