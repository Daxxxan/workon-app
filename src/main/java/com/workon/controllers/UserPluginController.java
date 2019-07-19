package com.workon.controllers;

import com.jfoenix.controls.JFXButton;
import com.workon.plugin.PluginInterface;
import com.workon.plugin.PluginLoader;
import com.workon.utils.ButtonHelper;
import com.workon.utils.ContextMenuHelper;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UserPluginController {
    @FXML
    private VBox vboxPlugins;

    public void initialize(){
        vboxPlugins.setSpacing(10);
        try {
            Files.list(Paths.get("src/main/resources/pluginsWorkon/"))
                    .forEach(path -> {
                        JFXButton fileButton = ButtonHelper.setButton(path.getFileName().toString(),
                                null, Double.MAX_VALUE,
                                "-fx-border-color: #000000; " + "-fx-border-radius: 7; " + "-fx-padding: 10px;"
                                        + "-fx-background-color: #A9CCE3;", Cursor.HAND,
                                new Font("Book Antiqua", 16));

                        ContextMenuHelper.setContextMenuToButtonToDeletePlugin(fileButton, path);

                        fileButton.setOnAction(event -> {
                            PluginLoader pluginLoader = new PluginLoader();
                            PluginInterface pluginInterface = null;
                            try {
                                pluginInterface = (PluginInterface) pluginLoader.loadPlugin(path.getFileName().toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if(pluginInterface != null){
                                pluginInterface.LoadPane(ProjectsController.getMainPane());
                            }
                        });

                        vboxPlugins.getChildren().add(fileButton);
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
