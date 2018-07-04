package com.workon.controllers;

import com.jfoenix.controls.JFXButton;
import com.workon.Main;
import com.workon.utils.*;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
            int finalCounter = counter;
            fileButton.setOnAction(event -> {
                try {
                    StringBuffer result = HttpRequest.downloadFile(fileDirectory.get(finalCounter).substring(1, fileDirectory.get(finalCounter).length() - 1),
                            filesNames.get(finalCounter).substring(1, filesNames.get(finalCounter).length() - 1));
                    
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
