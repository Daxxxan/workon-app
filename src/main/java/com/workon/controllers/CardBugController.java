package com.workon.controllers;

import com.workon.utils.HttpRequest;
import com.workon.utils.LoadFXML;
import com.workon.utils.ParseRequestContent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.Objects;

public class CardBugController {

    @FXML
    private Label projectTitleLabel;

    @FXML
    public void initialize() throws Exception{
        projectTitleLabel.setText(CreateProjectController.getProject().getName());
    }

    @FXML
    protected void handleBugsList() throws Exception{
        LoadFXML.loadFXMLInScrollPane("/fxml/bugList.fxml", ProjectsController.getMainPane(), true, true);
    }
}
