package com.workon.controllers;

import com.jfoenix.controls.JFXButton;
import com.workon.utils.*;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class VboxProjectController {
    @FXML
    private VBox projectListVBox;

    @FXML
    public void initialize() {
        projectListVBox.setSpacing(10);
        projectListVBox.setStyle("-fx-padding: 5px");
        String contentRequest = HttpRequest.getProjects("0");
        ArrayList<String> names = ParseRequestContent.getValuesOf(Objects.requireNonNull(contentRequest), "name");
        ArrayList<String> ids = ParseRequestContent.getValuesOf(Objects.requireNonNull(contentRequest), "id");

        for(int counter = 0; counter < names.size(); counter++){
            String projectName = names.get(counter).substring(1, names.get(counter).length() - 1);
            JFXButton button = ButtonHelper.setButton(projectName, ids.get(counter), Double.MAX_VALUE,
                    "-fx-border-color: #000000; " + "-fx-border-radius: 7; " + "-fx-padding: 10px;"
                    + "-fx-background-color:  #A9CCE3;", Cursor.HAND,
                    new Font("Book Antiqua", 16));

            ContextMenuHelper.setContextMenuToButton(button, "Clôturer le projet", "/fxml/vboxProject.fxml");

            button.setOnAction(event -> {
                CreateProjectController.getProject().setId(button.getId());
                CreateProjectController.getProject().setName(projectName);

                LoadFXML.loadFXMLInScrollPane("/fxml/addStepsProject.fxml", ProjectsController.getMainPane(), true, true);
                ProjectsController.getBug().setDisable(false);
                ProjectsController.getMeeting().setDisable(false);
                ProjectsController.getDocumentation().setDisable(false);
            });
            projectListVBox.getChildren().add(button);
        }
    }
}
