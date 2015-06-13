package comp354.gui;

import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;
import java.text.SimpleDateFormat;

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
    private int maxPredID;

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
            maxPredID = ((PMTableModel) dataModel).getLastID(row) + 1;
            dataModel.setValueAt(Integer.toString(maxPredID), row, 0);
        }
    }

    public int getMaxPredID() {
        return maxPredID;
    }
}
