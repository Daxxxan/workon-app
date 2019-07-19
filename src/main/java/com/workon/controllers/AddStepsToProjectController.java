package com.workon.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.workon.models.Project;
import com.workon.models.Step;
import com.workon.utils.*;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.time.LocalDate;
import java.util.ArrayList;

public class AddStepsToProjectController {
    @FXML
    private VBox vboxStepsList;
    @FXML
    private  VBox vboxAddSteps;
    @FXML
    private Label projectTitleLabel;

    private ArrayList<JFXTextField> textFieldStepNameArray = new ArrayList<>();
    private ArrayList<JFXDatePicker> datePickerStepArray = new ArrayList<>();

    public void initialize() {
        //Get steps du projet
        String contentProjectSteps = HttpRequest.getProjectSteps();
        //Fill des dates
        ArrayList<String> stepsName = ParseRequestContent.getValuesOf(contentProjectSteps, "name");
        ArrayList<String> stepsDate = ParseRequestContent.getValuesOf(contentProjectSteps, "date");
        ArrayList<String> stepsId = ParseRequestContent.getValuesOf(contentProjectSteps, "id");
        ArrayList<String> stepsState = ParseRequestContent.getValuesOf(contentProjectSteps, "state");

        projectTitleLabel.setText(CreateProjectController.getProject().getName());
        vboxStepsList.setSpacing(10);
        vboxStepsList.setStyle("-fx-padding: 5px");
        vboxAddSteps.setSpacing(10);

        for(int counter = 0; counter < stepsName.size(); counter++){
            LocalDate localDateStep = FormatedDate.stringToLocalDate(stepsDate.get(counter).substring(1, stepsDate.get(counter).length() - 1));
            String step = FormatedDate.localDateToString(localDateStep);
            String buttonStyle = null;

            String buttonStepName = stepsName.get(counter).substring(1, stepsName.get(counter).length() - 1) + " : " + step;

            if(stepsState.get(counter).equals("true")){
                buttonStyle = "-fx-border-color: #000000; " + "-fx-border-radius: 7; " + "-fx-padding: 10px;" + "-fx-background-color: #D98880;";
            }else{
                buttonStyle = "-fx-border-color: #000000; " + "-fx-border-radius: 7; " + "-fx-padding: 10px;" + "-fx-background-color: #52BE80;";
            }
            JFXButton button = ButtonHelper.setButton(buttonStepName, stepsId.get(counter), Double.MAX_VALUE, buttonStyle, Cursor.HAND,
                    new Font("Book Antiqua", 16));

            int finalCounter = counter;
            button.setOnAction(event -> {
                CreateProjectController.getProject().setCurrentStepId(stepsId.get(finalCounter));
                CreateProjectController.getProject().setCurrentStepName(buttonStepName);
                LoadFXML.loadFXMLInScrollPane("/fxml/tasksList.fxml", ProjectsController.getMainPane(), true, true);
            });

            vboxStepsList.getChildren().add(button);
        }
    }

    @FXML
    protected void handleAddStepButtonAction() {
        JFXTextField textFieldStepName = AddStep.setStepName();
        JFXDatePicker datePickerStep = AddStep.setDatePicker();

        setTextFieldStepNameArray(textFieldStepName);
        setDatePickerStepArray(datePickerStep);

        vboxAddSteps.getChildren().addAll(textFieldStepName, datePickerStep);
    }

    @FXML
    protected void handleValidateSteps() {
        ArrayList<Step> steps = AddStep.addStepsInDB(textFieldStepNameArray, datePickerStepArray, CreateProjectController.getProject().getId(), vboxStepsList);
        if (steps.size() != 0)
            LoadFXML.loadFXMLInScrollPane("/fxml/addStepsProject.fxml", ProjectsController.getMainPane(), true, true);
    }

    @FXML
    protected void handleSwitchToCollaborators() {
        LoadFXML.loadFXMLInScrollPane("/fxml/addCollaboratorsProject.fxml", ProjectsController.getMainPane(), true, true);
    }

    private void setTextFieldStepNameArray(JFXTextField textFieldStep){
        this.textFieldStepNameArray.add(textFieldStep);
    }

    public ArrayList<JFXTextField> getTextFieldStepNameArray(){
        return this.textFieldStepNameArray;
    }

    private void setDatePickerStepArray(JFXDatePicker datePickerStep){
        this.datePickerStepArray.add(datePickerStep);
    }

    public ArrayList<JFXDatePicker> getDatePickerStepArray() {
        return this.datePickerStepArray;
    }
}
