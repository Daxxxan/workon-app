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

    public static ArrayList<Step> addStepsInDB(ArrayList<JFXTextField> textFieldStepNameArray, ArrayList<JFXDatePicker> datePickerStepArray, String projectId) throws IOException {
        ArrayList<Step> stepsList = new ArrayList<>();
        String addStepsRequest;
        for(int counter = 0; counter < textFieldStepNameArray.size(); counter++){
            if(datePickerStepArray.get(counter).getValue() != null && textFieldStepNameArray.get(counter).getText() != null){
                //Request pour l'ajout des steps au projet
                addStepsRequest = "http://localhost:3000/api/accounts/".concat(Integer.toString(LoginConnectionController.getUserId()))
                        .concat("/projects/").concat(projectId).concat("/steps");
                System.out.println(addStepsRequest);
                //Convert LocalDate to String
                DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
                String stepDate = datePickerStepArray.get(counter).getValue().format(formatter);

                //Set des parameters de la requete en POST
                Map<String, String> parameters = new HashMap<>();
                parameters.put("name", textFieldStepNameArray.get(counter).getText());
                parameters.put("date", stepDate);
                parameters.put("state", "En cours");

                //Lancement de l'ajout des steps
                StringBuffer contentAddStepsToProject = HttpRequest.setRequest(addStepsRequest, parameters, null, "POST", null, LoginConnectionController.getUserToken());
                System.out.println(contentAddStepsToProject);
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
