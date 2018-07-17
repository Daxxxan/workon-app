package com.workon.controllers;

import com.jfoenix.controls.JFXButton;
import com.workon.Main;
import com.workon.utils.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

import java.io.File;
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

    @FXML
    public void initialize() throws Exception {
        projectTitleLabel.setText(CreateProjectController.getProject().getName());
        LabelHelper.setLabel(listOfFiles, "Liste des documents", Pos.CENTER, "#FFFFFF", new Font("Book Antiqua", 16));
        vboxFiles.setSpacing(10);

        String files = HttpRequest.getFiles(CreateProjectController.getProject().getId());
        ArrayList<String> filesNames = ParseRequestContent.getValuesOf(files, "name");
        ArrayList<String> fileDirectory = ParseRequestContent.getValuesOf(files, "container");

        for(int counter = 0; counter < filesNames.size(); counter++){
            JFXButton fileButton = ButtonHelper.setButton(filesNames.get(counter).substring(1, filesNames.get(counter).length() - 1),
                    fileDirectory.get(counter).substring(1, fileDirectory.get(counter).length() - 1), Double.MAX_VALUE,
                    "-fx-border-color: #000000; " + "-fx-border-radius: 7; " + "-fx-padding: 10px;"
                    + "-fx-background-color: #A9CCE3", Cursor.HAND,
                    new Font("Book Antiqua", 16));

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
        httpRequestFile.addFilePart("fileUpload", file);
        httpRequestFile.finish();
        LoadFXML.loadFXMLInScrollPane("/fxml/fileList.fxml", ProjectsController.getMainPane(), true, true);
    }
}
