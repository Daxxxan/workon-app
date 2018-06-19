package com.workon.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.workon.models.Project;
import com.workon.models.Step;
import com.workon.models.User;
import com.workon.utils.ButtonHelper;
import com.workon.utils.HttpRequest;
import com.workon.utils.LabelHelper;
import com.workon.utils.ParseRequestContent;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
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
        JFXTextField textFieldStepName = new JFXTextField();
        JFXDatePicker datePickerStep = new JFXDatePicker();

        textFieldStepName.setPromptText("Nom du jalon");
        datePickerStep.setPromptText("Date de fin du jalon");

        setTextFieldStepNameArray(textFieldStepName);
        setDatePickerStepArray(datePickerStep);

        vboxStep.getChildren().addAll(textFieldStepName, datePickerStep);
    }

    @FXML
    protected void handleValidateProjectButtonAction() throws Exception {
        if(!(projectNameTextField.getText().isEmpty())){
            Project project = new Project();
            //Root pour la création d'un projet
            String createProjectRequest = "http://localhost:3000/api/accounts/".concat(Integer.toString(LoginConnectionController.getUserId())).concat("/projects");
            Map<String, String> createProjectParameters = new HashMap<>();
            createProjectParameters.put("name", projectNameTextField.getText());

            //Lancement de la création d'un projet
            StringBuffer contentCreateProject = HttpRequest.setRequest(createProjectRequest, createProjectParameters, null, "POST", null, LoginConnectionController.getUserToken());

            //Si le projet a été créé
            if(contentCreateProject != null){
                //Récupération de l'ID du projet créé
                String projectId = ParseRequestContent.getValueOf(contentCreateProject.toString(), "id");
                String addCollaboratorsRequest;
                String addStepsRequest;

                //Set d'un objet ProjectsController
                project.setName(projectNameTextField.getText());
                project.setDirector(LoginConnectionController.getUserId());

                //Root pour l'ajout du directeur aux collaborateurs
                String addDirectorRequest = "http://localhost:3000/api/accounts/".concat(Integer.toString(LoginConnectionController.getUserId()))
                        .concat("/projects/").concat(projectId).concat("/accounts/rel/").concat(Integer.toString(LoginConnectionController.getUserId()));
                //Lancement de l'ajout du directeur aux collaborateurs
                StringBuffer contentAddDirectorToProjectCollaborators = HttpRequest.setRequest(addDirectorRequest, null, null, "PUT", null, LoginConnectionController.getUserToken());

                //Si le directeur a bien été ajouté
                if(contentAddDirectorToProjectCollaborators != null){
                    ArrayList<User> usersList = new ArrayList<>();
                    User director = new User(LoginConnectionController.getUserId());
                    director.setRole("Director");
                    usersList.add(director);

                    //Ajout de chaque collaborateurs au projet
                    for(JFXTextField textField : getTextFieldCollaboratorArray()) {
                        //Requête pour l'ajout des collaborateurs au projet
                        addCollaboratorsRequest = "http://localhost:3000/api/accounts/".concat(Integer.toString(LoginConnectionController.getUserId()))
                                .concat("/projects/").concat(projectId).concat("/accounts/rel/").concat(textField.getText());
                        //Lancement de l'ajout des collaborateurs
                        StringBuffer contentAddCollaboratorsToProject = HttpRequest.setRequest(addCollaboratorsRequest, null, null, "PUT", null, LoginConnectionController.getUserToken());
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
                    ArrayList<Step> stepsList = new ArrayList<>();
                    for(int counter = 0; counter < getTextFieldStepNameArray().size(); counter++){
                        if(getTextFieldStepNameArray() != null && getDatePickerStepArray().get(counter).getValue() != null){
                            //Request pour l'ajout des steps au projet
                            addStepsRequest = "http://localhost:3000/api/accounts/".concat(Integer.toString(LoginConnectionController.getUserId()))
                                    .concat("/projects/").concat(projectId).concat("/steps");

                            //Convert LocalDate to String
                            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
                            String stepDate = getDatePickerStepArray().get(counter).getValue().format(formatter);

                            //Set des parameters de la requete en POST
                            Map<String, String> parameters = new HashMap<>();
                            parameters.put("name", getTextFieldStepNameArray().get(counter).getText());
                            parameters.put("date", stepDate);
                            parameters.put("state", "En cours");

                            //Lancement de l'ajout des steps
                            StringBuffer contentAddStepsToProject = HttpRequest.setRequest(addStepsRequest, parameters, null, "POST", null, LoginConnectionController.getUserToken());

                            //Si l'ajout a bien été effectué
                            if(contentAddStepsToProject != null){
                                Step step = new Step(getTextFieldStepNameArray().get(counter).getText(), getDatePickerStepArray().get(counter).getValue());
                                stepsList.add(step);
                            }
                        }
                    }
                    //Ajout des steps au projet local
                    project.setSteps(stepsList);
                    //Création du projet global
                    setProject(project);

                    //Création du bouton projet dans l'interface
                    JFXButton button = ButtonHelper.setButton(projectNameTextField.getText(), projectId, Double.MAX_VALUE,
                            "-fx-border-color: #000000; " + "-fx-border-radius: 7;");

                    //Récupération de la VBox pour insérer le bouton
                    ObservableList<Node> observableList = validateProjectButton.getParent().getParent().getParent().getParent()
                            .getParent().getChildrenUnmodifiable();
                    for(Node node : observableList){
                        if(Objects.equals(node.getId(), "scrollPaneProjectList")){
                            ScrollPane scrollPane = (ScrollPane)node;
                            VBox projectListVBox = (VBox)scrollPane.lookup("#projectListVBox");
                            projectListVBox.setSpacing(10);
                            projectListVBox.setStyle("-fx-padding: 5px");
                            projectListVBox.getChildren().add(button);
                        }
                    }
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
