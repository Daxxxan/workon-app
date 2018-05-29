package com.workon.utils;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class LabelHelper {
    public static void setLabel(Label label, String text, Pos position, String color){
        label.setText(text);
        label.setAlignment(position);
        label.setTextFill(Color.web(color));
    }
}
