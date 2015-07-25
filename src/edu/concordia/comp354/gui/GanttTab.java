package edu.concordia.comp354.gui;

import edu.concordia.comp354.gui.editors.IntegerEditor;
import edu.concordia.comp354.gui.editors.PredecessorEditor;
import org.jdesktop.swingx.table.DatePickerCellEditor;
import sun.swing.table.DefaultTableCellHeaderRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GanttTab extends ActivityEntry  {

    public GanttTab(MainWindow mainWindow) {
        super(mainWindow);
    }

    protected void initializeTab() {

        setName("Gantt");
//        setLayout(new BorderLayout());

//        JPanel parentContainer = getParentWindow().getParentContainer(this);

        JPanel parentContainer = new JPanel();
        getParentWindow().tabbedPane.addTab(getName(), null, this, null);
//        tabbedPane.addTab(panel.getName(), null, panel, null);
//        parentPanel.add(panel);

//        parentContainer = this;

        parentContainer.setLayout(new BorderLayout());

        Dimension dimension = new Dimension(150, 600);
        parentContainer.setMaximumSize(dimension);
        parentContainer.setMinimumSize(dimension);
        parentContainer.setPreferredSize(dimension);
//        Dimension dimension = new Dimension(150, 600);
//        this.setLayout(new BorderLayout());
//        this.setMaximumSize(dimension);
//        this.setMinimumSize(dimension);
//        this.setPreferredSize(dimension);

//        parentContainer.addTab("Gantt", null, panel, null);
//        parentContainer.add(this);
    }

    @Override
    public void gainFocus() {
    }

    protected void createEntryColumns() {
        tableRows = new Object[PMTable.MAX_TABLE_SIZE][];
        columnNames = new String[]{PMTable.ID, PMTable.NAME, PMTable.DURATION, PMTable.START, PMTable.FINISH, PMTable.PREDECESSORS};
        dtm = new PMTableModelGantt(tableRows, columnNames, getProjectManager());

        dtm.clear();

        DefaultTableColumnModel scm = new DefaultTableColumnModel();

        activitiesTable = new PMTable(dtm, scm, this);
        activitiesTable.setCellSelectionEnabled(true);
        activitiesTable.addMouseListener(new PopClickListener());

        activitiesTable.createDefaultColumnsFromModel();

        activitiesTable.getColumn(PMTable.DURATION).setCellEditor(new IntegerEditor(1, PMTable.MAX_TABLE_SIZE, this));
        activitiesTable.getColumn(PMTable.START).setCellEditor(new DatePickerCellEditor(new SimpleDateFormat(DATE_FORMAT)));
        activitiesTable.getColumn(PMTable.FINISH).setCellEditor(new DatePickerCellEditor(new SimpleDateFormat(DATE_FORMAT)));
        activitiesTable.getColumn(PMTable.PREDECESSORS).setCellEditor(new PredecessorEditor(this));

        JTableHeader header = activitiesTable.getTableHeader();
        header.setPreferredSize(new Dimension(10, 38));

        activitiesTable.getColumn(PMTable.ID).setHeaderRenderer(new DefaultTableCellHeaderRenderer());
        activitiesTable.getColumn(PMTable.DURATION).setHeaderRenderer(new DefaultTableCellHeaderRenderer());
        activitiesTable.getColumn(PMTable.START).setHeaderRenderer(new DefaultTableCellHeaderRenderer());
        activitiesTable.getColumn(PMTable.FINISH).setHeaderRenderer(new DefaultTableCellHeaderRenderer());
        activitiesTable.getColumn(PMTable.PREDECESSORS).setHeaderRenderer(new DefaultTableCellHeaderRenderer());

        activitiesTable.getColumn(PMTable.ID).sizeWidthToFit();
        activitiesTable.getColumn(PMTable.DURATION).sizeWidthToFit();
        activitiesTable.getColumn(PMTable.START).sizeWidthToFit();
        activitiesTable.getColumn(PMTable.FINISH).sizeWidthToFit();
        activitiesTable.getColumn(PMTable.PREDECESSORS).sizeWidthToFit();

        TableCellRenderer tableCellRenderer = new DefaultTableCellRenderer() {

            SimpleDateFormat f = new SimpleDateFormat(DATE_FORMAT);

            public Component getTableCellRendererComponent(JTable table,
                                                           Object value, boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                if (value instanceof Date) {
                    value = f.format(value);
                }
                return super.getTableCellRendererComponent(table, value, isSelected,
                        hasFocus, row, column);
            }
        };

        activitiesTable.getColumn(PMTable.START).setCellRenderer(tableCellRenderer);
        activitiesTable.getColumn(PMTable.FINISH).setCellRenderer(tableCellRenderer);
    }
}
