package com.workon.controllers;

import com.workon.utils.HttpRequest;
import com.workon.utils.LoadFXML;
import com.workon.utils.ParseRequestContent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.Objects;

public class BugListController {
    @FXML
    private Label projectTitleLabel;

    @FXML
    public void initialize() throws Exception{
        projectTitleLabel.setText(CreateProjectController.getProject().getName());
    }

    @FXML
    public void handleCreateBug() throws Exception{
        LoadFXML.loadFXMLInScrollPane("/fxml/ficheBug.fxml", ProjectsController.getMainPane(), true, true);
    }
}
