package com.workon.utils;

import com.workon.utils.parser.AnnotationParser;
import com.workon.utils.parser.NoNull;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class LoadFXML {
    public static void loadFXMLInScrollPane(@NoNull String path, @NoNull ScrollPane scrollPane, @NoNull Boolean fitToHeight, @NoNull Boolean fitToWidth){
        AnnotationParser.parse(path, scrollPane, fitToHeight, fitToWidth);
        Parent parent = null;
        try {
            parent = FXMLLoader.load(LoadFXML.class.getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        scrollPane.setContent(parent);
        scrollPane.setFitToHeight(fitToHeight);
        scrollPane.setFitToWidth(fitToWidth);
    }
}
