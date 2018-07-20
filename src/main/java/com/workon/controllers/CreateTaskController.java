package com.workon.controllers;

import com.jfoenix.controls.JFXTextField;
import com.workon.utils.HttpRequest;
import com.workon.utils.LoadFXML;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CreateTaskController {
    @FXML
    private Label projectTitleLabel;
    @FXML
    private JFXTextField taskName;
    @FXML
    private Label errorLabel;

    @FXML
    public void initialize(){
        projectTitleLabel.setText(CreateProjectController.getProject().getCurrentStepName());
    }

    @FXML
    protected void handleTaskList(){
        LoadFXML.loadFXMLInScrollPane("/fxml/tasksList.fxml", ProjectsController.getMainPane(), true, true);
    }

    @FXML
    protected void handleValidateTask(){
        if (!taskName.getText().isEmpty()){
            HttpRequest.createTask(taskName.getText());
            LoadFXML.loadFXMLInScrollPane("/fxml/tasksList.fxml", ProjectsController.getMainPane(), true, true);
        }else{
            errorLabel.setText("Veuillez renseigner le champ");
        }
    }
}
