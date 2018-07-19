package com.workon.controllers;

import com.workon.Main;
import com.workon.utils.*;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
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
    private VBox vboxFiles;

    @FXML
    public void initialize() {
        projectTitleLabel.setText(CreateProjectController.getProject().getName());
        LabelHelper.setLabel(listOfFiles, "Liste des documents", Pos.CENTER, "#FFFFFF", new Font("Book Antiqua", 16));
        vboxFiles.setSpacing(10);

        String files = HttpRequest.getFiles(CreateProjectController.getProject().getId());
        ArrayList<String> filesNames = ParseRequestContent.getValuesOf(files, "name");
        ArrayList<String> fileDirectory = ParseRequestContent.getValuesOf(files, "container");

        String buttonStyle = "-fx-border-color: #000000; " + "-fx-border-radius: 7; " + "-fx-padding: 10px;" + "-fx-background-color: #A9CCE3";
        ButtonHelper.loadListOfButton(filesNames, fileDirectory, buttonStyle, "file", vboxFiles);
    }

    @FXML
    protected void handleImportFileButton() {
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
