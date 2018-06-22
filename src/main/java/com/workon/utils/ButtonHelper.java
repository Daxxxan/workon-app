package com.workon.utils;

import com.jfoenix.controls.JFXButton;
import javafx.scene.Cursor;
import javafx.scene.text.Font;

public class ButtonHelper {
    public static JFXButton setButton(String title, String id, Double width, String borderStyle, Cursor cursor, Font font){
        JFXButton button = new JFXButton(title);
        if(id != null){
            button.setId(id);
        }
        if(width != null){
            button.setMaxWidth(width);
        }
        if(borderStyle != null){
            button.setStyle(borderStyle);
        }
        if(cursor != null){
            button.setCursor(cursor);
        }
        if(font != null){
            button.setFont(font);
        }

        return button;
    }
}
