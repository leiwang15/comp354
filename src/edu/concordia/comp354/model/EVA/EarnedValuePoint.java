package edu.concordia.comp354.model.EVA;

import edu.concordia.comp354.model.DirtyAware;

/**
 * Created by joao on 15.08.06.
 */
public class EarnedValuePoint extends DirtyAware {
    private int dbID;
    private int date;
    private float plannedValue;
    private float earnedValue;
    private float actualCost;
    private int projectID;

    public EarnedValuePoint() {
        super(DirtyLevels.NEW);
    }

    public EarnedValuePoint(EarnedValueAnalysis eva) {
        super(DirtyLevels.NEW);
        computeFromEVA(eva);
    }

    public EarnedValuePoint(int dbID, int projectID, int date, float plannedValue, float earnedValue, float actualCost) {
        super(DirtyLevels.UNTOUCHED);
        this.dbID = dbID;
        this.projectID = projectID;
        this.date = date;
        this.plannedValue = plannedValue;
        this.earnedValue = earnedValue;
        this.actualCost = actualCost;
    }

    public void computeFromEVA(EarnedValueAnalysis eva) {
        date = eva.getDate();
        plannedValue = eva.getPV();
        earnedValue = eva.getEV();
        actualCost = eva.getAC();

        changed();
    }

    public void setDate(int date) {
        if (date != this.date) {
            this.date = date;
            changed();
        }
    }

    public void setPlannedValue(float plannedValue) {
        if (plannedValue != this.plannedValue) {
            this.plannedValue = plannedValue;
            changed();
        }
    }

    public void setEarnedValue(float earnedValue) {
        if (earnedValue != this.earnedValue) {
            this.earnedValue = earnedValue;
            changed();
        }
    }

    public void setActualCost(float actualCost) {
        if (actualCost != this.actualCost) {
            this.actualCost = actualCost;
            changed();
        }
    }

    public int getDate() {
        return date;
    }

    public int getPlannedValue() {
        return (int) plannedValue;
    }

    public int getEarnedValue() {
        return (int) earnedValue;
    }

    public int getActualCost() {
        return (int) actualCost;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {

        if (projectID != this.projectID) {
            this.projectID = projectID;
            changed();
        }
    }

    public int getDBID() {
        return dbID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EarnedValuePoint that = (EarnedValuePoint) o;

        if (dbID != that.dbID) return false;
        if (Float.compare(that.plannedValue, plannedValue) != 0) return false;
        if (Float.compare(that.earnedValue, earnedValue) != 0) return false;
        if (Float.compare(that.actualCost, actualCost) != 0) return false;
        if (projectID != that.projectID) return false;
        return date == that.date;

    }

    @Override
    public int hashCode() {
        int result = dbID;
        result = 31 * result + date;
        result = 31 * result + (plannedValue != +0.0f ? Float.floatToIntBits(plannedValue) : 0);
        result = 31 * result + (earnedValue != +0.0f ? Float.floatToIntBits(earnedValue) : 0);
        result = 31 * result + (actualCost != +0.0f ? Float.floatToIntBits(actualCost) : 0);
        result = 31 * result + projectID;
        return result;
    }

    public void setDBID(int dbID) {
        this.dbID = dbID;
    }
}
