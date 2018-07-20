package com.workon.utils;

import com.jfoenix.controls.JFXButton;
import com.workon.controllers.CreateProjectController;
import com.workon.controllers.LoginConnectionController;
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
     * Attache un menu clique gauche a un bouton
     *
     * @param button
     *        Bouton pour attacher le menu
     * @param menuItemName
     *        nom du MenuItem
     * @param fxmlMainPane
     *        ScrollPane sur laquelle charger le nouveau FXML
     * @param fxmlPath
     *        Chemin vers le FXML a load
     * @param contextType
     *        Type de menu
     */
    public static void setContextMenuToButton(@NoNull JFXButton button, @NoNull String menuItemName, String fxmlPath, @NoNull String fxmlMainPane, @NoNull String contextType){
        AnnotationParser.parse(button, menuItemName, fxmlPath, fxmlMainPane, contextType);
        ContextMenu contextMenu = new ContextMenu();
        MenuItem finished = new MenuItem(menuItemName);
        contextMenu.getItems().add(finished);
        button.setContextMenu(contextMenu);

        if(contextType.equals("project")){
            finished.setOnAction(event -> {
                HttpRequest.updateProject(button.getId());
                ProjectsController.getBug().setDisable(true);
                ProjectsController.getMeeting().setDisable(true);
                ProjectsController.getDocumentation().setDisable(true);
                LoadFXML.loadFXMLInScrollPane(fxmlPath, ProjectsController.getProjectListPane(), true, true);
                LoadFXML.loadFXMLInScrollPane(fxmlMainPane, ProjectsController.getMainPane(), true, true);
            });
        }else if(contextType.equals("bug")){
            finished.setOnAction(event -> {
                HttpRequest.deleteBug(button.getId());
                LoadFXML.loadFXMLInScrollPane(fxmlMainPane, ProjectsController.getMainPane(), true, true);
            });
        }else if(contextType.equals("conversation")){
            finished.setOnAction(event -> {
                HttpRequest.deleteConversation(button.getId());
                LoadFXML.loadFXMLInScrollPane(fxmlMainPane, ProjectsController.getMainPane(), true, true);
            });
        }else if(contextType.equals("collaborator")){
            finished.setOnAction(event -> {
                HttpRequest.removeCollaboratorFromProject(button.getId());
                if(button.getId().equals(LoginConnectionController.getUserId())){
                    ProjectsController.getBug().setDisable(true);
                    ProjectsController.getMeeting().setDisable(true);
                    ProjectsController.getDocumentation().setDisable(true);
                    LoadFXML.loadFXMLInScrollPane("/fxml/createProject.fxml", ProjectsController.getMainPane(), true, true);
                }else{
                    LoadFXML.loadFXMLInScrollPane(fxmlMainPane, ProjectsController.getMainPane(), true, true);
                }
                LoadFXML.loadFXMLInScrollPane(fxmlPath, ProjectsController.getProjectListPane(), true, true);
            });
        }
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
