package comp354.pm.gui;

import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;

/**
 * Created by joao on 15.06.06.
 */
public class PMTable extends JTable {

    public PMTable() {
        super();
    }

    public PMTable(PMTableModel dtm, DefaultTableColumnModel scm) {
        super(dtm, scm);
    }

    @Override
    public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {

        int row = getSelectedRow();
        int col = getSelectedColumn();

        super.changeSelection(rowIndex, columnIndex, toggle, extend);

//        System.out.println(row + "," + col);
        if (getSelectedColumn() == 0) {

            setCellSelectionEnabled(true);

            changeSelection(row, 1, false, false);
            requestFocus();
        } else if (row != -1 && getValueAt(row, 0).equals("") &&
                StringUtils.isNotEmpty((CharSequence) getValueAt(row, col))) {
            dataModel.setValueAt(Integer.toString(((PMTableModel) dataModel).getLastID(row) + 1), row, 0);
        }
    }
}
