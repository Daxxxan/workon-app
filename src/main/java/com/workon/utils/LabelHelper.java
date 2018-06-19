package com.workon.utils;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class LabelHelper {
    public static void setLabel(Label label, String text, Pos position, String color){
        label.setText(text);
        label.setAlignment(position);
        label.setTextFill(Color.web(color));
    }

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
}
