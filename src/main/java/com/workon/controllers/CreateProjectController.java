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

import java.util.ArrayList;
import java.util.Objects;

public class CreateProjectController {
    @FXML
    private VBox vboxCollaborators;
    @FXML
    private VBox vboxStep;
    @FXML
    private TextField projectNameTextField;
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
    protected void handleAddCollaboratorButtonAction() {
        JFXTextField textFieldCollaborator = new JFXTextField();
        textFieldCollaborator.setPromptText("Adresse email du collaborateur");
        setTextFieldCollaboratorArray(textFieldCollaborator);
        vboxCollaborators.getChildren().add(textFieldCollaborator);
    }

    @FXML
    protected void handleAddStepButtonAction() {
        JFXTextField textFieldStepName = AddStep.setStepName();
        JFXDatePicker datePickerStep = AddStep.setDatePicker();

        setDatePickerStepArray(datePickerStep);
        setTextFieldStepNameArray(textFieldStepName);

        vboxStep.getChildren().addAll(textFieldStepName, datePickerStep);
    }

    @FXML
    protected void handleValidateProjectButtonAction() {
        if(!(projectNameTextField.getText().isEmpty())){
            Project project = new Project();
            String contentCreateProject = HttpRequest.addProject(projectNameTextField.getText());

            if(contentCreateProject != null){
                String projectId = ParseRequestContent.getValueOf(contentCreateProject, "id");

                project.setName(projectNameTextField.getText());
                project.setDirector(LoginConnectionController.getUserId());

                String contentAddDirectorToProjectCollaborators = HttpRequest.addCollaboratorsToProject(LoginConnectionController.getUserId().toString(), projectId);

                if(contentAddDirectorToProjectCollaborators != null){
                    ArrayList<User> usersList = new ArrayList<>();
                    User director = new User(LoginConnectionController.getUserId());
                    director.setRole("Director");
                    usersList.add(director);

                    for(JFXTextField textField : getTextFieldCollaboratorArray()) {
                        String contentAddCollaboratorsToProject = HttpRequest.addCollaboratorsToProject(textField.getText(), projectId);
                        if(contentAddCollaboratorsToProject != null){
                            int collaboratorId = Integer.parseInt(Objects.requireNonNull(ParseRequestContent.getValueOf(contentAddCollaboratorsToProject, "accountId")));

                            int exist = 0;
                            for (User anUser : usersList) {
                                if (collaboratorId == anUser.getId()) {
                                    exist = 1;
                                }
                            }
                            if(exist == 0){
                                User user = new User(collaboratorId, textField.getText());
                                usersList.add(user);
                            }
                        }
                    }
                    project.setUsers(usersList);

                    ArrayList<Step> stepsList = AddStep.addStepsInDB(textFieldStepNameArray, datePickerStepArray, projectId, vboxStep);
                    project.setSteps(stepsList);
                    setProject(project);

                    JFXButton button = ButtonHelper.setButton(projectNameTextField.getText(), projectId, Double.MAX_VALUE,
                            "-fx-border-color: #000000; " + "-fx-border-radius: 7; " + "-fx-padding: 10px;"
                                    + "-fx-background-color:  #A9CCE3;", Cursor.HAND,
                            new Font("Book Antiqua", 16));
                    CreateProjectController.getProject().setId(button.getId());
                    CreateProjectController.getProject().setName(projectNameTextField.getText());

                    ContextMenuHelper.setContextMenuToButton(button, "Clôturer le projet", "/fxml/vboxProject.fxml", "/fxml/createProject.fxml", "project");

                    button.setOnAction(event -> {
                        CreateProjectController.getProject().setId(button.getId());
                        CreateProjectController.getProject().setName(projectNameTextField.getText());
                        LoadFXML.loadFXMLInScrollPane("/fxml/addStepsProject.fxml", ProjectsController.getMainPane(), true, true);
                    });

                    VBox projectListVBox = (VBox)ProjectsController.getProjectListPane().lookup("#projectListVBox");
                    projectListVBox.setSpacing(10);
                    projectListVBox.setStyle("-fx-padding: 5px");
                    projectListVBox.getChildren().add(button);

                    LoadFXML.loadFXMLInScrollPane("/fxml/addStepsProject.fxml", ProjectsController.getMainPane(),true, true);
                    ProjectsController.getBug().setDisable(false);
                    ProjectsController.getMeeting().setDisable(false);
                    ProjectsController.getDocumentation().setDisable(false);
                }
            }
        }else{
            LabelHelper.setLabel(projectNameErrorLabel, "Veuillez saisir le nom du projet", Pos.CENTER_LEFT, "#FF0000", new Font("Book Antiqua", 16));
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
