package com.workon.controllers;

import com.workon.utils.HttpRequest;
import com.workon.utils.LabelHelper;
import com.workon.utils.ParseRequestContent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

    private static String userId;
    private static String userToken;
    private static String path;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Récupération du path de l'api
        Properties properties = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("src/main/resources/properties/path.properties");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setPath(properties.getProperty("apiPath"));
    }

    public static String getUserId() {
        return userId;
    }

    private void setUserId(String userId) {
        LoginConnectionController.userId = userId;
    }

    public static String getUserToken() {
        return userToken;
    }

    private void setUserToken(String userToken) {
        LoginConnectionController.userToken = userToken;
    }

    public static String getPath() {
        return path;
    }

    public static void setPath(String path) {
        LoginConnectionController.path = path;
    }

    /**
     * Connection method
     */
    @FXML
    protected void handleLoginConnectionButtonAction() throws Exception {
        if(loginEmailField.getText().isEmpty() ) {
            LabelHelper.setLabel(loginEmailErrorLabel, "Saisissez votre adresse mail.", Pos.CENTER_LEFT, "#FF0000", new Font("Book Antiqua", 12));
        }else{
            loginEmailErrorLabel.setText("");
        }
        if(loginPasswordField.getText().isEmpty()){
            LabelHelper.setLabel(loginPasswordErrorLabel, "Saisissez votre mot de passe", Pos.CENTER_LEFT, "#FF0000", new Font("Book Antiqua", 12));
        }else{
            loginPasswordErrorLabel.setText("");
        }

        //Si tout est ok on test la connexion
        if(!(loginPasswordField.getText().isEmpty()) && !(loginEmailField.getText().isEmpty())){
            String content = HttpRequest.connection(loginEmailField.getText(), loginPasswordField.getText(), loginErrorConnectionLabel);

            if(content != null){
                //Récupération de l'id et du token user
                String userId = ParseRequestContent.getValueOf(content, "userId");
                String userToken = ParseRequestContent.getValueOf(content, "id");
                setUserId(userId);
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
    protected void handleRegisterValidateButtonAction() throws Exception {
        Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        if(registerEmailField.getText().isEmpty() || registerFirstnameField.getText().isEmpty()
                || registerLastnameField.getText().isEmpty() || registerPasswordField.getText().isEmpty()
                || registerPasswordVerifyField.getText().isEmpty()){
            LabelHelper.setLabel(registerErrorLabel, "Veuillez saisir tous les champs du formulaire", Pos.CENTER, "#FF0000", new Font("Book Antiqua", 16));
        }else if(!Objects.equals(registerPasswordField.getText(), registerPasswordVerifyField.getText())){
            LabelHelper.setLabel(registerErrorLabel, "Les mots de passe ne sont pas identiques", Pos.CENTER, "#FF0000", new Font("Book Antiqua", 16));
        }else{
            Matcher emailMatcher = emailPattern.matcher(registerEmailField.getText());
            if(emailMatcher.matches()){
                String content = HttpRequest.createAccount(registerLastnameField.getText(), registerFirstnameField.getText(),
                        registerEmailField.getText(), registerPasswordField.getText(), registerErrorLabel);
                if(content != null){
                    LabelHelper.setLabel(registerErrorLabel, "Votre compte a été créé avec succes", Pos.CENTER, "#00CD00", new Font("Book Antiqua", 16));
                }else{
                    LabelHelper.setLabel(registerErrorLabel, "Le compte existe déjà", Pos.CENTER, "#FF0000", new Font("Book Antiqua", 16));
                }
            }else{
                LabelHelper.setLabel(registerErrorLabel, "Votre email est inccorect", Pos.CENTER, "#FF0000", new Font("Book Antiqua", 16));
            }
        }
    }
}
