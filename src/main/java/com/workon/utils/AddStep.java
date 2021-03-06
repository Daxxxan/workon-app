package com.workon.utils;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.workon.controllers.LoginConnectionController;
import com.workon.models.Step;
import com.workon.utils.parser.AnnotationParser;
import com.workon.utils.parser.NoNull;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public abstract class AddStep implements Comparable{

    /**
     * Set un champ de texte pour l'ajout d'un jalon
     *
     * @return JFXTextField
     *         Retourne un champ de texte
     */
    public static JFXTextField setStepName(){
        JFXTextField textFieldStepName = new JFXTextField();
        textFieldStepName.setPromptText("Nom du jalon");
        return textFieldStepName;
    }

    /**
     * Set dun champ de texte pour l'ajout d'un jalon
     *
     * @return JFXDatePicker
     */
    public static JFXDatePicker setDatePicker(){
        JFXDatePicker datePickerStep = new JFXDatePicker();
        datePickerStep.setPromptText("Date de fin du jalon");
        return datePickerStep;
    }

    /**
     * Ajout des jalon au projet
     *
     * @param textFieldStepNameArray
     *        ArrayList des noms des jalons
     * @param datePickerStepArray
     *        ArrayList des dates des jalons
     * @param projectId
     *        ID du projet
     * @param vboxSteps
     *        Vbox contenant les steps
     * @return ArrayList
     *         Retourne une ArrayList de jalon
     */
    public static ArrayList<Step> addStepsInDB(@NoNull ArrayList<JFXTextField> textFieldStepNameArray, @NoNull ArrayList<JFXDatePicker> datePickerStepArray, @NoNull String projectId, @NoNull VBox vboxSteps) {
        AnnotationParser.parse(textFieldStepNameArray, datePickerStepArray, projectId, vboxSteps);
        ArrayList<Step> stepsList = new ArrayList<>();
        int count = 0;
        for(int counter = 0; counter < textFieldStepNameArray.size(); counter++){
            if(datePickerStepArray.get(counter).getValue() != null && textFieldStepNameArray.get(counter).getText() != null){
                //Convert LocalDate to String
                DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

                String contentAddStepsToProject = null;
                try {
                    contentAddStepsToProject = HttpRequest.addStep(projectId, textFieldStepNameArray.get(counter).getText(), datePickerStepArray.get(counter).getValue().format(formatter));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Si l'ajout a bien été effectué
                if(contentAddStepsToProject != null){
                    Step step = new Step(textFieldStepNameArray.get(counter).getText(), datePickerStepArray.get(counter).getValue());
                    stepsList.add(step);
                }
            }else{
                if(count == 0){
                    Label label = LabelHelper.createLabel("Impossible d'ajouter le jalon", Double.MAX_VALUE, new Font("Book Antiqua", 14), Pos.CENTER);
                    LabelHelper.setLabel(label, null, null, "#FF0000", new Font("Book Antiqua", 14));
                    vboxSteps.getChildren().add(label);
                    count++;
                }
            }
        }

        return stepsList;
    }
}
