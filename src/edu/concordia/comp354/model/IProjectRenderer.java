package edu.concordia.comp354.model;

import java.util.List;

/**
 * Created by joao on 15.07.18.
 */
public interface IProjectRenderer {

    void setProjectList();

    void projectSelected(String projectName);

    void populateUsers();

    void setCurrentUser(User currentUser);
}
