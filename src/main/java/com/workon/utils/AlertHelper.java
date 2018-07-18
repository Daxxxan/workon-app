package com.workon.utils;

import com.workon.utils.parser.AnnotationParser;
import com.workon.utils.parser.NoNull;
import javafx.scene.control.Alert;
import javafx.stage.Window;

public class AlertHelper {

    public static void showAlert(@NoNull Alert.AlertType alertType, @NoNull Window owner, @NoNull String title, @NoNull String message) {
        AnnotationParser.parse(alertType, owner, title, message);
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }
}