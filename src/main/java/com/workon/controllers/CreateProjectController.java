package com.workon.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.workon.models.Project;
import com.workon.models.Step;
import com.workon.models.User;
import com.workon.utils.*;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CreateProjectController {
    @FXML
    private VBox vboxCollaborators;
    @FXML
    private VBox vboxStep;
    @FXML
    private TextField projectNameTextField;
    @FXML
    private JFXButton validateProjectButton;
    @FXML
    private Label projectNameErrorLabel;


    private ArrayList<JFXTextField> textFieldCollaboratorArray = new ArrayList<>();
    private ArrayList<JFXTextField> textFieldStepNameArray = new ArrayList<>();
    private ArrayList<JFXDatePicker> datePickerStepArray = new ArrayList<>();

    private static Project project = new Project();

    @FXML
    public void initialize(){
        vboxCollaborators.setSpacing(10);
        vboxCollaborators.setStyle("-fx-padding: 5px");
        vboxStep.setSpacing(10);
        vboxStep.setStyle("-fx-padding: 5px");
    }

    @FXML
    protected void handleAddCollaboratorButtonAction() throws IOException {
        JFXTextField textFieldCollaborator = new JFXTextField();
        textFieldCollaborator.setPromptText("Adresse email du collaborateur");
        setTextFieldCollaboratorArray(textFieldCollaborator);
        vboxCollaborators.getChildren().add(textFieldCollaborator);
    }

    @FXML
    protected void handleAddStepButtonAction() throws  IOException{
        JFXTextField textFieldStepName = AddStep.setStepName();
        JFXDatePicker datePickerStep = AddStep.setDatePicker();

        setDatePickerStepArray(datePickerStep);
        setTextFieldStepNameArray(textFieldStepName);

        vboxStep.getChildren().addAll(textFieldStepName, datePickerStep);
    }

    @FXML
    protected void handleValidateProjectButtonAction() throws Exception {
        if(!(projectNameTextField.getText().isEmpty())){
            Project project = new Project();
            //Lancement de la création d'un projet
            StringBuffer contentCreateProject = HttpRequest.addProject(projectNameTextField.getText());

            //Si le projet a été créé
            if(contentCreateProject != null){
                //Récupération de l'ID du projet créé
                String projectId = ParseRequestContent.getValueOf(contentCreateProject.toString(), "id");
                String addCollaboratorsRequest;

                //Set d'un objet ProjectsController
                project.setName(projectNameTextField.getText());
                project.setDirector(LoginConnectionController.getUserId());

                //Lancement de l'ajout du directeur aux collaborateurs
                StringBuffer contentAddDirectorToProjectCollaborators = HttpRequest.addCollaboratorsToProject(LoginConnectionController.getUserId().toString(), projectId);

                //Si le directeur a bien été ajouté
                if(contentAddDirectorToProjectCollaborators != null){
                    ArrayList<User> usersList = new ArrayList<>();
                    User director = new User(LoginConnectionController.getUserId());
                    director.setRole("Director");
                    usersList.add(director);

                    //Ajout de chaque collaborateurs au projet
                    for(JFXTextField textField : getTextFieldCollaboratorArray()) {
                        //Lancement de l'ajout des collaborateurs
                        StringBuffer contentAddCollaboratorsToProject = HttpRequest.addCollaboratorsToProject(textField.getText(), projectId);
                        if(contentAddCollaboratorsToProject != null){
                            //Récupération de l'id du collaborateur
                            int collaboratorId = Integer.parseInt(Objects.requireNonNull(ParseRequestContent.getValueOf(contentAddCollaboratorsToProject.toString(), "accountId")));

                            //Vérifier que l'id n'est pas déjà dans le tableau des users
                            int exist = 0;
                            for (User anUser : usersList) {
                                if (collaboratorId == anUser.getId()) {
                                    exist = 1;
                                }
                            }
                            //Si le user n'existe pas on le créer
                            if(exist == 0){
                                User user = new User(collaboratorId, textField.getText());
                                usersList.add(user);
                            }
                        }
                    }
                    //Ajout du tableau d'utilisateur au projet local
                    project.setUsers(usersList);

                    //Ajout de chaque steps au projet
                    ArrayList<Step> stepsList = AddStep.addStepsInDB(textFieldStepNameArray, datePickerStepArray, projectId);
                    //Ajout des steps au projet local
                    project.setSteps(stepsList);
                    //Création du projet global
                    setProject(project);

                    //Création du bouton projet dans l'interface
                    JFXButton button = ButtonHelper.setButton(projectNameTextField.getText(), projectId, Double.MAX_VALUE,
                            "-fx-border-color: #000000; " + "-fx-border-radius: 7; " + "-fx-padding: 10px;", Cursor.HAND,
                            new Font("Times New Roman", 16));
                    CreateProjectController.getProject().setId(button.getId());
                    CreateProjectController.getProject().setName(projectNameTextField.getText());

                    button.setOnAction(event -> {
                        try {
                            CreateProjectController.getProject().setId(button.getId());
                            CreateProjectController.getProject().setName(projectNameTextField.getText());
                            LoadFXML.loadFXMLInScrollPane("/fxml/addStepsProject.fxml", ProjectsController.getMainPane(), true, true);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                    VBox projectListVBox = (VBox)ProjectsController.getProjectListPane().lookup("#projectListVBox");
                    projectListVBox.setSpacing(10);
                    projectListVBox.setStyle("-fx-padding: 5px");
                    projectListVBox.getChildren().add(button);

                    LoadFXML.loadFXMLInScrollPane("/fxml/addStepsProject.fxml", ProjectsController.getMainPane(),true, true);
                }
            }
        }else{
            LabelHelper.setLabel(projectNameErrorLabel, "Veuillez saisir le nom du projet", Pos.CENTER_LEFT, "#FF0000");
        }
    }

    public static Project getProject() {
        return project;
    }

    public static void setProject(Project project) {
        CreateProjectController.project = project;
    }

    private void setTextFieldCollaboratorArray(JFXTextField textFieldCollaborator){
        this.textFieldCollaboratorArray.add(textFieldCollaborator);
    }

    public ArrayList<JFXTextField> getTextFieldCollaboratorArray(){
        return this.textFieldCollaboratorArray;
    }

    private void setTextFieldStepNameArray(JFXTextField textFieldStep){
        this.textFieldStepNameArray.add(textFieldStep);
    }

    public ArrayList<JFXTextField> getTextFieldStepNameArray(){
        return this.textFieldStepNameArray;
    }

    private void setDatePickerStepArray(JFXDatePicker datePickerStep){
        this.datePickerStepArray.add(datePickerStep);
    }

    public ArrayList<JFXDatePicker> getDatePickerStepArray() {
        return this.datePickerStepArray;
    }
}
