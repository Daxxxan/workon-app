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

import java.io.IOException;
import java.util.ArrayList;

public class BugListController {

    @FXML
    private Label projectTitleLabel;
    @FXML
    private Label listBugLabel;
    @FXML
    private VBox vboxBugsList;

    @FXML
    public void initialize() throws Exception{
        projectTitleLabel.setText(CreateProjectController.getProject().getName());
        String contentBugs = HttpRequest.getBugs("0");
        if(contentBugs != null){
            ArrayList<String> bugsNames;
            ArrayList<String> bugsId;
            bugsNames = ParseRequestContent.getValuesOf(contentBugs, "name");
            bugsId = ParseRequestContent.getValuesOf(contentBugs, "id");
            if(bugsNames.isEmpty() || bugsId.isEmpty()){
                LabelHelper.setLabel(listBugLabel, "Pas de bug dans ce projet", Pos.CENTER, "#FFFFFF", new Font("Book Antiqua", 16));
            }else{
                LabelHelper.setLabel(listBugLabel, "Liste des bugs dans ce projet", Pos.CENTER, "#FFFFFF", new Font("Book Antiqua", 16));
                for(int counter = 0; counter < bugsNames.size(); counter++){
                    String bugName = bugsNames.get(counter).substring(1, bugsNames.get(counter).length() - 1);
                    JFXButton bugButton = ButtonHelper.setButton(bugsNames.get(counter).substring(1, bugsNames.get(counter).length() - 1),
                            bugsId.get(counter), Double.MAX_VALUE,
                            "-fx-border-color: #000000; " + "-fx-border-radius: 7; " + "-fx-padding: 10px;"
                            + "-fx-background-color: #A9CCE3;", Cursor.HAND,
                            new Font("Book Antiqua", 16));

                    ContextMenuHelper.setContextMenuToButtonToDeleteBug(bugButton);

                    bugButton.setOnAction(event -> {
                        CreateProjectController.getProject().setCurrentBugId(bugButton.getId());
                        CreateProjectController.getProject().setCurrentBugName(bugName);
                        try {
                            LoadFXML.loadFXMLInScrollPane("/fxml/bug.fxml", ProjectsController.getMainPane(), true, true);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                    vboxBugsList.setSpacing(10);
                    vboxBugsList.getChildren().add(bugButton);

                    Bug bug = new Bug(bugsNames.get(counter).substring(1, bugsNames.get(counter).length() - 1),
                            bugsId.get(counter));
                    CreateProjectController.getProject().addBugToArrayList(bug);
                }
            }
        }else{
            LabelHelper.setLabel(listBugLabel, "Erreur technique: Veuillez contacter le support.", Pos.CENTER, "#FF0000", new Font("Book Antiqua", 16));
        }
    }

    @FXML
    public void handleCreateBug() throws Exception{
        LoadFXML.loadFXMLInScrollPane("/fxml/ficheBug.fxml", ProjectsController.getMainPane(), true, true);
    }
}
