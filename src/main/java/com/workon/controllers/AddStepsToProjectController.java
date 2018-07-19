package com.workon.controllers;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.workon.models.Step;
import com.workon.utils.*;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

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
        ArrayList<String> stepsName = ParseRequestContent.getValuesOf(Objects.requireNonNull(contentProjectSteps), "name");
        ArrayList<String> stepsDate = ParseRequestContent.getValuesOf(Objects.requireNonNull(contentProjectSteps), "date");
        //Set map
        Map<String, String> steps = SetMap.setStringStringMapWithArrayLists(stepsName, stepsDate);
        //Sorted map
        Map<String, LocalDate> formated = FormatedDate.sortStringLocalDate(Objects.requireNonNull(steps));
        //Fill project name
        projectTitleLabel.setText(CreateProjectController.getProject().getName());

        vboxStepsList.setSpacing(10);
        vboxStepsList.setStyle("-fx-padding: 5px");
        vboxAddSteps.setSpacing(10);

        Set set = formated.entrySet();
        for (Object aSet : set){
            Map.Entry entry = (Map.Entry) aSet;
            Label labelStepName = LabelHelper.createLabel(entry.getKey().toString().substring(1, entry.getKey().toString().length() - 1) +
                            " : " + FormatedDate.localDateToString((LocalDate)entry.getValue()),
                            Double.MAX_VALUE, new Font("Book Antiqua", 14), Pos.CENTER);
            vboxStepsList.getChildren().add(labelStepName);
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
