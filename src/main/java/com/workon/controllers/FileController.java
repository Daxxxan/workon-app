package com.workon.controllers;

import com.jfoenix.controls.JFXButton;
import com.workon.Main;
import com.workon.utils.*;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;


public class FileController {
    @FXML
    private Label projectTitleLabel;
    @FXML
    private Label listOfFiles;
    @FXML
    private JFXButton importFileButton;
    @FXML
    private VBox vboxFiles;

    private Desktop desktop = Desktop.getDesktop();

    @FXML
    public void initialize() throws Exception {
        projectTitleLabel.setText(CreateProjectController.getProject().getName());
        LabelHelper.setLabel(listOfFiles, "Liste des documents", Pos.CENTER, "#FFFFFF");
        vboxFiles.setSpacing(10);

        StringBuffer files = HttpRequest.getFiles();
        ArrayList<String> filesNames = ParseRequestContent.getValuesOf(files.toString(), "name");
        ArrayList<String> fileDirectory = ParseRequestContent.getValuesOf(files.toString(), "container");

        for(int counter = 0; counter < filesNames.size(); counter++){
            JFXButton fileButton = ButtonHelper.setButton(filesNames.get(counter).substring(1, filesNames.get(counter).length() - 1),
                    fileDirectory.get(counter).substring(1, fileDirectory.get(counter).length() - 1), Double.MAX_VALUE,
                    "-fx-border-color: #000000; " + "-fx-border-radius: 7; " + "-fx-padding: 10px;", Cursor.HAND,
                    new Font("Times New Roman", 16));
            String fileName = filesNames.get(counter).substring(1, filesNames.get(counter).length() - 1);
            String container = fileDirectory.get(counter).substring(1, fileDirectory.get(counter).length() - 1);
            fileButton.setOnAction(event -> {
                try {
                    HttpRequest.downloadFile(container, fileName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            vboxFiles.getChildren().add(fileButton);
        }
    }

    @FXML
    protected void handleImportFileButton() throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir votre fichier");
        File file = fileChooser.showOpenDialog(Main.getMainStage());


        HttpRequestFile httpRequestFile = new HttpRequestFile(
                LoginConnectionController.getPath().concat("Containers/").concat(CreateProjectController.getProject().getId()).concat("/upload")
        );
        httpRequestFile.addFormField("description", "Cool Pictures");

        httpRequestFile.addFilePart("fileUpload", file);

        String response = httpRequestFile.finish();
        System.out.println("SERVER REPLIED:");
        System.out.println("response: " + response);
    }

    private void openFile(File file) {
        try {
            desktop.open(file);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
