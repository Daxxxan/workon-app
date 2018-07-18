package com.workon.utils;

import com.jfoenix.controls.JFXButton;
import com.workon.controllers.ProjectsController;
import com.workon.utils.parser.AnnotationParser;
import com.workon.utils.parser.NoNull;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class ContextMenuHelper {
    public static void setContextMenuToButton(@NoNull JFXButton button){
        AnnotationParser.parse(button);
        ContextMenu contextMenu = new ContextMenu();
        MenuItem finished = new MenuItem("Clôturer le projet");
        contextMenu.getItems().add(finished);
        button.setContextMenu(contextMenu);

        finished.setOnAction(event -> {
            try {
                HttpRequest.updateProject(button.getId());
                LoadFXML.loadFXMLInScrollPane("/fxml/vboxProject.fxml", ProjectsController.getProjectListPane(), true, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void setContextMenuToButtonToDeleteBug(@NoNull JFXButton button){
        AnnotationParser.parse(button);
        ContextMenu contextMenu = new ContextMenu();
        MenuItem finished = new MenuItem("Supprimer le bug");
        contextMenu.getItems().add(finished);
        button.setContextMenu(contextMenu);

        finished.setOnAction(event -> {
            try {
                HttpRequest.deleteBug(button.getId());
                LoadFXML.loadFXMLInScrollPane("/fxml/bugList.fxml", ProjectsController.getMainPane(), true, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void setContextMenuToButtonToDeleteConversation(@NoNull JFXButton button){
        AnnotationParser.parse(button);
        ContextMenu contextMenu = new ContextMenu();
        MenuItem finished = new MenuItem("Quitter la conversation");
        contextMenu.getItems().add(finished);
        button.setContextMenu(contextMenu);

        finished.setOnAction(event -> {
            try {
                HttpRequest.deleteConversation(button.getId());
                LoadFXML.loadFXMLInScrollPane("/fxml/conversationList.fxml", ProjectsController.getMainPane(), true, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
