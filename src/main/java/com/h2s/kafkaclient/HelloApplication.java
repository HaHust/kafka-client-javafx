package com.h2s.kafkaclient;

import com.h2s.kafkaclient.controller.HelloController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        scene.getRoot().setStyle("-fx-font-family: 'Arial'");
        stage.setTitle("Hello!");
        stage.setScene(scene);
        HelloController controller =(HelloController) fxmlLoader.getController();
        controller.stopService(stage);
        stage.show();
    }

    @FXML
    public void exitApplication(ActionEvent event) {
        System.out.println("Exiting application...");
        Platform.exit();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch();
    }

}
