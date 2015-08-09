package edu.concordia.comp354.controller;

import edu.concordia.comp354.model.EVA.EarnedValuePoint;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EVAController extends DB_Controller {

    public EVAController() {
        super();
    }

    public int add(EarnedValuePoint point) {

        int id = 0;

        String sql = "INSERT INTO EVAPoints (Project_ID,Date,PV,EV,AC) "
                + "VALUES('" + point.getProjectID() + "', '"
                + point.getDate() + "', '"
                + point.getPlannedValue() + "', '" + point.getEarnedValue() + "', '" + point.getActualCost()
                + "');";

        String sql2 = "SELECT last_insert_rowid() FROM EVAPoints;";

        try {
            st = c.createStatement();
            st.executeUpdate(sql);
            st = c.createStatement();
            ResultSet result = st.executeQuery(sql2);
            result.next();
            id = result.getInt(1);
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public void update(EarnedValuePoint point) {

        String sql = "update EVAPoints set Date = '" + point.getDate() +
                "',PV = '" + point.getPlannedValue() + "',EV = " +
                point.getEarnedValue() + ",AC = " + point.getActualCost() +
                " where DBID = " + point.getDBID() + ";";

        try {
            st = c.createStatement();
            st.executeUpdate(sql);
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<EarnedValuePoint> getPoints(int projectID) {

        String sql = "SELECT * FROM EVAPoints "
                + "WHERE Project_ID = " + projectID + ";";

        ResultSet res;
        List<EarnedValuePoint> list = new ArrayList<>();

        int id;
        int date;
        float PV, EV, AC;

        try {
            st = c.createStatement();
            res = st.executeQuery(sql);

            if (res != null) {
                while (res.next()) {
                    id = res.getInt("DBID");
                    date = res.getInt("Date");
                    PV = res.getFloat("PV");
                    EV = res.getFloat("EV");
                    AC = res.getFloat("AC");
                    list.add(new EarnedValuePoint(id, projectID, date, PV, EV, AC));
                }
            }
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void deletePoint(int evID) {
        String sql = "DELETE FROM EVAPoints WHERE DBID = " + evID + ";";
        try {
            st = c.createStatement();
            st.executeUpdate(sql);
            c.close();

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    public void deleteProject(int projectID) {
        String sql = "DELETE FROM EVAPoints WHERE Project_ID = " + projectID + ";";
        try {
            st = c.createStatement();
            st.executeUpdate(sql);
            c.close();

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
}
