package com.workon.controllers;

import com.jfoenix.controls.JFXButton;
import com.workon.utils.ButtonHelper;
import com.workon.utils.HttpRequest;
import com.workon.utils.LabelHelper;
import com.workon.utils.ParseRequestContent;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.LightBase;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginConnectionController implements Initializable {
    /**
     * Connection fields
     */
    @FXML
    private TextField loginEmailField;
    @FXML
    private PasswordField loginPasswordField;
    @FXML
    private Button loginConnectionButton;
    @FXML
    private Label loginEmailErrorLabel;
    @FXML
    private Label loginPasswordErrorLabel;
    @FXML
    private Label loginErrorConnectionLabel;

    /**
     * Registration fields
     */
    @FXML
    private TextField registerEmailField;
    @FXML
    private TextField registerLastnameField;
    @FXML
    private TextField registerFirstnameField;
    @FXML
    private PasswordField registerPasswordField;
    @FXML
    private PasswordField registerPasswordVerifyField;
    @FXML
    private Label registerErrorLabel;
    @FXML
    private ScrollPane mainScrollPane;

    private static Integer userId;
    private static String userToken;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    public static Integer getUserId() {
        return userId;
    }

    private void setUserId(Integer userId) {
        LoginConnectionController.userId = userId;
    }

    public static String getUserToken() {
        return userToken;
    }

    private void setUserToken(String userToken) {
        LoginConnectionController.userToken = userToken;
    }

    /**
     * Connection method
     */
    @FXML
    protected void handleLoginConnectionButtonAction() throws IOException{
        if(loginEmailField.getText().isEmpty() ) {
            LabelHelper.setLabel(loginEmailErrorLabel, "Saisissez votre adresse mail.", Pos.CENTER_LEFT, "#FF0000");
        }else{
            loginEmailErrorLabel.setText("");
        }
        if(loginPasswordField.getText().isEmpty()){
            LabelHelper.setLabel(loginPasswordErrorLabel, "Saisissez votre mot de passe", Pos.CENTER_LEFT, "#FF0000");
        }else{
            loginPasswordErrorLabel.setText("");
        }

        //Si tout est ok on test la connexion
        if(!(loginPasswordField.getText().isEmpty()) && !(loginEmailField.getText().isEmpty())){
            //Set des parameters
            Map<String, String> parameters = new HashMap<>();
            parameters.put("email", loginEmailField.getText());
            parameters.put("password", loginPasswordField.getText());

            //Lancement de la requete
            StringBuffer content = HttpRequest.setRequest("http://localhost:3000/api/accounts/login", parameters, loginErrorConnectionLabel, "POST", "Le compte n'existe pas", null);
            if(content != null){
                //Récupération de l'id et du token user
                String userId = ParseRequestContent.getValueOf(content.toString(), "userId");
                String userToken = ParseRequestContent.getValueOf(content.toString(), "id");
                setUserId(Integer.parseInt(userId));
                setUserToken(userToken.substring(1, userToken.length() - 1));

                //Load de la nouvelle scene
                Parent mainParent = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
                Scene mainScene = new Scene(mainParent);
                Stage primaryStage = (Stage) loginConnectionButton.getScene().getWindow();
                primaryStage.setResizable(true);
                primaryStage.setScene(mainScene);
                primaryStage.setMaximized(true);
                primaryStage.show();
            }
        }
    }

    /**
     * Registration method
     */
    @FXML
    protected void handleRegisterValidateButtonAction() throws IOException {
        Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        if(registerEmailField.getText().isEmpty() || registerFirstnameField.getText().isEmpty()
                || registerLastnameField.getText().isEmpty() || registerPasswordField.getText().isEmpty()
                || registerPasswordVerifyField.getText().isEmpty()){
            LabelHelper.setLabel(registerErrorLabel, "Veuillez saisir tous les champs du formulaire", Pos.CENTER, "#FF0000");
        }else if(!Objects.equals(registerPasswordField.getText(), registerPasswordVerifyField.getText())){
            LabelHelper.setLabel(registerErrorLabel, "Les mots de passe ne sont pas identiques", Pos.CENTER, "#FF0000");
        }else{
            Matcher emailMatcher = emailPattern.matcher(registerEmailField.getText());
            if(emailMatcher.matches()){
                Map<String, String> parameters = new HashMap<>();
                parameters.put("lastname", registerLastnameField.getText());
                parameters.put("firstname", registerFirstnameField.getText());
                parameters.put("email", registerEmailField.getText());
                parameters.put("password", registerPasswordField.getText());

                StringBuffer content = HttpRequest.setRequest("http://localhost:3000/api/accounts", parameters, registerErrorLabel, "POST", "Le compte existe déjà !", null);
                if(content != null){
                    LabelHelper.setLabel(registerErrorLabel, "Votre compte a été créé avec succes", Pos.CENTER, "#00CD00");
                }
            }else{
                LabelHelper.setLabel(registerErrorLabel, "Votre email est inccorect", Pos.CENTER, "#FF0000");
            }
        }
    }
}
