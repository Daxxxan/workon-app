package com.workon.controllers;

import com.jfoenix.controls.JFXTextField;
import com.workon.utils.HttpRequest;
import com.workon.utils.LabelHelper;
import com.workon.utils.LoadFXML;
import com.workon.utils.ParseRequestContent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Objects;

public class AddCollaboratorsController {
    @FXML
    private Label projectTitleLabel;
    @FXML
    private VBox vboxCollaboratorList;
    @FXML
    private VBox vboxAddCollaborators;

    private ArrayList<JFXTextField> collaboratorsList = new ArrayList<>();
    private ArrayList<String> collaboratorsNames = new ArrayList<>();

    public void initialize(){
        //Get collaborators
        String contentProjectCollaborators = HttpRequest.getCollaborators();

        projectTitleLabel.setText(CreateProjectController.getProject().getName());

        ArrayList<String> collaborators = ParseRequestContent.getValuesOf(Objects.requireNonNull(contentProjectCollaborators), "email");

        vboxCollaboratorList.setSpacing(10);
        vboxCollaboratorList.setStyle("-fx-padding: 5px");
        vboxAddCollaborators.setSpacing(10);

        if(collaborators != null){
            for (String collaborator : collaborators){
                Label collaboratorLabel = LabelHelper.createLabel(collaborator.substring(1, collaborator.length() - 1), Double.MAX_VALUE, new Font("Book Antiqua", 14), Pos.CENTER);
                vboxCollaboratorList.getChildren().add(collaboratorLabel);
                setCollaboratorsNames(collaborator.substring(1, collaborator.length() - 1));
            }
        }
    }

    @FXML
    protected void handleSwitchToSteps(){
        LoadFXML.loadFXMLInScrollPane("/fxml/addStepsProject.fxml", ProjectsController.getMainPane(), true, true);
    }

    @FXML
    protected void handleAddCollaboratorProjectButton(){
        JFXTextField collaboratorEmail = new JFXTextField();
        setCollaboratorsList(collaboratorEmail);
        collaboratorEmail.setPromptText("Email du collaborateur");
        vboxAddCollaborators.getChildren().add(collaboratorEmail);
    }

    @FXML
    protected void handleValidateCollaborators(){
        for(JFXTextField jfxTextField : getCollaboratorsList()){
            if(jfxTextField.getText() != null){
                if(!getCollaboratorsNames().contains(jfxTextField.getText())){
                    String contentAddCollaboratorsToProject =  HttpRequest.addCollaboratorsToProject(jfxTextField.getText(), CreateProjectController.getProject().getId());
                    if(contentAddCollaboratorsToProject != null){
                        LoadFXML.loadFXMLInScrollPane("/fxml/addCollaboratorsProject.fxml", ProjectsController.getMainPane(), true, true);
                    }else{
                        Label label = LabelHelper.createLabel("Impossible d'ajouter le collaborateur", Double.MAX_VALUE, new Font("Book Antiqua", 14), Pos.CENTER);
                        LabelHelper.setLabel(label, null, null, "#FF0000", new Font("Book Antiqua", 16));
                        vboxCollaboratorList.getChildren().add(label);
                    }
                }
            }
        }
    }

    public void setCollaboratorsList(JFXTextField jfxTextField){
        this.collaboratorsList.add(jfxTextField);
    }

    public ArrayList<JFXTextField> getCollaboratorsList() {
        return collaboratorsList;
    }

    public ArrayList<String> getCollaboratorsNames() {
        return collaboratorsNames;
    }

    public void setCollaboratorsNames(String collaboratorsNames) {
        this.collaboratorsNames.add(collaboratorsNames);
    }
}
