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

public class AddCollaboratorToConversationController {
    @FXML
    private VBox vboxCollaborators;

    private ArrayList<JFXTextField> textFieldCollaboratorArray = new ArrayList<>();

    @FXML
    public void initialize(){
        vboxCollaborators.setSpacing(10);
        String accounts = HttpRequest.getAccountsFromConversation(ConversationListController.getSelectedConversation());

        ArrayList<String> emails = ParseRequestContent.getValuesOf(accounts, "email");

        if (emails != null && emails.size() != 0){
            for (String email : emails){
                Label emailLabel = LabelHelper.createLabel(email.substring(1, email.length() - 1)
                        , Double.MAX_VALUE, new Font("Book Antiqua", 14), Pos.CENTER);
                vboxCollaborators.getChildren().add(emailLabel);
            }
        }
    }

    @FXML
    protected void handleValidateCollaborators(){
        for(JFXTextField collaborator : textFieldCollaboratorArray){
            if(!collaborator.getText().isEmpty()){
                HttpRequest.addCollaboratorToConversation(ConversationListController.getSelectedConversation(), collaborator.getText());
            }
        }
        LoadFXML.loadFXMLInScrollPane("/fxml/conversation.fxml", ProjectsController.getMainPane(), true, true);
    }

    @FXML
    protected void handleAddCollaboratorButtonAction(){
        JFXTextField textFieldCollaborator = new JFXTextField();
        textFieldCollaborator.setPromptText("Adresse email du collaborateur");
        setTextFieldCollaboratorArray(textFieldCollaborator);
        vboxCollaborators.getChildren().add(textFieldCollaborator);
    }

    public ArrayList<JFXTextField> getTextFieldCollaboratorArray() {
        return textFieldCollaboratorArray;
    }

    public void setTextFieldCollaboratorArray(JFXTextField textFieldCollaborator) {
        this.textFieldCollaboratorArray.add(textFieldCollaborator);
    }
}
