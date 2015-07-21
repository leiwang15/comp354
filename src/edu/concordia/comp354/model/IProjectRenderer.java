package edu.concordia.comp354.model;

import java.util.List;

/**
 * Created by joao on 15.07.18.
 */
public interface IProjectRenderer {

    void setProjectList();

    void setCurrentProject(int i);

    void populateUsers();

    void setCurrentUser(User currentUser);
}
