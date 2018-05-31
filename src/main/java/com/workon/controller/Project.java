package com.workon.controller;

import com.workon.utils.HttpRequest;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;

import java.io.IOException;

public class Project {
    @FXML
    private ScrollPane mainScrollPane;

    @FXML
    protected void handleCreateProjectAction() throws IOException{
        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/createProject.fxml"));
        mainScrollPane.setContent(parent);
    }
}
