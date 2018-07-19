package com.workon.utils;

import com.jfoenix.controls.JFXButton;
import com.workon.controllers.ProjectsController;
import com.workon.plugin.PluginLoader;
import com.workon.utils.parser.AnnotationParser;
import com.workon.utils.parser.NoNull;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ContextMenuHelper {
    /**
     * Attache un menu clique gauche a un bouton pour cloturer un projet
     *
     * @param button
     *        Bouton pour attacher le menu
     * @param menuItemName
     *        nom du MenuItem
     * @param fxmlMainPane
     *        ScrollPane sur laquelle charger le nouveau FXML
     * @param fxmlPath
     *        Chemin vers le FXML a load
     */
    public static void setContextMenuToButton(@NoNull JFXButton button, String menuItemName, String fxmlPath, String fxmlMainPane){
        AnnotationParser.parse(button);
        ContextMenu contextMenu = new ContextMenu();
        MenuItem finished = new MenuItem(menuItemName);
        contextMenu.getItems().add(finished);
        button.setContextMenu(contextMenu);

        finished.setOnAction(event -> {
            try {
                HttpRequest.updateProject(button.getId());
                ProjectsController.getBug().setDisable(true);
                ProjectsController.getMeeting().setDisable(true);
                ProjectsController.getDocumentation().setDisable(true);
                LoadFXML.loadFXMLInScrollPane(fxmlPath, ProjectsController.getProjectListPane(), true, true);
                LoadFXML.loadFXMLInScrollPane(fxmlMainPane, ProjectsController.getMainPane(), true, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Attache un menu clique gauche a un bouton pour supprimer un bug
     *
     * @param button
     *        Bouton pour attacher le menu
     */
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

    /**
     * Attache un menu clique gauche a un bouton pour supprimer une conversation
     *
     * @param button
     *        Bouton pour attacher le menu
     */
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

    /**
     * Attache un menu clique gauche a un bouton pour supprimer un plugin
     *
     * @param button
     *        Bouton pour attacher le menu
     * @param path
     *        Chemin vers le fichier a supprimer
     */
    public static void setContextMenuToButtonToDeletePlugin(@NoNull JFXButton button, Path path){
        AnnotationParser.parse(button);
        ContextMenu contextMenu = new ContextMenu();
        MenuItem finished = new MenuItem("Supprimer un plugin");
        contextMenu.getItems().add(finished);
        button.setContextMenu(contextMenu);

        finished.setOnAction(event -> {
            try {
                Files.delete(path);
                LoadFXML.loadFXMLInScrollPane("/fxml/userPlugin.fxml", ProjectsController.getMainPane(), true, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
