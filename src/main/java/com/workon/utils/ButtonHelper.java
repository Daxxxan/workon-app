package com.workon.utils;

import com.jfoenix.controls.JFXButton;
import javafx.scene.Cursor;
import javafx.scene.text.Font;

public class ButtonHelper {
    /**
     * Creer un bouton
     *
     * @param title
     *        Nom du bouton
     * @param id
     *        ID du bouton
     * @param width
     *        Taille du bouton
     * @param borderStyle
     *        Style des bordures
     * @param cursor
     *        Curseur lors du passage sur le bouton
     * @param font
     *        Formatage du texte
     * @return JFXButton
     */
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
