package org.example.PlaneManagement.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class JavaFXUI extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        try {
            System.out.println("🚀 Starting Plane Management GUI...");
            
            // Load the FXML file for the user interface
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/PlaneManagement/gui/plane_managementUI.fxml"));
            System.out.println("📄 FXML loader created successfully");

            // Create a new Scene using the loaded FXML file
            Scene scene = new Scene(loader.load());
            System.out.println("🎭 Scene created successfully");
            
            // Load CSS stylesheet programmatically
            try {
                String cssPath = getClass().getResource("/org/example/PlaneManagement/gui/styles.css").toExternalForm();
                scene.getStylesheets().add(cssPath);
                System.out.println("✅ CSS loaded successfully: " + cssPath);
            } catch (Exception e) {
                System.out.println("⚠️ Warning: Could not load CSS stylesheet: " + e.getMessage());
            }

        // Set the title of the primary stage (window)
        primaryStage.setTitle("Airline Management System - Professional Edition");

        // Set appropriate window size to prevent hiding content
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(600);
        primaryStage.setWidth(1200);
        primaryStage.setHeight(700);

        // Set the scene of the primary stage
        primaryStage.setScene(scene);

            // Show the primary stage (make the window visible)
            primaryStage.show();
            System.out.println("🎉 GUI launched successfully!");
            
        } catch (Exception e) {
            System.err.println("❌ Error launching GUI: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}