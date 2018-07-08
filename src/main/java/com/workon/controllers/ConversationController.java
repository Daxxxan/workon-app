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

public class ConversationController {
    @FXML
    private JFXTextField message;
    @FXML
    private VBox vboxMessages;

    @FXML
    public void initialize() throws Exception{
        vboxMessages.setSpacing(10);
        String messages = HttpRequest.getMessagesFromConversation(ConversationListController.getSelectedConversation());
        ArrayList<String>accountId = ParseRequestContent.getValuesOf(messages, "accountId");
        ArrayList<String>content = ParseRequestContent.getValuesOf(messages, "content");
        System.out.println(accountId + " : " + content);
        for(int counter = 0; counter < accountId.size(); counter++){
            String account = HttpRequest.getAccountFromConversation(ConversationListController.getSelectedConversation(), accountId.get(counter));
            System.out.println("account: " + account);
            String email = ParseRequestContent.getValueOf(account, "email");

            String contentLabel = email + " : " +
                    content.get(counter).substring( 1 , content.get(counter).length() - 1);

            Label label = LabelHelper.createLabel(contentLabel, Double.MAX_VALUE, new Font("Times New Roman", 14), Pos.CENTER_LEFT);
            vboxMessages.getChildren().add(label);
        }
    }

    @FXML
    protected void handleConversationList() throws Exception{
        LoadFXML.loadFXMLInScrollPane("/fxml/conversationList.fxml", ProjectsController.getMainPane(), true, true);
    }

    @FXML
    protected void handleSendMessage() throws Exception{
        if(!message.getText().isEmpty()){
            HttpRequest.addMessageToConversation(message.getText(), ConversationListController.getSelectedConversation());
            LoadFXML.loadFXMLInScrollPane("/fxml/conversation.fxml", ProjectsController.getMainPane(), true, true);
        }
    }
}
