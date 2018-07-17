package com.workon.controllers;

import com.jfoenix.controls.JFXButton;
import com.workon.utils.*;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class MeetingListController {
    @FXML
    private Label projectTitleLabel;
    @FXML
    private VBox vboxMeetingList;

    @FXML
    public void initialize() throws Exception {
        projectTitleLabel.setText(CreateProjectController.getProject().getName());
        String meetings = HttpRequest.getMeetings();
        ArrayList<String>subjects = ParseRequestContent.getValuesOf(meetings, "subject");
        ArrayList<String>dates = ParseRequestContent.getValuesOf(meetings, "date");
        ArrayList<String>meetingId = ParseRequestContent.getValuesOf(meetings, "id");

        MeetingHelper.displayMeetingList(subjects, dates, meetingId, vboxMeetingList);
    }

    @FXML
    protected void handleCreateMeetings() throws Exception{
        LoadFXML.loadFXMLInScrollPane("/fxml/createMeeting.fxml", ProjectsController.getMainPane(), true ,true);
    }

    @FXML
    protected void handleDisplayOldMeetings() throws Exception{
        LoadFXML.loadFXMLInScrollPane("/fxml/oldMeetingList.fxml", ProjectsController.getMainPane(), true, true);
    }
}
