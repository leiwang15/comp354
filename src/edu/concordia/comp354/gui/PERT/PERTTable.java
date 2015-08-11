package edu.concordia.comp354.gui.PERT;

import edu.concordia.comp354.gui.PMTable;
import edu.concordia.comp354.gui.PMTableModel;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableColumnModel;

/**
 * Created by joao on 15.08.11.
 */
public class PERTTable extends PMTable {

    private final PERTTab pertTab;

    public PERTTable(PMTableModel dtm, DefaultTableColumnModel scm, PERTTab pertTab) {
        super(dtm, scm,pertTab);
        this.pertTab = pertTab;
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        super.tableChanged(e);

        int col = e.getColumn();
        int row = e.getFirstRow();
        if (col != -1 &&
                row != -1 &&
                getValueAt(row, PMTableModelPERT.DURATION_COL) != null &&
                getValueAt(row, PMTableModelPERT.OPTIMISTIC_COL) != null &&
                getValueAt(row, PMTableModelPERT.PESSIMISTIC_COL) != null) {

            pertTab.fillActivityList();
            pertTab.setActivities(true);
            pertTab.getProjectManager().activityChanged();
        }
    }
}
