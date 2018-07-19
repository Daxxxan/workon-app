package com.workon.utils;

import com.jfoenix.controls.JFXButton;
import com.workon.controllers.CreateProjectController;
import com.workon.controllers.ProjectsController;
import com.workon.utils.parser.AnnotationParser;
import com.workon.utils.parser.NoNull;
import javafx.scene.Cursor;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class MeetingHelper {
    /**
     * Affiche toutes les reunions
     *
     * @param subjects
     *        ArrayList des sujets
     * @param dates
     *        ArrayList des dates
     * @param meetingId
     *        ArrayList des ID des meetings
     * @param vboxMeetingList
     *        vbox sur laquel afficher la liste
     */
    public static void displayMeetingList(@NoNull ArrayList<String> subjects, @NoNull ArrayList<String>dates, @NoNull ArrayList<String>meetingId, @NoNull VBox vboxMeetingList) {
        AnnotationParser.parse(subjects, dates, meetingId, vboxMeetingList);
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
                LoadFXML.loadFXMLInScrollPane("/fxml/meeting.fxml", ProjectsController.getMainPane(), true, true);
            });

            vboxMeetingList.setSpacing(10);
            vboxMeetingList.getChildren().add(meetingButton);
        }
    }
}
