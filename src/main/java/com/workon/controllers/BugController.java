package com.workon.controllers;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.workon.utils.HttpRequest;
import com.workon.utils.LabelHelper;
import com.workon.utils.LoadFXML;
import com.workon.utils.ParseRequestContent;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class BugController {

    @FXML
    private Label projectTitleLabel;
    @FXML
    private Label bugName;
    @FXML
    private Label descriptionLabel;
    @FXML
    private VBox vboxDescription;
    @FXML
    private VBox vboxMessages;
    @FXML
    private JFXTextField message;

    @FXML
    public void initialize() throws Exception {
        StringBuffer contentBug = HttpRequest.getCurrentBugs();
        String description = ParseRequestContent.getValueOf(Objects.requireNonNull(contentBug).toString(), "description");
        String creatorId = ParseRequestContent.getValueOf(contentBug.toString(), "creatorId");

        StringBuffer contentCreator = HttpRequest.getCollaboratorAccount(creatorId);
        String creatorMail = ParseRequestContent.getValueOf(Objects.requireNonNull(contentCreator).toString(), "email");

        StringBuffer contentBugMessages = HttpRequest.getBugMessages();
        ArrayList<String> accountsId = ParseRequestContent.getValuesOf(contentBugMessages.toString(), "accountId");
        ArrayList<String> messages = ParseRequestContent.getValuesOf(contentBugMessages.toString(), "content");

        description = description.replace("\\n", " ");

        for(int counter = 0; counter < accountsId.size(); counter++){
            StringBuffer account = HttpRequest.getCollaboratorAccount(accountsId.get(counter));
            String accountEmail = ParseRequestContent.getValueOf(account.toString(), "email");

            String contentLabel = accountEmail.substring(1, accountEmail.length() - 1) + " : "
                    + messages.get(counter);

            Label label = LabelHelper.createLabel(contentLabel, Double.MAX_VALUE, new Font("Times New Roman", 14), Pos.CENTER_LEFT);
            vboxMessages.getChildren().add(label);
        }

        vboxDescription.setSpacing(10);
        vboxMessages.setSpacing(10);
        projectTitleLabel.setText(CreateProjectController.getProject().getName());
        bugName.setText("Nom: " + CreateProjectController.getProject().getCurrentBugName() + ", créé par: " + creatorMail.substring(1, creatorMail.length() - 1));
        descriptionLabel.setText(description.substring(1, description.length() - 1));
        descriptionLabel.setWrapText(true);
    }
    @FXML
    protected void handleBugsList() throws Exception{
        LoadFXML.loadFXMLInScrollPane("/fxml/bugList.fxml", ProjectsController.getMainPane(), true, true);
    }

    @FXML
    protected void handleSendMessage() throws Exception{
        if(!message.getText().isEmpty()){
            HttpRequest.addMessage(message.getText());
            LoadFXML.loadFXMLInScrollPane("/fxml/bug.fxml", ProjectsController.getMainPane(), true, true);
        }
    }
}