package com.workon.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.workon.plugin.CoffeePluginInterface;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.ServiceLoader;

public class Project {
    @FXML
    private ScrollPane mainScrollPane;

    @FXML
    private VBox vboxCollaborators;

    @FXML
    private VBox vboxStep;

    @FXML
    private JFXButton validateProjectButton;

    @FXML
    private JFXButton createBugButton;

    @FXML
    private JFXButton createEvolutionButton;

    @FXML
    private JFXButton createDocumentationButton;

    private ArrayList<JFXTextField> textFieldCollaboratorArray = new ArrayList<>();
    private ArrayList<JFXTextField> textFieldStepNameArray = new ArrayList<>();;
    private ArrayList<JFXDatePicker> datePickerStepArray = new ArrayList<>();;

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
        for(JFXTextField textField : getTextFieldCollaboratorArray()) {
            System.out.println(textField.getText());
        }

        for(int i = 0; i < getTextFieldStepNameArray().size(); i++){
            System.out.println(getTextFieldStepNameArray().get(i).getText());
            System.out.println(getDatePickerStepArray().get(i).getValue());
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
