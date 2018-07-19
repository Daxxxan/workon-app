package com.workon.utils;

import com.jfoenix.controls.JFXButton;
import com.workon.controllers.ConversationListController;
import com.workon.controllers.CreateProjectController;
import com.workon.controllers.ProjectsController;
import com.workon.models.Bug;
import javafx.scene.Cursor;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.ArrayList;

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
                    ContextMenuHelper.setContextMenuToButtonToDeleteBug(button);
                    button.setOnAction(event -> {
                        CreateProjectController.getProject().setCurrentBugId(button.getId());
                        CreateProjectController.getProject().setCurrentBugName(bugName);
                        LoadFXML.loadFXMLInScrollPane("/fxml/bug.fxml", ProjectsController.getMainPane(), true, true);
                    });
                    Bug bug = new Bug(iterator.get(counter).substring(1, iterator.get(counter).length() - 1),
                            iteratorId.get(counter));
                    CreateProjectController.getProject().addBugToArrayList(bug);
                }else if(buttonType.equals("conversation")){
                    ContextMenuHelper.setContextMenuToButtonToDeleteConversation(button);

                    int finalCounter = counter;
                    button.setOnAction(event -> {
                        ConversationListController.setSelectedConversation(iteratorId.get(finalCounter));
                        LoadFXML.loadFXMLInScrollPane("/fxml/conversation.fxml", ProjectsController.getMainPane(), true, true);
                    });
                }else if(buttonType.equals("file")){
                    String fileName = iterator.get(counter).substring(1, iterator.get(counter).length() - 1);
                    String container = iteratorId.get(counter).substring(1, iteratorId.get(counter).length() - 1);
                    button.setOnAction(event -> {
                        try {
                            HttpRequest.downloadFile(container, fileName);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }else if(buttonType.equals("project")){
                    ContextMenuHelper.setContextMenuToButton(button, "Clôturer le projet", "/fxml/vboxProject.fxml", "/fxml/createProject.fxml");

                    int finalCounter1 = counter;
                    button.setOnAction(event -> {
                        CreateProjectController.getProject().setId(button.getId());
                        CreateProjectController.getProject().setName(iterator.get(finalCounter1).substring(1, iterator.get(finalCounter1).length() - 1));

                        LoadFXML.loadFXMLInScrollPane("/fxml/addStepsProject.fxml", ProjectsController.getMainPane(), true, true);
                        ProjectsController.getBug().setDisable(false);
                        ProjectsController.getMeeting().setDisable(false);
                        ProjectsController.getDocumentation().setDisable(false);
                    });
                }
            }
            vboxList.setSpacing(10);
            vboxList.getChildren().add(button);
        }
    }
}
