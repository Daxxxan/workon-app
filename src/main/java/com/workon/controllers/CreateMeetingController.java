package com.workon.controllers;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import com.workon.utils.HttpRequest;
import com.workon.utils.LoadFXML;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CreateMeetingController {
    @FXML
    private Label projectTitleLabel;
    @FXML
    private JFXTextField meetingName;
    @FXML
    private JFXDatePicker meetingDate;
    @FXML
    private JFXTextField meetingArea;
    @FXML
    private Label errorLabel;
    @FXML
    private JFXTimePicker meetingTime;

    @FXML
    public void initialize(){
        projectTitleLabel.setText(CreateProjectController.getProject().getName());
    }

    @FXML
    protected void handleListMeeting() throws Exception {
        LoadFXML.loadFXMLInScrollPane("/fxml/meetingList.fxml", ProjectsController.getMainPane(), true, true);
    }

    @FXML
    protected void handleValidateMeeting() throws Exception {
        if(meetingName.getText().isEmpty() || meetingArea.getText().isEmpty() || meetingDate.getValue() == null || meetingTime.getValue() == null){
            errorLabel.setText("Veuillez saisir tous les champs");
        }else{
            String date = String.valueOf(meetingDate.getValue()) + ":" + String.valueOf(meetingTime.getValue());
            String result = HttpRequest.createMeeting(meetingName.getText(), meetingArea.getText(), String.valueOf(date));
            if(result != null){
                LoadFXML.loadFXMLInScrollPane("/fxml/meetingList.fxml", ProjectsController.getMainPane(), true, true);
            }
        }
    }
}
