package com.workon.controllers;

import com.jfoenix.controls.JFXButton;
import com.workon.utils.*;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.ArrayList;

public class TaskListController {
    @FXML
    private Label projectTitleLabel;
    @FXML
    private VBox vboxTasksList;

    @FXML
    public void initialize(){
        projectTitleLabel.setText(CreateProjectController.getProject().getCurrentStepName());

        String getTasks = HttpRequest.getTasksFromStep();
        ArrayList<String>tasksName = ParseRequestContent.getValuesOf(getTasks, "name");
        ArrayList<String>tasksId = ParseRequestContent.getValuesOf(getTasks, "id");
        ArrayList<String>tasksState = ParseRequestContent.getValuesOf(getTasks, "state");
        int state = 1;
        for (int counter = 0; counter < tasksId.size(); counter++){
            if(tasksState.get(counter).equals("false")){
                state = 0;
                break;
            }
        }
        if(state == 1){
            HttpRequest.updateStateStep("0");
        }else {
            HttpRequest.updateStateStep("1");
        }

        for (int counter = 0; counter < tasksId.size(); counter++){
            String buttonStyle;
            if(tasksState.get(counter).equals("false")){
                buttonStyle = "-fx-border-color: #000000; " + "-fx-border-radius: 7; " + "-fx-padding: 10px;" + "-fx-background-color: #D98880;";
            }else{
                buttonStyle = "-fx-border-color: #000000; " + "-fx-border-radius: 7; " + "-fx-padding: 10px;" + "-fx-background-color: #52BE80;";
            }
            JFXButton button = ButtonHelper.setButton(tasksName.get(counter).substring(1, tasksName.get(counter).length() - 1),
                    tasksId.get(counter), Double.MAX_VALUE, buttonStyle, Cursor.HAND,
                    new Font("Book Antiqua", 16));
            ContextMenuHelper.setContextMenuToButtonToManageTask(button, tasksId.get(counter));
            vboxTasksList.getChildren().add(button);
        }

        vboxTasksList.setSpacing(10);
    }

    @FXML
    protected void handleAddTask(){
        LoadFXML.loadFXMLInScrollPane("/fxml/createTask.fxml", ProjectsController.getMainPane(), true, true);
    }

    @FXML
    protected void handleStepList(){
        LoadFXML.loadFXMLInScrollPane("/fxml/addStepsProject.fxml", ProjectsController.getMainPane(), true, true);
    }
}
