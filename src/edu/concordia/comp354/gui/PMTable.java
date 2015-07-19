package edu.concordia.comp354.gui;

import edu.concordia.comp354.model.IActivityEntryRenderer;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableColumnModel;

/**
 * Created by joao on 15.06.06.
 */
public class PMTable extends JTable {

    public static final String START = "Start";
    public static final String FINISH = "Finish";
    protected static final String PREDECESSORS = "Predecessors";
    protected static final String DURATION = "Duration";
    protected static final String NAME = "Name";
    protected static final String ID = "ID";
    protected static final int MAX_TABLE_SIZE = 1024;
    private IActivityEntryRenderer renderer;
    private int maxPredID;

    public PMTable() {
        super();
    }

    public PMTable(PMTableModel dtm, DefaultTableColumnModel scm, IActivityEntryRenderer renderer) {
        super(dtm, scm);
        this.renderer = renderer;

        findMaxPredID(MAX_TABLE_SIZE - 1);

        for (int i = 0; i < maxPredID - 1; i++) {
            addActivity();
        }
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        super.tableChanged(e);

        int col = e.getColumn();
        int row = e.getFirstRow();
        if (col == 0) {

//            setCellSelectionEnabled(true);

            changeSelection(row, 1, false, false);
            requestFocus();
        } else if (row != -1 && getValueAt(row, 0).equals("") &&
                StringUtils.isNotEmpty((CharSequence) getValueAt(row, col))) {
            findMaxPredID(row);
            addActivity();
            dataModel.setValueAt(Integer.toString(maxPredID), row, 0);
        }
    }

    public void addActivity() {
        ((PMTableModel) dataModel).projectManager.getCurrentProject().addActivity();
    }

    private void findMaxPredID(int row) {
        maxPredID = ((PMTableModel) dataModel).getLastID(row) + 1;
    }

    @Override
    public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
        super.changeSelection(rowIndex, columnIndex, toggle, extend);

        renderer.activitySelected(rowIndex);
    }

    public int getMaxPredID() {
        return maxPredID;
    }
}
