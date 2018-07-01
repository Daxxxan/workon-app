package com.workon.controllers;

import com.jfoenix.controls.JFXTextArea;
import com.workon.utils.HttpRequest;
import com.workon.utils.LoadFXML;
import com.workon.utils.ParseRequestContent;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.Objects;

public class BugController {

    @FXML
    private Label projectTitleLabel;
    @FXML
    private Label bugName;
    @FXML
    private Label descriptionLabel;

    @FXML
    public void initialize() throws IOException {
        StringBuffer contentBug = HttpRequest.getCurrentBugs();
        String description = ParseRequestContent.getValueOf(Objects.requireNonNull(contentBug).toString(), "description");
        String creatorId = ParseRequestContent.getValueOf(contentBug.toString(), "creatorId");

        StringBuffer contentCreator = HttpRequest.getAccount(creatorId);
        String creatorMail = ParseRequestContent.getValueOf(Objects.requireNonNull(contentCreator).toString(), "email");

        description = description.replace("\\n", " ");

        projectTitleLabel.setText(CreateProjectController.getProject().getName());
        bugName.setText("Bug: " + CreateProjectController.getProject().getCurrentBugName() + ", créé par: " + creatorMail.substring(1, creatorMail.length() - 1));
        descriptionLabel.setText(description.substring(1, description.length() - 1));
        descriptionLabel.setWrapText(true);
    }
    @FXML
    protected void handleBugsList() throws Exception{
        LoadFXML.loadFXMLInScrollPane("/fxml/bugList.fxml", ProjectsController.getMainPane(), true, true);
    }
}
