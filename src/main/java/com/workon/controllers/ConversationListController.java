package com.workon.controllers;

import com.jfoenix.controls.JFXButton;
import com.workon.utils.*;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.ArrayList;

public class ConversationListController {
    @FXML
    private Label messageTitleLabel;
    @FXML
    private Label listMessagesLabel;
    @FXML
    private VBox vboxConversationsList;

    private static String selectedConversation;

    @FXML
    public void initialize() throws Exception {
        vboxConversationsList.setSpacing(10);
        LabelHelper.setLabel(messageTitleLabel, "Messagerie", Pos.CENTER, null, new Font("Book Antiqua", 16));
        LabelHelper.setLabel(listMessagesLabel, "Liste des conversations", Pos.CENTER, null, new Font("Book Antiqua", 16));
        String getConversation = HttpRequest.getConversations();
        ArrayList<String>conversations = ParseRequestContent.getValuesOf(getConversation, "name");
        ArrayList<String>ids = ParseRequestContent.getValuesOf(getConversation, "id");

        for(int counter = 0; counter < conversations.size(); counter++){
            JFXButton conversationButton = ButtonHelper.setButton(conversations.get(counter).substring(1, conversations.get(counter).length() - 1),
                    ids.get(counter), Double.MAX_VALUE,
                    "-fx-border-color: #000000; " + "-fx-border-radius: 7; " + "-fx-padding: 10px;"
                    + "-fx-background-color: #A9CCE3", Cursor.HAND,
                    new Font("Book Antiqua", 16));

            vboxConversationsList.getChildren().add(conversationButton);

            ContextMenuHelper.setContextMenuToButtonToDeleteConversation(conversationButton);

            int finalCounter = counter;
            conversationButton.setOnAction(event -> {
                setSelectedConversation(ids.get(finalCounter));
                try {
                    LoadFXML.loadFXMLInScrollPane("/fxml/conversation.fxml", ProjectsController.getMainPane(), true, true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @FXML
    protected void handleCreateConversation() throws Exception{
        LoadFXML.loadFXMLInScrollPane("/fxml/createConversation.fxml", ProjectsController.getMainPane(), true, true);
    }

    public static String getSelectedConversation() {
        return selectedConversation;
    }

    public static void setSelectedConversation(String selectedConversation) {
        ConversationListController.selectedConversation = selectedConversation;
    }
}
