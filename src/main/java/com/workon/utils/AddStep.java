package com.workon.utils;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.workon.controllers.LoginConnectionController;
import com.workon.models.Step;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public abstract class AddStep implements Comparable{
    public static JFXTextField setStepName(){
        JFXTextField textFieldStepName = new JFXTextField();
        textFieldStepName.setPromptText("Nom du jalon");
        return textFieldStepName;
    }

    public static JFXDatePicker setDatePicker(){
        JFXDatePicker datePickerStep = new JFXDatePicker();
        datePickerStep.setPromptText("Date de fin du jalon");
        return datePickerStep;
    }

    public static ArrayList<Step> addStepsInDB(ArrayList<JFXTextField> textFieldStepNameArray, ArrayList<JFXDatePicker> datePickerStepArray, String projectId) throws Exception {
        ArrayList<Step> stepsList = new ArrayList<>();
        for(int counter = 0; counter < textFieldStepNameArray.size(); counter++){
            if(datePickerStepArray.get(counter).getValue() != null && textFieldStepNameArray.get(counter).getText() != null){
                //Convert LocalDate to String
                DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

                String contentAddStepsToProject = HttpRequest.addStep(projectId, textFieldStepNameArray.get(counter).getText(), datePickerStepArray.get(counter).getValue().format(formatter));

                //Si l'ajout a bien été effectué
                if(contentAddStepsToProject != null){
                    Step step = new Step(textFieldStepNameArray.get(counter).getText(), datePickerStepArray.get(counter).getValue());
                    stepsList.add(step);
                }
            }
        }

        return stepsList;
    }
}
