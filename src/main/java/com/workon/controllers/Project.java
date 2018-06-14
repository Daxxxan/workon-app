package com.workon.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.workon.models.Step;
import com.workon.models.User;
import com.workon.plugin.CoffeePluginInterface;
import com.workon.utils.HttpRequest;
import com.workon.utils.ParseRequestContent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class Project{
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

    private ArrayList<JFXTextField> textFieldCollaboratorArray = new ArrayList<>();
    private ArrayList<JFXTextField> textFieldStepNameArray = new ArrayList<>();
    private ArrayList<JFXDatePicker> datePickerStepArray = new ArrayList<>();

    private static ArrayList<User> usersArrayList = new ArrayList<>();
    private static ArrayList<Step> stepsArrayList = new ArrayList<>();

    private static ArrayList<User> getUsers() {
        return usersArrayList;
    }

    private static void setUser(User user) {
        Project.usersArrayList.add(user);
    }

    private static ArrayList<Step> getSteps() {
        return stepsArrayList;
    }

    private static void setStep(Step step) {
        Project.stepsArrayList.add(step);
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
        String request = "http://localhost:3000/api/accounts/".concat(Integer.toString(LoginConnection.getUserId())).concat("/projects");
        Map<String, String> parameters = new HashMap<>();
        parameters.put("name", projectNameTextField.getText());

        StringBuffer contentCreateProject = HttpRequest.setRequest(request, parameters, null, "POST", null, LoginConnection.getUserToken());

        //Add collaborators to project

        for(JFXTextField textField : getTextFieldCollaboratorArray()) {
            User user = new User(textField.getText());
            Project.setUser(user);
        }

        for(User user : Project.getUsers()){
            System.out.println(user.getEmail());
        }

        for(int i = 0; i < getTextFieldStepNameArray().size(); i++){
            Step step = new Step(getTextFieldStepNameArray().get(i).getText(), getDatePickerStepArray().get(i).getValue());
            Project.setStep(step);
        }

        for(Step step : Project.stepsArrayList){
            System.out.println(step.getName());
            System.out.println(step.getDate());
        }

        System.out.println(LoginConnection.getUserId());
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
