package com.workon.controllers;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.workon.models.Bug;
import com.workon.utils.HttpRequest;
import com.workon.utils.LabelHelper;
import com.workon.utils.LoadFXML;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;

import java.util.HashMap;
import java.util.Map;

public class CardBugController {

    @FXML
    private Label projectTitleLabel;
    @FXML
    private JFXTextField bugName;
    @FXML
    private JFXTextArea bugDescription;
    @FXML
    private Label errorCreateBugLabel;

    @FXML
    public void initialize(){
        projectTitleLabel.setText(CreateProjectController.getProject().getName());
    }

    @FXML
    protected void handleBugsList() throws Exception{
        LoadFXML.loadFXMLInScrollPane("/fxml/bugList.fxml", ProjectsController.getMainPane(), true, true);
    }

    @FXML
    protected void handleValidateBug() throws Exception{
        if(!bugName.getText().isEmpty() && !bugDescription.getText().isEmpty()){
            StringBuffer contentRequest = HttpRequest.addBug(bugName.getText(), bugDescription.getText(), CreateProjectController.getProject().getId());
            if(contentRequest != null){
                Bug bug = new Bug(bugName.getText(), bugDescription.getText());
                CreateProjectController.getProject().addBugToArrayList(bug);
                LoadFXML.loadFXMLInScrollPane("/fxml/bugList.fxml", ProjectsController.getMainPane(), true, true);
            }else{
                LabelHelper.setLabel(errorCreateBugLabel, "Erreur technique: Veuillez contacter le support", Pos.CENTER, "#ff0000");
            }
        }else{
            LabelHelper.setLabel(errorCreateBugLabel, "Veuillez renseigner tous les champs", Pos.CENTER, "#ff0000");
        }
    }
}
