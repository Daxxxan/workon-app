package com.workon.utils;

import com.jfoenix.controls.JFXButton;
import com.workon.controllers.CreateProjectController;
import com.workon.controllers.ProjectsController;
import javafx.scene.Cursor;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class MeetingHelper {
    public static void displayMeetingList(ArrayList<String> subjects, ArrayList<String>dates, ArrayList<String>meetingId, VBox vboxMeetingList) {
        for (int counter = 0; counter < subjects.size(); counter++) {
            LocalDateTime meetingDate = FormatedDate.stringToLocalDateTime(dates.get(counter).substring(1, dates.get(counter).length() - 1));
            String meetingDateString = FormatedDate.localDateTimeToString(meetingDate);

            String meetingName = subjects.get(counter).substring(1, subjects.get(counter).length() - 1) + " : "
                    + meetingDateString;
            JFXButton meetingButton = ButtonHelper.setButton(meetingName,
                    meetingId.get(counter), Double.MAX_VALUE,
                    "-fx-border-color: #000000; " + "-fx-border-radius: 7; " + "-fx-padding: 10px;"
                            + "-fx-background-color: #A9CCE3;", Cursor.HAND,
                    new Font("Book Antiqua", 16));

            meetingButton.setOnAction(event -> {
                CreateProjectController.getProject().setCurrentMeetingId(meetingButton.getId());
                CreateProjectController.getProject().setCurrentMeetingName(meetingName);
                try {
                    LoadFXML.loadFXMLInScrollPane("/fxml/meeting.fxml", ProjectsController.getMainPane(), true, true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            vboxMeetingList.setSpacing(10);
            vboxMeetingList.getChildren().add(meetingButton);
        }
    }
}
