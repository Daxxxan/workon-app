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

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class MainProjectUIController {
    @FXML
    private VBox vboxStepsList;
    @FXML
    private  VBox vboxAddSteps;
    @FXML
    private Label projectTitleLabel;

    private ArrayList<JFXTextField> textFieldStepNameArray = new ArrayList<>();
    private ArrayList<JFXDatePicker> datePickerStepArray = new ArrayList<>();

    public void initialize() throws IOException {
        //Get steps du projet
        String getProjectSteps = "http://localhost:3000/api/accounts/".concat(LoginConnectionController.getUserId().toString())
                .concat("/projects/").concat(CreateProjectController.getProject().getId()).concat("/steps");
        StringBuffer contentProjectSteps = HttpRequest.setRequest(getProjectSteps, null, null, "GET", null, LoginConnectionController.getUserToken());

        //Get du projet
        String getProject = "http://localhost:3000/api/accounts/".concat(LoginConnectionController.getUserId().toString())
                .concat("/projects/").concat(CreateProjectController.getProject().getId());
        StringBuffer contentProject = HttpRequest.setRequest(getProject, null, null, "GET", null, LoginConnectionController.getUserToken());

        //Fill des dates
        ArrayList<String> stepsName = ParseRequestContent.getValuesOf(Objects.requireNonNull(contentProjectSteps).toString(), "name");
        ArrayList<String> stepsDate = ParseRequestContent.getValuesOf(Objects.requireNonNull(contentProjectSteps).toString(), "date");
        //Set map
        Map<String, String> steps = SetMap.setStringStringMapWithArrayLists(stepsName, stepsDate);
        //Sorted map
        Map<String, LocalDate> formated = FormatedDate.sortStringLocalDate(Objects.requireNonNull(steps));
        //Fill project name
        String projectName = ParseRequestContent.getValueOf(Objects.requireNonNull(contentProject).toString(), "name");
        projectTitleLabel.setText(projectName.substring(1, projectName.length() - 1));

        vboxStepsList.setSpacing(10);
        vboxStepsList.setStyle("-fx-padding: 5px");
        vboxAddSteps.setSpacing(10);

        Set set = formated.entrySet();
        for (Object aSet : set){
            Map.Entry entry = (Map.Entry) aSet;
            Label labelStepName = LabelHelper.createLabel(entry.getKey().toString().substring(1, entry.getKey().toString().length() - 1) +
                            " : " + FormatedDate.localDateToString((LocalDate)entry.getValue()),
                            Double.MAX_VALUE, new Font("Times New Roman", 14), Pos.CENTER);
            vboxStepsList.getChildren().add(labelStepName);
        }

    }

    @FXML
    protected void handleAddStepButtonAction() throws Exception{
        JFXTextField textFieldStepName = AddStep.setStepName();
        JFXDatePicker datePickerStep = AddStep.setDatePicker();

        setTextFieldStepNameArray(textFieldStepName);
        setDatePickerStepArray(datePickerStep);

        vboxAddSteps.getChildren().addAll(textFieldStepName, datePickerStep);
    }

    @FXML
    protected void handleValidateSteps() throws Exception{
        ArrayList<Step> steps = AddStep.addStepsInDB(textFieldStepNameArray, datePickerStepArray, CreateProjectController.getProject().getId());
        for (Step step : steps){
            Label labelStepName = LabelHelper.createLabel(step.getName()
                            + " : " + FormatedDate.StringFormater(step.getDate().toString()),
                    Double.MAX_VALUE, new Font("Times New Roman", 14), Pos.CENTER);
            vboxStepsList.getChildren().add(labelStepName);
        }
    }

    @FXML
    protected void handleSwitchToCollaborators() throws Exception{
        System.out.println("Switch");
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
