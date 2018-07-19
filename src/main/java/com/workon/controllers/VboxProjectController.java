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

        String buttonStyle = "-fx-border-color: #000000; " + "-fx-border-radius: 7; " + "-fx-padding: 10px;" + "-fx-background-color:  #A9CCE3;";
        ButtonHelper.loadListOfButton(names, ids, buttonStyle, "project", projectListVBox);
    }
}
