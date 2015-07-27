package edu.concordia.comp354.gui;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import de.jollyday.Holiday;
import de.jollyday.HolidayManager;
import de.jollyday.ManagerParameters;
import edu.concordia.comp354.gui.editors.IntegerEditor;
import edu.concordia.comp354.gui.editors.PredecessorEditor;
import edu.concordia.comp354.model.Activity;
import edu.concordia.comp354.model.IActivityEntryRenderer;
import net.objectlab.kit.datecalc.common.DateCalculator;
import net.objectlab.kit.datecalc.common.DefaultHolidayCalendar;
import net.objectlab.kit.datecalc.common.HolidayCalendar;
import net.objectlab.kit.datecalc.common.HolidayHandlerType;
import net.objectlab.kit.datecalc.jdk8.LocalDateKitCalculatorsFactory;
import org.apache.commons.lang3.StringUtils;
import org.jdesktop.swingx.table.DatePickerCellEditor;
import sun.swing.table.DefaultTableCellHeaderRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.List;


/**
 * Created by joao on 15.06.05.
 */
public abstract class ActivityEntry extends BaseTab implements IActivityEntryRenderer, ActionListener, ItemListener {
    protected static final String DATE_FORMAT = "yyyy/MM/dd";

    public static final int X_SCALE = 20;
    protected static final int X_GAP = 0;

//    protected JPanel panel;
protected JTable activitiesTable;
    protected JPanel charts;
    private JScrollPane tablePane;
    private JScrollPane chartPane;
    public PMTableModel dtm;
    protected String[] columnNames;

    protected Object[][] tableRows;
    protected mxGraphComponent graphComponent;
    private int previousID = -1;

    public ActivityEntry(MainWindow mainWindow) {
        super(mainWindow);
        getParentWindow().setActivityEntry(this);

        createUIComponents();

        graphComponent = new mxGraphComponent(new mxGraph());

        previousID = -1;
    }

    public PMTable getActivitiesTable() {
        return (PMTable) activitiesTable;
    }

    private void createUIComponents() {

        createEntryColumns();

        activitiesTable.setGridColor(new Color(211, 211, 211));
        activitiesTable.setPreferredScrollableViewportSize(new Dimension(450, 400));
        activitiesTable.setEnabled(true);
        activitiesTable.setDropMode(DropMode.USE_SELECTION);
        activitiesTable.setForeground(new Color(0, 0, 0));
        activitiesTable.setBackground(new Color(255, 255, 255));
        activitiesTable.setIntercellSpacing(new Dimension(1, 1));
        activitiesTable.setSelectionBackground(new Color(202, 202, 202));
        activitiesTable.setSelectionForeground(new Color(0, 0, 0));
        activitiesTable.setShowHorizontalLines(true);
        activitiesTable.setShowVerticalLines(true);
        activitiesTable.setUpdateSelectionOnSort(true);

        tablePane = new JScrollPane(activitiesTable);

        charts = new JPanel(new BorderLayout());

        charts.setForeground(new Color(0, 0, 0));
        charts.setBackground(new Color(238, 238, 238));
        charts.setEnabled(true);


        chartPane = new JScrollPane(charts);
        chartPane.setEnabled(true);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tablePane, chartPane);
        splitPane.setResizeWeight(0);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(330);

//Provide minimum sizes for the two components in the split pane
        Dimension minimumSize = new Dimension(100, 50);
        splitPane.setMinimumSize(minimumSize);
        splitPane.setMaximumSize(new Dimension(-1, -1));

//        panel = new JPanel(new BorderLayout(), false);
//        panel.setSize(new Dimension(640, 480));
//        panel.add(splitPane);
        setLayout(new BorderLayout());
//        setSize(new Dimension(640, 480));
        add(splitPane);


        final InputVerifier iv = new InputVerifier() {

            @Override
            public boolean verify(JComponent input) {
                JTextField field = (JTextField) input;
                if (StringUtils.isNotEmpty(field.getText())) {
                    String tmp = field.getText().replaceAll("[,;]", " ");
                    if (StringUtils.isNumericSpace(tmp)) {
                        for (String s : tmp.split(" ")) {
                            int pred = Integer.parseInt(s);
                            if (pred < 1 || pred > ((PMTable) activitiesTable).getMaxPredID()) {
                                return false;
                            }
                        }
                    }
                }
                return true;
            }

            @Override
            public boolean shouldYieldFocus(JComponent input) {
                boolean valid = verify(input);
                if (!valid) {
                    String errorMsg = "Invalid entry: ID must be between 1 and " + ((PMTable) activitiesTable).getMaxPredID();
                    JOptionPane.showMessageDialog(ActivityEntry.this, errorMsg);
                }
                return valid;
            }

        };
        DefaultCellEditor editor = new DefaultCellEditor(new JTextField()) {
            {
                getComponent().setInputVerifier(iv);
            }

            @Override
            public boolean stopCellEditing() {
                if (!iv.shouldYieldFocus(getComponent())) return false;
                return super.stopCellEditing();
            }

            @Override
            public JTextField getComponent() {
                return (JTextField) super.getComponent();
            }

        };
    }

    protected abstract void createEntryColumns();

    /*
        Code actions for menus here
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand().equals("Delete")) {
            ActivityEntry.this.deleteActivity();
        }

        repaint();
    }

    public void setCPMData(Object[] nonCriticals, Object[] criticals) {
        getProjectManager().getActivityList().graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "lightblue", nonCriticals);
        getProjectManager().getActivityList().graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "orange", criticals);
    }

    public void activitySelected(int id) {
        if (getCurrentProject() != null) {
            if (previousID != -1 && previousID < getCurrentProject().getActivities().size()) {
                getProjectManager().losingDetailFocus(previousID);
            }

            if (id < getCurrentProject().getActivities().size()) {
                getProjectManager().gainingDetailFocus(id);
            } else {
                clearActivityDetails();
            }
        }
        previousID = id;
    }

    @Override
    public int getProjectID() {
        return getCurrentProject().getProject_id();
    }

    @Override
    public void filterByUser(String userName) {
        activitiesTable.repaint();
    }

    @Override
    public boolean isActiveActivity(int id) {
        return getProjectManager().isActiveActivity(id);
    }

    @Override
    public void clearActivityDetails() {
        getProjectManager().getActivityDetailRenderer().setUIDetailsFromActivity(new Activity());

    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        String newline = "\n";
        JMenuItem source = (JMenuItem) (e.getSource());
        String s = "Item event detected."
                + newline
                + "    Event source: " + source.getText()
                + " (an instance of " + getClassName(source) + ")"
                + newline
                + "    New state: "
                + ((e.getStateChange() == ItemEvent.SELECTED) ?
                "selected" : "unselected");
        System.out.println(s + newline);
    }

    // Returns just the class name -- no package info.
    protected String getClassName(Object o) {
        String classString = o.getClass().getName();
        int dotIndex = classString.lastIndexOf(".");
        return classString.substring(dotIndex + 1);
    }

    @Override
    public int getRowHeight() {
        return activitiesTable.getRowHeight();
    }

    @Override
    public int getXScale() {
        return X_SCALE;
    }

    @Override
    public int getXGap() {
        return X_GAP;
    }

    @Override
    public void fillActivityList() {
        dtm.fillActivityList();
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        activitiesTable.setEnabled(enabled);
//        panel.setEnabled(enabled);
    }

    class PopUpDeleteActivity extends JPopupMenu {
        JMenuItem anItem;

        public PopUpDeleteActivity() {
            anItem = new JMenuItem("Delete");
            add(anItem);
            anItem.addActionListener(ActivityEntry.this);
        }
    }

    public class PopClickListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            if (e.isPopupTrigger()) {
                doPop(e);
            }
        }

        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger())
                doPop(e);
        }

        private void doPop(MouseEvent e) {
            PopUpDeleteActivity menu = new PopUpDeleteActivity();

            int row = activitiesTable.rowAtPoint(e.getPoint());
            if (!activitiesTable.isRowSelected(row)) {
                activitiesTable.setRowSelectionInterval(row, row);
            }

            menu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    public boolean isActivitySelected() {
        int row = getSelectedActivityRow();

        return 0 <= row && row <= getCurrentProject().getActivities().size();
    }

    public int getSelectedActivityRow() {
        return activitiesTable.getSelectedRow();
    }
    public abstract void deleteActivity();
}
