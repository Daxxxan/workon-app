package com.workon.controllers;

import com.jfoenix.controls.JFXTextField;
import com.workon.utils.HttpRequest;
import com.workon.utils.LoadFXML;
import com.workon.utils.ParseRequestContent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class CreateConversationController {
    @FXML
    private VBox vboxCollaborators;
    @FXML
    private JFXTextField conversationNameTextField;

    private ArrayList<JFXTextField> textFieldCollaboratorArray = new ArrayList<>();

    public void initialize(){
        vboxCollaborators.setSpacing(10);
        vboxCollaborators.setStyle("-fx-padding: 5px");
    }

    @FXML
    protected void handleAddCollaboratorButtonAction() {
        JFXTextField textFieldCollaborator = new JFXTextField();
        textFieldCollaborator.setPromptText("Adresse email du collaborateur");
        setTextFieldCollaboratorArray(textFieldCollaborator);
        vboxCollaborators.getChildren().add(textFieldCollaborator);
    }

    @FXML
    protected void handleValidateConversation() {
        if(!conversationNameTextField.getText().isEmpty()){
            String conversation = HttpRequest.createConversation(conversationNameTextField.getText());
            String conversationId = ParseRequestContent.getValueOf(conversation, "id");
            for(JFXTextField collaborator : textFieldCollaboratorArray){
                if(!collaborator.getText().isEmpty()){
                    HttpRequest.addCollaboratorToConversation(conversationId, collaborator.getText());
                    LoadFXML.loadFXMLInScrollPane("/fxml/conversationList.fxml", ProjectsController.getMainPane(), true, true);
                }
            }
        }
    }

    public ArrayList<JFXTextField> getTextFieldCollaboratorArray() {
        return textFieldCollaboratorArray;
    }

    public void setTextFieldCollaboratorArray(JFXTextField textFieldCollaborator) {
        this.textFieldCollaboratorArray.add(textFieldCollaborator);
    }
}
