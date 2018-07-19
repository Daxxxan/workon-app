package com.workon.utils;

import com.workon.controllers.LoginConnectionController;
import com.workon.utils.parser.AnnotationParser;
import com.workon.utils.parser.NoNull;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class LabelHelper {
    /**
     * Set d'un Label existant
     *
     * @param label
     *        Label a set
     * @param text
     *        Texte a mettre dans le label
     * @param position
     *        Position que doit avoir le label
     * @param color
     *        Couleur du label
     * @param font
     *        Formatage du texte
     */
    public static void setLabel(Label label, String text, Pos position, String color, Font font){
        if(text != null){
            label.setText(text);
        }
        if(text != null){
            label.setAlignment(position);
        }
        if(color != null){
            label.setTextFill(Color.web(color));
        }
        if(font != null){
            label.setFont(font);
        }
    }

    /**
     * Cree un label
     *
     * @param name
     *        Nom du label
     * @param maxWidth
     *        Taille du label
     * @param font
     *        Formatage du texte
     * @param pos
     *        Position du label
     * @return String
     *         le retour de la requete HTTP
     */
    public static Label createLabel(String name, Double maxWidth, Font font, Pos pos){
        Label label = new Label(name);

        if(maxWidth != null){
            label.setMaxWidth(maxWidth);
        }
        if(font != null){
            label.setFont(font);
        }
        if(pos != null){
            label.setAlignment(pos);
        }

        return label;
    }

    /**
     * Set du style d'un message
     *
     * @param label
     *        Label a modifier
     * @param accountId
     *        ID de l'emmeteur du message
     */
    public static void setMessageStyle(@NoNull String accountId, @NoNull Label label){
        AnnotationParser.parse(accountId, label);
        if(accountId.equals(LoginConnectionController.getUserId())){
            label.setStyle("-fx-background-color: #CACFD2; -fx-background-radius: 10; -fx-label-padding: 5;");
        }else{
            label.setStyle("-fx-background-color: #A9CCE3; -fx-background-radius: 10; -fx-label-padding: 5;");
        }
    }
}
