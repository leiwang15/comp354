package comp354.pm.gui;

import comp354.pm.Controller.DB_Controller;
import comp354.pm.Model.Activity;
import comp354.pm.Model.ActivityList;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by joao on 15.06.05.
 */
public class ActivityEntry extends JFrame implements ActionListener {
    private JPanel panel1;
    private JTable activitiesTable;
    private JPanel charts;
    private JScrollPane tablePane;
    private JScrollPane chartPane;
    PMTableModel dtm;
    private ActivityList activityList;
    String[] columnNames;

    private static JMenuBar menuBar;
    String[][] tableRows;
    private DB_Controller project;


    public ActivityEntry(String name) {
        super(name);

        menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem item = new JMenuItem("New");
        item.addActionListener(this);
        menu.add(item);
        item = new JMenuItem("Open...");
        item.addActionListener(this);
        menu.add(item);
        item = new JMenuItem("Save");
        item.addActionListener(this);
        menu.add(item);
        item = new JMenuItem("Save As...");
        item.addActionListener(this);
        menu.add(item);
        item = new JMenuItem("Close");
        item.addActionListener(this);
        menu.add(item);

        menuBar.add(menu);

        setJMenuBar(menuBar);

        setContentPane(panel1);

        setSize(640, 480);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /*
        get activities from grid input
    */
    public ActivityList getActivities() {
        return this.activityList = dtm.fillActivityList();
    }

    /*
        Set activities in grid
    */
    private void setActivities(ActivityList activityList, boolean update) {
        clear();

        this.activityList = activityList;

        if (update) {

            ArrayList<Activity> activities = activityList.getActivities();
            for (int i = 0; i < activityList.getActivities().size(); i++) {
                Activity activity = activities.get(i);
                tableRows[i] = new String[]{Integer.toString(activity.getActivity_id()), activity.getActivity_name(), Integer.toString(activity.getDuration()), activity.getPredecessors().toString().replaceAll("\\[|\\]", "")};
            }
            dtm.setDataVector(tableRows, columnNames);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new ActivityEntry("ActivityEntry");
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {

        tableRows = new String[1024][];
        columnNames = new String[]{"ID", "Name", "Duration", "Predecessors"};
        dtm = new PMTableModel(tableRows, columnNames);

        clear();

        DefaultTableColumnModel scm = new DefaultTableColumnModel();

        activitiesTable = new PMTable(dtm, scm);
        activitiesTable.setCellSelectionEnabled(true);

        activitiesTable.createDefaultColumnsFromModel();

        tablePane = new JScrollPane(activitiesTable);
        getContentPane().add(tablePane, BorderLayout.CENTER);
    }

    private void clear() {
        tableRows[0] = new String[]{"1"};
        for (int i = 1; i < 1024; i++) {
            tableRows[i] = new String[]{""};
        }

        dtm.setDataVector(tableRows, columnNames);
    }

    /*
        Code actions for menus here
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("New")) {
            doNewProject();
        } else if (e.getActionCommand().equals("Open...")) {
            doOpenProject();
        } else if (e.getActionCommand().equals("Save As...")) {
            doSaveProject();
        } else if (e.getActionCommand().equals("Close")) {
            doCloseProject();
        }

        repaint();
    }

    private void doNewProject() {
        clear();

        ActivityList activities = getActivities();

        activities.getActivities();
    }

    private void doOpenProject() {
        try {
//            File file = getOpenFilename();
            File file = new File("/Users/joao/test.db");

            if (file != null) {

                clear();
                activityList.readFromFile(file);

                setActivities(activityList, true);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void doSaveProject() {
        try {
//            File file = getSaveFilename();
            File file = new File("/Users/joao/test.db");

            if (file != null) {

                getActivities().writeToFile(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doCloseProject() {
        //todo: implement real functionality

        doNewProject();
    }

    private File getOpenFilename() {
        JFileChooser chooser = new JFileChooser();

        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Project", "db");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println("You chose to open this file: " + chooser.getSelectedFile().getAbsolutePath());
        }
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }
        return null;
    }

    private File getSaveFilename() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Project", "db");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println("You chose to save to this file: " + chooser.getSelectedFile().getAbsolutePath());
        }
        String path = chooser.getSelectedFile().getAbsolutePath();
        if (path.endsWith(".db")) {
            return chooser.getSelectedFile();
        }
        return null;
    }
}
