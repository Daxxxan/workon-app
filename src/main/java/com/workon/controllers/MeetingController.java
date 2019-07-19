package com.workon.controllers;

import com.jfoenix.controls.JFXTextArea;
import com.workon.utils.HttpRequest;
import com.workon.utils.LoadFXML;
import com.workon.utils.ParseRequestContent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.Objects;

public class MeetingController {
    @FXML
    private JFXTextArea summary;
    @FXML
    private Label projectTitleLabel;
    @FXML
    private Label meetingLabel;

    @FXML
    public void initialize(){
        projectTitleLabel.setText(CreateProjectController.getProject().getCurrentMeetingName());
        String currentMeeting = null;
        try {
            currentMeeting = HttpRequest.getMeetingById(CreateProjectController.getProject().getCurrentMeetingId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(currentMeeting != null){
            String summaryContent = ParseRequestContent.getValueOf(currentMeeting, "summary");
            String meetingArea = ParseRequestContent.getValueOf(currentMeeting, "place");
            if(meetingArea != null){
                meetingLabel.setText("Emplacement: " + meetingArea.substring(1, meetingArea.length() - 1));
            }
            if(!Objects.equals(summaryContent, "null")){
                summary.setText(summaryContent.substring(1, Objects.requireNonNull(summaryContent).length() - 1));
            }
        }
    }

    @FXML
    protected void handleValidate() {
        if(!summary.getText().isEmpty()){
            HttpRequest.setSummaryMeeting(summary.getText(), CreateProjectController.getProject().getCurrentMeetingId());
            LoadFXML.loadFXMLInScrollPane("/fxml/meetingList.fxml", ProjectsController.getMainPane(), true, true);
        }
    }

    @FXML
    protected void handleMeetingList() {
        LoadFXML.loadFXMLInScrollPane("/fxml/meetingList.fxml", ProjectsController.getMainPane(), true, true);
    }
}
