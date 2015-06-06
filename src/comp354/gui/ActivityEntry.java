package comp354.gui;

import comp354.ActivityList;
import pm.Model.Activity;

import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        item = new JMenuItem("Save as...");
        item.addActionListener(this);
        menu.add(item);

        menuBar.add(menu);

        setJMenuBar(menuBar);

        setContentPane(panel1);

        setSize(640, 480);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /*
        Code actions for menus here
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("New")) {
            clear();
        }

        repaint();
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

    /*
        get activities from grid input
    */
    public ActivityList getActivities() {
        return this.activityList = dtm.fillActivityList();
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
}
