package edu.concordia.comp354.gui.EVA;

import edu.concordia.comp354.gui.PMTableModel;
import edu.concordia.comp354.model.EVA.EarnedValuePoint;
import edu.concordia.comp354.model.ProjectManager;

import java.util.List;
import java.util.Vector;

/**
 * Created by joao on 15.07.25.
 */
public class PMTableModelEVA extends PMTableModel {
    protected static final int DATE_COL = 2;
    protected static final int PV_COL = 3;
    protected static final int AC_COL = 4;

    public PMTableModelEVA(Object[][] data, Object[] columnNames, ProjectManager projectManager) {
        super(data, columnNames, projectManager);
    }

    public PMTableModelEVA(Vector data, Vector columnNames, ProjectManager projectManager) {
        super(data, columnNames, projectManager);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column > 2;
    }

    public void readForm() {

        List<EarnedValuePoint> points = projectManager.getCurrentProject().getEVAPoints();

        for (int i = 0; i < points.size(); i++) {
            points.get(i).setActualCost((Integer) getValueAt(i, AC_COL));
        }
    }
}
