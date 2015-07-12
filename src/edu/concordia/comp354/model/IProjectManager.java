package edu.concordia.comp354.model;

/**
 * Created by joao on 15.07.12.
 */
public interface IProjectManager {

    ActivityList getActivities();
    void setActivities(ActivityList list, boolean update);
}