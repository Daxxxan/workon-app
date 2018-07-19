package com.workon.controllers;

import com.jfoenix.controls.JFXButton;
import com.workon.utils.ButtonHelper;
import com.workon.utils.HttpRequest;
import com.workon.utils.ParseRequestContent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Objects;


public class PluginController {
    @FXML
    private VBox vboxPlugins;

    public void initialize() {
        vboxPlugins.setSpacing(10);
        String plugins = HttpRequest.getFiles("plugins");
        ArrayList<String> filesNames = ParseRequestContent.getValuesOf(plugins, "name");

        for(int counter = 0; counter < filesNames.size(); counter++){
            JFXButton fileButton = ButtonHelper.setButton(filesNames.get(counter).substring(1, filesNames.get(counter).length() - 1),
                    null, Double.MAX_VALUE,
                    "-fx-border-color: #000000; " + "-fx-border-radius: 7; " + "-fx-padding: 10px;"
                    + "-fx-background-color: #D98880", Cursor.HAND,
                    new Font("Book Antiqua", 16));
            String fileName = filesNames.get(counter).substring(1, filesNames.get(counter).length() - 1);

            fileButton.setOnAction(event -> {
                URL url = null;
                try {
                    url = new URL(LoginConnectionController.getPath().concat("Containers/plugins/download/").concat(fileName));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                ReadableByteChannel rbc = null;
                try {
                    rbc = Channels.newChannel(Objects.requireNonNull(url).openStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream("src/main/resources/pluginsWorkon/" + fileName);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    Objects.requireNonNull(fos).getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            vboxPlugins.getChildren().add(fileButton);
        }
    }
}
