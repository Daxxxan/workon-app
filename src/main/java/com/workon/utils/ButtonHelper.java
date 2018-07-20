package com.workon.utils;

import com.jfoenix.controls.JFXButton;
import com.workon.controllers.ConversationListController;
import com.workon.controllers.CreateProjectController;
import com.workon.controllers.LoginConnectionController;
import com.workon.controllers.ProjectsController;
import com.workon.models.Bug;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Objects;

public class ButtonHelper {
    /**
     * Creer un bouton
     *
     * @param title
     *        Nom du bouton
     * @param id
     *        ID du bouton
     * @param width
     *        Taille du bouton
     * @param borderStyle
     *        Style des bordures
     * @param cursor
     *        Curseur lors du passage sur le bouton
     * @param font
     *        Formatage du texte
     * @return JFXButton
     */
    public static JFXButton setButton(String title, String id, Double width, String borderStyle, Cursor cursor, Font font){
        JFXButton button = new JFXButton(title);
        if(id != null){
            button.setId(id);
        }
        if(width != null){
            button.setMaxWidth(width);
        }
        if(borderStyle != null){
            button.setStyle(borderStyle);
        }
        if(cursor != null){
            button.setCursor(cursor);
        }
        if(font != null){
            button.setFont(font);
        }

        return button;
    }

    public static void loadListOfButton(ArrayList<String>iterator, ArrayList<String>iteratorId, String buttonStyle, String buttonType, VBox vboxList){
        for(int counter = 0; counter < iterator.size(); counter++){
            String bugName = iterator.get(counter).substring(1, iterator.get(counter).length() - 1);
            JFXButton button = ButtonHelper.setButton(iterator.get(counter).substring(1, iterator.get(counter).length() - 1),
                    iteratorId.get(counter), Double.MAX_VALUE, buttonStyle, Cursor.HAND,
                    new Font("Book Antiqua", 16));

            if(buttonType != null){
                if(buttonType.equals("bug")){
                    ContextMenuHelper.setContextMenuToButton(button, "Supprimer le bug", null, "/fxml/bugList.fxml", "bug");
                    button.setOnAction(event -> {
                        CreateProjectController.getProject().setCurrentBugId(button.getId());
                        CreateProjectController.getProject().setCurrentBugName(bugName);
                        LoadFXML.loadFXMLInScrollPane("/fxml/bug.fxml", ProjectsController.getMainPane(), true, true);
                    });
                    Bug bug = new Bug(iterator.get(counter).substring(1, iterator.get(counter).length() - 1),
                            iteratorId.get(counter));
                    CreateProjectController.getProject().addBugToArrayList(bug);
                }else if(buttonType.equals("conversation")){
                    ContextMenuHelper.setContextMenuToButton(button, "Quitter la conversation", null, "/fxml/conversationList.fxml", "conversation");

                    int finalCounter = counter;
                    button.setOnAction(event -> {
                        ConversationListController.setSelectedConversation(iteratorId.get(finalCounter));
                        LoadFXML.loadFXMLInScrollPane("/fxml/conversation.fxml", ProjectsController.getMainPane(), true, true);
                    });
                }else if(buttonType.equals("file")){
                    String fileName = iterator.get(counter).substring(1, iterator.get(counter).length() - 1);
                    String container = iteratorId.get(counter).substring(1, iteratorId.get(counter).length() - 1);
                    button.setOnAction(event -> {
                        HttpRequest.downloadFile(container, fileName);
                    });
                }else if(buttonType.equals("project")){
                    ContextMenuHelper.setContextMenuToButton(button, "Clôturer le projet", "/fxml/vboxProject.fxml", "/fxml/createProject.fxml", "project");

                    int finalCounter1 = counter;
                    button.setOnAction(event -> {
                        CreateProjectController.getProject().setId(button.getId());
                        CreateProjectController.getProject().setName(iterator.get(finalCounter1).substring(1, iterator.get(finalCounter1).length() - 1));

                        LoadFXML.loadFXMLInScrollPane("/fxml/addStepsProject.fxml", ProjectsController.getMainPane(), true, true);
                        ProjectsController.getBug().setDisable(false);
                        ProjectsController.getMeeting().setDisable(false);
                        ProjectsController.getDocumentation().setDisable(false);
                    });
                }else if(buttonType.equals("collaborators")){
                    ContextMenuHelper.setContextMenuToButton(button, "Retirer le collaborateur du projet", "/fxml/vboxProject.fxml", "/fxml/addCollaboratorsProject.fxml", "collaborator");
                }
            }
            vboxList.setSpacing(10);
            vboxList.getChildren().add(button);
        }
    }

    /**
     * Set du style d'un message
     *
     * @param button
     *        Bouton a modifier
     * @param accountId
     *        ID de l'emmeteur du message
     */
    public static void setButtonStyle(JFXButton button, String accountId){
        button.setAlignment(Pos.BASELINE_LEFT);
        if(accountId.equals(LoginConnectionController.getUserId())){
            button.setStyle("-fx-background-color: #CACFD2; -fx-background-radius: 10;");
        }else{
            button.setStyle("-fx-background-color: #A9CCE3; -fx-background-radius: 10;");
        }
    }

    /**
     * Creer un bouton
     *
     * @param accountsId
     *        Liste des comptes ayant ecris des messages
     * @param messages
     *        Liste des messages
     * @param vboxMessages
     *        Emplacement des messages
     * @param type
     *        Type de conversation
     */
    public static void loadConversationList(ArrayList<String> accountsId, ArrayList<String> messages, VBox vboxMessages, String type){
        for(int counter = 0; counter < accountsId.size(); counter++){
            String account = null;
            if(type.equals("bug")){
                account = HttpRequest.getCollaboratorAccount(accountsId.get(counter));
            }else if(type.equals("conversation")){
                account = HttpRequest.getAccountFromConversation(ConversationListController.getSelectedConversation(), accountsId.get(counter));
            }
            String accountEmail;
            if(account != null){
                accountEmail = ParseRequestContent.getValueOf(account, "email");
            }else{
                accountEmail = " Inconnu ";
            }
            String contentLabel = accountEmail.substring(1, Objects.requireNonNull(accountEmail).length() - 1) + " : "
                    + messages.get(counter);

            JFXButton button = ButtonHelper.setButton(contentLabel, null, Double.MAX_VALUE, null, Cursor.DEFAULT, new Font("Book Antiqua", 14));
            ButtonHelper.setButtonStyle(button, accountsId.get(counter));
            vboxMessages.getChildren().add(button);
        }
    }
}
