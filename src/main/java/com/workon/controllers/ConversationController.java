package com.workon.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.workon.utils.*;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Objects;

public class ConversationController {
    @FXML
    private JFXTextField message;
    @FXML
    private VBox vboxMessages;

    @FXML
    public void initialize() {
        vboxMessages.setSpacing(15);
        String messages = HttpRequest.getMessagesFromConversation(ConversationListController.getSelectedConversation());
        ArrayList<String>accountId = ParseRequestContent.getValuesOf(messages, "accountId");
        ArrayList<String>content = ParseRequestContent.getValuesOf(messages, "content");

        ButtonHelper.loadConversationList(accountId, content, vboxMessages, "conversation");
    }

    @FXML
    protected void handleConversationList() {
        LoadFXML.loadFXMLInScrollPane("/fxml/conversationList.fxml", ProjectsController.getMainPane(), true, true);
    }

    @FXML
    protected void handleSendMessage() {
        if(!message.getText().isEmpty()){
            HttpRequest.addMessageToConversation(message.getText(), ConversationListController.getSelectedConversation());
            LoadFXML.loadFXMLInScrollPane("/fxml/conversation.fxml", ProjectsController.getMainPane(), true, true);
        }
    }

    @FXML
    protected void handleAddCollaborator(){
        LoadFXML.loadFXMLInScrollPane("/fxml/addCollaboratorsToConversation.fxml", ProjectsController.getMainPane(), true, true);
    }
}
