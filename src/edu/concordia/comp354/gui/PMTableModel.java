package edu.concordia.comp354.gui;

import edu.concordia.comp354.model.ProjectManager;

import javax.swing.table.DefaultTableModel;
import java.util.Vector;

/**
 * Created by joao on 15.06.05.
 */
public abstract class PMTableModel extends DefaultTableModel {

    protected static final int ID_COL = 0;
    protected final Object[] columnNames;
    protected final ProjectManager projectManager;

    public PMTableModel(Object[][] data, Object[] columnNames, ProjectManager projectManager) {
        super(data, columnNames);

        this.columnNames = columnNames;
        this.projectManager = projectManager;
    }

    /**
     * Constructs a <code>DefaultTableModel</code> and initializes the table
     * by passing <code>data</code> and <code>columnNames</code>
     * to the <code>setDataVector</code> method.
     *
     * @param data        the data of the table, a <code>Vector</code>
     *                    of <code>Vector</code>s of <code>Object</code>
     *                    values
     * @param columnNames <code>vector</code> containing the names
     *                    of the new columns
     * @see #getDataVector
     * @see #setDataVector
     */
    public PMTableModel(Vector data, Vector columnNames, ProjectManager projectManager) {
        super(data, columnNames);
        this.columnNames = columnNames.toArray();

        this.projectManager = projectManager;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column != 0;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        for (int j = 0; j < getRowCount(); j++) {
            for (int i = 0; i < getColumnCount(); i++) {
                sb.append(getValueAt(j, i)).append("\t");
            }
            sb.append("\n");
        }
        return "";
    }

    public int getLastID(int currentRow) {
        for (int j = currentRow; j >= 0; j--) {
            String s = (String) getValueAt(j, ID_COL);
            if (!s.equals("")) {
                return Integer.parseInt(s);
            }
        }
        return 0;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 2:
                return Integer.class;
            default:
                return String.class;
        }
    }

    public void clear() {

        Object[][] tableRows = new Object[PMTable.MAX_TABLE_SIZE][];
        tableRows[0] = new String[]{""};
        for (int i = 1; i < PMTable.MAX_TABLE_SIZE; i++) {
            tableRows[i] = new String[]{""};
        }

        setDataVector(tableRows, columnNames);
    }

    public abstract void readForm();
}
