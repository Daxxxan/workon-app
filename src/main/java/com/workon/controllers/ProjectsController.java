package com.workon.controllers;

import com.jfoenix.controls.JFXButton;
import com.workon.plugin.PluginLoader;
import com.workon.utils.*;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class ProjectsController {
    @FXML
    private ScrollPane mainScrollPane;
    @FXML
    private VBox projectListVBox;
    @FXML
    private GridPane mainGridPane;
    @FXML
    private JFXButton createBugButton;
    @FXML
    private JFXButton createEvolutionButton;
    @FXML
    private JFXButton createDocumentationButton;
    @FXML
    private ScrollPane scrollPaneProjectList;

    private static ScrollPane mainPane;
    private static ScrollPane projectListPane;

    @FXML
    public void initialize() throws IOException {
        String getProjectRequest = LoginConnectionController.getPath().concat("accounts/").concat(Integer.toString(LoginConnectionController.getUserId())).concat("/projects");
        StringBuffer contentRequest = HttpRequest.setRequest(getProjectRequest, null, null, "GET", null, LoginConnectionController.getUserToken());

        setMainPane(mainScrollPane);
        setProjectListPane(scrollPaneProjectList);

        createBugButton.setDisable(true);
        createEvolutionButton.setDisable(true);
        createDocumentationButton.setDisable(true);

        projectListVBox.setSpacing(10);
        projectListVBox.setStyle("-fx-padding: 5px");
        ArrayList<String> names = ParseRequestContent.getValuesOf(Objects.requireNonNull(contentRequest).toString(), "name");
        ArrayList<String> ids = ParseRequestContent.getValuesOf(Objects.requireNonNull(contentRequest).toString(), "id");

        for(int counter = 0; counter < names.size(); counter++){
            JFXButton button = ButtonHelper.setButton(names.get(counter).substring(1, names.get(counter).length() - 1),
                    ids.get(counter), Double.MAX_VALUE,
                    "-fx-border-color: #000000; " + "-fx-border-radius: 7; " + "-fx-padding: 10px;", Cursor.HAND,
                    new Font("Times New Roman", 16));
            button.setOnAction(event -> {
                CreateProjectController.getProject().setId(button.getId());
                    try {
                    LoadFXML.loadFXMLInScrollPane("/fxml/addStepsProject.fxml", mainScrollPane, true, true);
                    createBugButton.setDisable(false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            projectListVBox.getChildren().add(button);
        }

        String getAccountInformations = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId().toString());
        StringBuffer contentAccountInformations = HttpRequest.setRequest(getAccountInformations, null, null, "GET", null, LoginConnectionController.getUserToken());
        String firstname = ParseRequestContent.getValueOf(Objects.requireNonNull(contentAccountInformations).toString(), "firstname");
        firstname = firstname.substring(1, firstname.length() - 1);

        Label mainLabel = LabelHelper.createLabel("Bienvenue ".concat(firstname).concat(" !"), Double.MAX_VALUE, new Font("Times New Roman", 40), Pos.CENTER);
        mainGridPane.add(mainLabel, 2, 0, 4, 1);
    }

    @FXML
    protected void handleCreateProjectView() throws IOException {
        LoadFXML.loadFXMLInScrollPane("/fxml/createProject.fxml", mainScrollPane, true, true);
    }

    @FXML
    protected void handlePluginMenuItem() throws Exception {
        LoadFXML.loadFXMLInScrollPane("/fxml/pluginPanel.fxml", mainScrollPane, true, true);

        PluginLoader pluginLoader = new PluginLoader();
    }

    @FXML
    protected void handleCreateBugButton() throws Exception{
        LoadFXML.loadFXMLInScrollPane("/fxml/bugList.fxml", mainScrollPane, true, true);
    }

    public static ScrollPane getMainPane() {
        return mainPane;
    }

    public static void setMainPane(ScrollPane mainPane) {
        ProjectsController.mainPane = mainPane;
    }

    public static ScrollPane getProjectListPane() {
        return projectListPane;
    }

    public static void setProjectListPane(ScrollPane projectListPane) {
        ProjectsController.projectListPane = projectListPane;
    }
}
