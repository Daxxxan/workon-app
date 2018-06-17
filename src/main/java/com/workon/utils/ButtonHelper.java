package com.workon.utils;

import com.jfoenix.controls.JFXButton;

public class ButtonHelper {
    public static JFXButton setButton(String title, String id, Double width, String borderStyle){
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

        return button;
    }
}
