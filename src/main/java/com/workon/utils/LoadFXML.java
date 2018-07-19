package com.workon.utils;

import com.workon.utils.parser.AnnotationParser;
import com.workon.utils.parser.NoNull;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class LoadFXML {
    /**
     * Charge un FXML
     *
     * @param path
     *        Chemin vers le FXML
     * @param scrollPane
     *        La scrollPane sur laquelle nous devons attacher le FXML
     * @param fitToHeight
     *        True si Le FXML doit prendre toute la hauteur sinon false
     * @param fitToWidth
     * True si le FXML doit prendre toute la largeur sinon false
     */
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
