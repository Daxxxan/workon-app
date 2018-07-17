package com.workon.controllers;

import com.workon.utils.HttpRequest;
import com.workon.utils.LoadFXML;
import com.workon.utils.MeetingHelper;
import com.workon.utils.ParseRequestContent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;

public class OldMeetingListController {
    @FXML
    private Label projectTitleLabel;
    @FXML
    private VBox vboxMeetingList;

    @FXML
    public void initialize() throws IOException {
        projectTitleLabel.setText(CreateProjectController.getProject().getName());
        String meetings = null;
        try {
            meetings = HttpRequest.getOldMeetings();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<String> subjects = ParseRequestContent.getValuesOf(meetings, "subject");
        ArrayList<String>dates = ParseRequestContent.getValuesOf(meetings, "date");
        ArrayList<String>meetingId = ParseRequestContent.getValuesOf(meetings, "id");

        MeetingHelper.displayMeetingList(subjects, dates, meetingId, vboxMeetingList);
    }

    @FXML
    protected void handleDisplayNextMeetings(){
        try {
            LoadFXML.loadFXMLInScrollPane("/fxml/meetingList.fxml", ProjectsController.getMainPane(), true, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
