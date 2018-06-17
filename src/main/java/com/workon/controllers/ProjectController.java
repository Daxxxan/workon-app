package com.workon.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.workon.models.Project;
import com.workon.models.Step;
import com.workon.models.User;
import com.workon.plugin.CoffeePluginInterface;
import com.workon.utils.HttpRequest;
import com.workon.utils.ParseRequestContent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ProjectController {
    @FXML
    private ScrollPane mainScrollPane;

    @FXML
    private VBox vboxCollaborators;

    @FXML
    private VBox vboxStep;

    @FXML
    private TextField projectNameTextField;

    @FXML
    private JFXButton validateProjectButton;

    @FXML
    private JFXButton createBugButton;

    @FXML
    private JFXButton createEvolutionButton;

    @FXML
    private JFXButton createDocumentationButton;

    @FXML
    private ScrollPane projectListScrollPane;

    private ArrayList<JFXTextField> textFieldCollaboratorArray = new ArrayList<>();
    private ArrayList<JFXTextField> textFieldStepNameArray = new ArrayList<>();
    private ArrayList<JFXDatePicker> datePickerStepArray = new ArrayList<>();

    private static Project project = new Project();

    public static Project getProject() {
        return project;
    }

    public static void setProject(Project project) {
        ProjectController.project = project;
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

    @FXML
    protected void handleCreateProjectView() throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/createProject.fxml"));
        mainScrollPane.setContent(parent);
        mainScrollPane.setFitToHeight(true);
        mainScrollPane.setFitToWidth(true);
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
    protected void handleValidateProjectButtonAction() throws IOException {
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
            String projectId = ParseRequestContent.getValueOf(contentCreateProject, "\"id\"");
            projectId = projectId.substring(0, Objects.requireNonNull(projectId).length() - 1);
            String addCollaboratorsRequest;
            String addStepsRequest;

            //Set d'un objet ProjectController
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
                        int collaboratorId = Integer.parseInt(Objects.requireNonNull(ParseRequestContent.getValueOf(contentAddCollaboratorsToProject, "\"accountId\"")));

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
                //Ajout des steps au projet local
                project.setSteps(stepsList);
                //Création du projet global
                setProject(project);

                //Création du bouton projet dans l'interface
            }
        }
    }

    @FXML
    protected void handlePluginMenuItem() throws Exception {
        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/pluginPanel.fxml"));
        mainScrollPane.setContent(parent);
        mainScrollPane.setFitToHeight(true);
        mainScrollPane.setFitToWidth(true);

        File location = new File("src/main/resources/pluginsWorkon");
        File[] fileList = location.listFiles();

        URL[] urls = new URL[fileList.length];
        for (int i = 0; i < fileList.length; i++){
            urls[i] = fileList[i].toURI().toURL();
        }

        URLClassLoader ucl = new URLClassLoader(urls);

        ServiceLoader<CoffeePluginInterface> sl = ServiceLoader.load(CoffeePluginInterface.class, ucl);
        Iterator<CoffeePluginInterface> apit = sl.iterator();

        while (apit.hasNext()){
            apit.next().coffeeAction();
        }
    }
}
