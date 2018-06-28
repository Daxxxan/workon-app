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
        //Get du projet
        StringBuffer contentProject = HttpRequest.getProject();
        String projectName = ParseRequestContent.getValueOf(Objects.requireNonNull(contentProject).toString(), "name");
        projectTitleLabel.setText(projectName.substring(1, projectName.length() - 1));
    }

    @FXML
    public void handleCreateBug() throws Exception{
        LoadFXML.loadFXMLInScrollPane("/fxml/ficheBug.fxml", ProjectsController.getMainPane(), true, true);
    }
}
