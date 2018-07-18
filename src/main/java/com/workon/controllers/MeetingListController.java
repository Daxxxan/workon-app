package com.workon.controllers;

import com.workon.utils.HttpRequest;
import com.workon.utils.LoadFXML;
import com.workon.utils.MeetingHelper;
import com.workon.utils.ParseRequestContent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class MeetingListController {
    @FXML
    private Label projectTitleLabel;
    @FXML
    private VBox vboxMeetingList;

    @FXML
    public void initialize() {
        projectTitleLabel.setText(CreateProjectController.getProject().getName());
        String meetings = HttpRequest.getMeetings();
        ArrayList<String>subjects = ParseRequestContent.getValuesOf(meetings, "subject");
        ArrayList<String>dates = ParseRequestContent.getValuesOf(meetings, "date");
        ArrayList<String>meetingId = ParseRequestContent.getValuesOf(meetings, "id");

        MeetingHelper.displayMeetingList(subjects, dates, meetingId, vboxMeetingList);
    }

    @FXML
    protected void handleCreateMeetings() {
        LoadFXML.loadFXMLInScrollPane("/fxml/createMeeting.fxml", ProjectsController.getMainPane(), true ,true);
    }

    @FXML
    protected void handleDisplayOldMeetings() {
        LoadFXML.loadFXMLInScrollPane("/fxml/oldMeetingList.fxml", ProjectsController.getMainPane(), true, true);
    }
}
