package com.workon.controllers;

import com.jfoenix.controls.JFXButton;
import com.workon.models.Bug;
import com.workon.utils.*;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.ArrayList;

public class BugListController {

    @FXML
    private Label projectTitleLabel;
    @FXML
    private Label listBugLabel;
    @FXML
    private VBox vboxBugsList;

    @FXML
    public void initialize() {
        projectTitleLabel.setText(CreateProjectController.getProject().getName());
        String contentBugs = HttpRequest.getBugs("0");
        if(contentBugs != null){
            ArrayList<String>bugsNames = ParseRequestContent.getValuesOf(contentBugs, "name");
            ArrayList<String>bugsId = ParseRequestContent.getValuesOf(contentBugs, "id");
            if(bugsNames.isEmpty() || bugsId.isEmpty()){
                LabelHelper.setLabel(listBugLabel, "Pas de bug dans ce projet", Pos.CENTER, "#FFFFFF", new Font("Book Antiqua", 16));
            }else{
                LabelHelper.setLabel(listBugLabel, "Liste des bugs dans ce projet", Pos.CENTER, "#FFFFFF", new Font("Book Antiqua", 16));

                String buttonStyle = "-fx-border-color: #000000; " + "-fx-border-radius: 7; " + "-fx-padding: 10px;" + "-fx-background-color: #A9CCE3;";
                ButtonHelper.loadListOfButton(bugsNames, bugsId, buttonStyle, "bug", vboxBugsList);
            }
        }else{
            LabelHelper.setLabel(listBugLabel, "Erreur technique: Veuillez contacter le support.", Pos.CENTER, "#FF0000", new Font("Book Antiqua", 16));
        }
    }

    @FXML
    public void handleCreateBug() {
        LoadFXML.loadFXMLInScrollPane("/fxml/ficheBug.fxml", ProjectsController.getMainPane(), true, true);
    }
}
