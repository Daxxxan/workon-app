package com.workon.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class LoadFXML {
    public static void loadFXMLInScrollPane(String path, ScrollPane scrollPane, Boolean fitToHeight, Boolean fitToWidth) throws IOException {
        Parent parent = FXMLLoader.load(LoadFXML.class.getResource(path));
        scrollPane.setContent(parent);
        scrollPane.setFitToHeight(fitToHeight);
        scrollPane.setFitToWidth(fitToWidth);
    }
}
