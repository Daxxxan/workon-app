package com.workon.controllers;

import com.jfoenix.controls.JFXTextField;
import com.workon.utils.HttpRequest;
import com.workon.utils.LabelHelper;
import com.workon.utils.LoadFXML;
import com.workon.utils.ParseRequestContent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

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
    public void initialize() {
        String contentBug = HttpRequest.getCurrentBugs();
        String description = ParseRequestContent.getValueOf(Objects.requireNonNull(contentBug), "description");
        String creatorId = ParseRequestContent.getValueOf(contentBug, "creatorId");

        String contentCreator = HttpRequest.getCollaboratorAccount(creatorId);
        String creatorMail;
        if(contentCreator != null){
            creatorMail = ParseRequestContent.getValueOf(Objects.requireNonNull(contentCreator), "email");
        }else{
            creatorMail = " Inconnu ";
        }

        String contentBugMessages = HttpRequest.getBugMessages();
        ArrayList<String> accountsId = ParseRequestContent.getValuesOf(contentBugMessages, "accountId");
        ArrayList<String> messages = ParseRequestContent.getValuesOf(contentBugMessages, "content");

        description = Objects.requireNonNull(description).replace("\\n", " ");

        for(int counter = 0; counter < accountsId.size(); counter++){
            String account = HttpRequest.getCollaboratorAccount(accountsId.get(counter));
            String accountEmail;
            if(account != null){
                accountEmail = ParseRequestContent.getValueOf(account, "email");
            }else{
                accountEmail = " Inconnu ";
            }
            String contentLabel = accountEmail.substring(1, Objects.requireNonNull(accountEmail).length() - 1) + " : "
                    + messages.get(counter);

            Label label = LabelHelper.createLabel(contentLabel, Double.MAX_VALUE , new Font("Book Antiqua", 14), Pos.CENTER_LEFT);
            LabelHelper.setMessageStyle(accountsId.get(counter), label);
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
    protected void handleBugsList() {
        LoadFXML.loadFXMLInScrollPane("/fxml/bugList.fxml", ProjectsController.getMainPane(), true, true);
    }

    @FXML
    protected void handleSendMessage() {
        if(!message.getText().isEmpty()){
            HttpRequest.addMessage(message.getText());
            LoadFXML.loadFXMLInScrollPane("/fxml/bug.fxml", ProjectsController.getMainPane(), true, true);
        }
    }

    @FXML
    protected void handleClose() {
        String str = HttpRequest.updateBug();
        LoadFXML.loadFXMLInScrollPane("/fxml/bugList.fxml", ProjectsController.getMainPane(), true, true);
    }
}
