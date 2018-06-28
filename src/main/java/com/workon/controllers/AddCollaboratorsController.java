package com.workon.controllers;

import com.jfoenix.controls.JFXButton;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class AddCollaboratorsController {
    @FXML
    private JFXButton switchToSteps;
    @FXML
    private Label projectTitleLabel;
    @FXML
    private VBox vboxCollaboratorList;
    @FXML
    private VBox vboxAddCollaborators;

    private ArrayList<JFXTextField> collaboratorsList = new ArrayList<>();
    private ArrayList<String> collaboratorsNames = new ArrayList<>();

    public void initialize() throws IOException {
        //Get collaborators
        String getProjectCollaborators = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId().toString())
                .concat("/projects/").concat(CreateProjectController.getProject().getId()).concat("/accounts");
        StringBuffer contentProjectCollaborators = HttpRequest.setRequest(getProjectCollaborators, null, null, "GET", null, LoginConnectionController.getUserToken());

        projectTitleLabel.setText(CreateProjectController.getProject().getName());

        ArrayList<String> collaborators = ParseRequestContent.getValuesOf(Objects.requireNonNull(contentProjectCollaborators).toString(), "email");

        vboxCollaboratorList.setSpacing(10);
        vboxCollaboratorList.setStyle("-fx-padding: 5px");
        vboxAddCollaborators.setSpacing(10);

        for (String collaborator : collaborators){
            Label collaboratorLabel = LabelHelper.createLabel(collaborator.substring(1, collaborator.length() - 1), Double.MAX_VALUE, new Font("Times New Roman", 14), Pos.CENTER);
            vboxCollaboratorList.getChildren().add(collaboratorLabel);
            setCollaboratorsNames(collaborator.substring(1, collaborator.length() - 1));
        }

    }

    @FXML
    protected void handleSwitchToSteps() throws Exception{
        LoadFXML.loadFXMLInScrollPane("/fxml/addStepsProject.fxml", ProjectsController.getMainPane(), true, true);
    }

    @FXML
    protected void handleAddCollaboratorProjectButton() throws Exception{
        JFXTextField collaboratorEmail = new JFXTextField();
        setCollaboratorsList(collaboratorEmail);
        collaboratorEmail.setPromptText("Email du collaborateur");
        vboxAddCollaborators.getChildren().add(collaboratorEmail);
    }

    @FXML
    protected void handleValidateCollaborators() throws Exception{
        for(JFXTextField jfxTextField : getCollaboratorsList()){
            if(jfxTextField.getText() != null){
                System.out.println("getTextField: " + jfxTextField.getText());
                if(!getCollaboratorsNames().contains(jfxTextField.getText())){
                    System.out.println("getTextField2: " + jfxTextField.getText());
                    String addCollaboratorsRequest = LoginConnectionController.getPath().concat("accounts/").concat(Integer.toString(LoginConnectionController.getUserId()))
                            .concat("/projects/").concat(CreateProjectController.getProject().getId()).concat("/accounts/rel/").concat(jfxTextField.getText());
                    //Lancement de l'ajout des collaborateurs
                    StringBuffer contentAddCollaboratorsToProject = HttpRequest.setRequest(addCollaboratorsRequest, null, null, "PUT", null, LoginConnectionController.getUserToken());
                    if(contentAddCollaboratorsToProject != null){
                        Label label = LabelHelper.createLabel(jfxTextField.getText(), Double.MAX_VALUE, new Font("Times New Roman", 14), Pos.CENTER);
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
