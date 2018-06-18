package com.workon;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception{
        System.out.println(getClass());
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/connection.fxml"));
        primaryStage.setTitle("Connexion");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/workOn.png")));
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
