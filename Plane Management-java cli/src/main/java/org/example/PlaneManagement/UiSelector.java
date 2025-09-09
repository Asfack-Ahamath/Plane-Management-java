package org.example.PlaneManagement;

import org.example.PlaneManagement.cli.PlaneManagement;
import org.example.PlaneManagement.gui.JavaFXUI;
import java.util.InputMismatchException;
import java.util.Scanner;

public class UiSelector {
    // Create a Scanner object for user input
    private static Scanner scanner = new Scanner(System.in);
    // Flag to check if it's the first run of the application
    private static boolean isFirstRun = true;

    public static void main(String[] args) {
        while (true) {
            if (isFirstRun) {
                // Display welcome message if it's the first run
                System.out.println("\n==========================================================");
                System.out.println("\t\tWelcome to the Plane Management Application");
                isFirstRun = false;
            }

            // Display menu options
            System.out.println("**********************************************************");
            System.out.println("*                Select Your Preferred UI                *");
            System.out.println("**********************************************************");
            System.out.println("\t1) Console UI");
            System.out.println("\t2) JavaFX UI");
            System.out.println("\t0) Exit");
            System.out.println("**********************************************************");
            System.out.print("Enter Your Choice : ");

            try {
                // Read user input for menu choice
                int choice = scanner.nextInt();
                System.out.println(); // Add an empty line after user input

                if (choice == 1) {
                    // Launch the Console UI if choice is 1
                    PlaneManagement.main(new String[]{"false"});
                } else if (choice == 2) {
                    // Launch the JavaFX UI if choice is 2
                    JavaFXUI.launch(JavaFXUI.class, args);
                } else if (choice == 0) {
                    // Exit the application if choice is 0
                    System.out.println("**********************************************************");
                    System.out.println("\n  Thank you for using the Plane Management Application.");
                    System.out.println("\n\t\t\t\tExiting The Programme...\n");
                    System.out.println("==========================================================");
                    System.exit(0);
                } else {
                    // Display an error message for invalid choice
                    System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                // Handle invalid input (non-numeric input)
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // Clear the invalid input from the scanner
            }
        }
    }
}