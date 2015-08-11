package edu.concordia.comp354.gui.EVA;

import edu.concordia.comp354.gui.PMTableModel;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableColumnModel;

/**
 * Created by joao on 15.08.09.
 */
public class EVATable extends JTable {

    private final EVATab evaTab;

    public EVATable(PMTableModel dtm, DefaultTableColumnModel scm, EVATab evaTab) {
        super(dtm, scm);
        this.evaTab = evaTab;
    }

    @Override
    public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
        super.changeSelection(rowIndex, columnIndex, toggle, extend);

        String dateStr = (String) getValueAt(rowIndex, 1);
        if (dateStr != null) {
            evaTab.getProjectManager().selectEVADate(dateStr);
            evaTab.getProjectManager().evaDateSelected(dateStr);
        }
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        super.tableChanged(e);

        int col = e.getColumn();
        int row = e.getFirstRow();
        if (col == 4 && StringUtils.isNotEmpty((String) getValueAt(row, col))) {
            int ac = Integer.parseInt(((String) getValueAt(row, col)).replace(",", "").replace("$", ""));
            evaTab.getProjectManager().getCurrentProject().getEvaPoints().get(row).setActualCost(ac);
        }
    }
}
