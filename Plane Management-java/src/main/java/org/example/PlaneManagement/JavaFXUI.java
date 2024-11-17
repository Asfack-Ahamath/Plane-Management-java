package org.example.PlaneManagement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class JavaFXUI extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        // Load the FXML file for the user interface
        FXMLLoader loader = new FXMLLoader(getClass().getResource("plane_managementUI.fxml"));

        // Create a new Scene using the loaded FXML file
        Scene scene = new Scene(loader.load());

        // Set the title of the primary stage (window)
        primaryStage.setTitle("Plane Management Application");

        // Set the scene of the primary stage
        primaryStage.setScene(scene);

        // Show the primary stage (make the window visible)
        primaryStage.show();
    }
}