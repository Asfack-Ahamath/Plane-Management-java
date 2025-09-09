package org.example.PlaneManagement.gui;

import org.example.PlaneManagement.common.Person;
import org.example.PlaneManagement.common.Ticket;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.animation.*;
import javafx.util.Duration;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


public class FXController {
    // FXML elements - Modern JavaFX Components
    @FXML private TextField nameTextField;
    @FXML private TextField surnameTextField;
    @FXML private TextField emailTextField;
    @FXML private ToggleButton rowARadioButton;
    @FXML private ToggleButton rowBRadioButton;
    @FXML private ToggleButton rowCRadioButton;
    @FXML private ToggleButton rowDRadioButton;
    @FXML private ComboBox<Integer> seatComboBox;
    @FXML private Label priceLabel;
    @FXML private Button buyButton;
    @FXML private Button cancelButton;
    @FXML private Button findButton;
    @FXML private Button showButton;
    @FXML private Button printButton;
    @FXML private Button searchButton;
    @FXML private TextArea outputTextArea;
    
    // Statistics Labels
    @FXML private Label totalSeatsLabel;
    @FXML private Label bookedSeatsLabel;
    @FXML private Label availableSeatsLabel;
    @FXML private ProgressBar occupancyProgressBar;
    @FXML private Label occupancyLabel;
    
    // Seat map grid reference
    @FXML private VBox seatMapGrid;

    // Plane seating and ticket data
    private static final int[][] seats = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, // Row A
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, // Row B
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, // Row C
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0} // Row D
    };

    private static Ticket[] sold_tickets = new Ticket[0];
    private char selectedRow = '\0';
    private int selectedSeat = -1;

    @FXML
    public void initialize() {
        // Initialize with modern welcome message
        String welcomeMessage = """
            ✈️ Welcome to the Professional Airline Management System!
            
            🎯 Features Available:
            • Interactive seat selection with real-time pricing
            • Modern booking and cancellation system
            • Live statistics dashboard
            • Comprehensive reporting tools
            • Advanced search capabilities
            
            💡 Tip: Click on seats in the map or use the form to select your preferred seat.
            """;
        outputTextArea.setText(welcomeMessage);
        
        // Set up ToggleGroup for radio buttons
        ToggleGroup rowToggleGroup = new ToggleGroup();
        rowARadioButton.setToggleGroup(rowToggleGroup);
        rowBRadioButton.setToggleGroup(rowToggleGroup);
        rowCRadioButton.setToggleGroup(rowToggleGroup);
        rowDRadioButton.setToggleGroup(rowToggleGroup);

        // Enhanced event handlers with animations
        setupRowButtonHandlers();
        setupActionButtons();
        setupSeatMap();
        
        // Initialize statistics
        updateStatistics();
        
        // Add input validation
        setupInputValidation();
        
        // Add modern effects
        addModernEffects();
    }

    private void setupRowButtonHandlers() {
        rowARadioButton.setOnAction(e -> {
            handleRowSelection('A');
            animateButtonPress(rowARadioButton);
        });
        rowBRadioButton.setOnAction(e -> {
            handleRowSelection('B');
            animateButtonPress(rowBRadioButton);
        });
        rowCRadioButton.setOnAction(e -> {
            handleRowSelection('C');
            animateButtonPress(rowCRadioButton);
        });
        rowDRadioButton.setOnAction(e -> {
            handleRowSelection('D');
            animateButtonPress(rowDRadioButton);
        });
    }

    private void setupActionButtons() {
        buyButton.setOnAction(e -> {
            animateButtonPress(buyButton);
            buySeat();
        });

        cancelButton.setOnAction(e -> {
            animateButtonPress(cancelButton);
            cancelSeat();
        });

        findButton.setOnAction(e -> {
            animateButtonPress(findButton);
            showModernPopup("Find Available Seat", getFirstAvailableSeatInfo());
        });

        showButton.setOnAction(e -> {
            animateButtonPress(showButton);
            showModernPopup("Full Seat Map", getSeatMapDisplay());
        });

        printButton.setOnAction(e -> {
            animateButtonPress(printButton);
            showModernPopup("Booking Report", getTicketsReport());
        });

        searchButton.setOnAction(e -> {
            animateButtonPress(searchButton);
            showSearchDialog();
        });

        seatComboBox.setOnAction(e -> updatePrice());
    }

    private void setupSeatMap() {
        // Initialize seat buttons for interactive seat selection
        initializeSeatButtons();
        updateSeatMapDisplay();
    }

    private void initializeSeatButtons() {
        // Make all seat buttons interactive
        if (seatMapGrid != null) {
            for (int i = 1; i < seatMapGrid.getChildren().size(); i++) { // Skip header row
                HBox rowBox = (HBox) seatMapGrid.getChildren().get(i);
                if (rowBox != null) {
                    char rowChar = (char) ('A' + i - 1);
                    for (int j = 1; j < rowBox.getChildren().size(); j++) { // Skip row label
                        Button seatButton = (Button) rowBox.getChildren().get(j);
                        if (seatButton != null) {
                            int seatNumber = j;
                            seatButton.setOnAction(e -> handleSeatClick(rowChar, seatNumber));
                        }
                    }
                }
            }
        }
    }

    private void handleSeatClick(char row, int seatNumber) {
        // Handle seat button clicks
        if (isValidSeat(row, seatNumber)) {
            selectedRow = row;
            selectedSeat = seatNumber;
            
            // Update row selection
            switch (row) {
                case 'A': rowARadioButton.setSelected(true); break;
                case 'B': rowBRadioButton.setSelected(true); break;
                case 'C': rowCRadioButton.setSelected(true); break;
                case 'D': rowDRadioButton.setSelected(true); break;
            }
            
            // Update seat combo box
            updateSeatComboBox();
            seatComboBox.getSelectionModel().select(seatNumber - 1);
            
            // Update price
            updatePrice();
            
            // Visual feedback
            showInfoMessage(String.format("Seat %c%d selected. Price: £%.2f", row, seatNumber, calculate_price(row, seatNumber)));
        }
    }

    private void handleRowSelection(char row) {
        selectedRow = row;
        updateSeatComboBox();
        updatePrice();
        updateSeatMapDisplay();
    }

    private void updateSeatComboBox() {
        if (selectedRow != '\0') {
            int rowIndex = selectedRow - 'A';
            int maxSeats = seats[rowIndex].length;
            
            ObservableList<Integer> seatNumbers = FXCollections.observableArrayList();
            for (int i = 1; i <= maxSeats; i++) {
                seatNumbers.add(i);
            }
            
            seatComboBox.setItems(seatNumbers);
            seatComboBox.getSelectionModel().clearSelection();
        }
    }

    private void updatePrice() {
        if (selectedRow != '\0' && seatComboBox.getValue() != null) {
            int seatNumber = seatComboBox.getValue();
            double price = calculate_price(selectedRow, seatNumber);
            priceLabel.setText(String.format("£%.2f", price));
            
            // Add price animation
            animatePriceUpdate();
        } else {
            priceLabel.setText("");
        }
    }

    private void buySeat() {
        // Enhanced validation
        if (!validateBookingForm()) {
            return;
        }

        int seatNumber = seatComboBox.getValue();
        
        if (isValidSeat(selectedRow, seatNumber) && is_Seat_available(selectedRow, seatNumber)) {
            Person person = new Person(
                nameTextField.getText().trim(),
                surnameTextField.getText().trim(),
                emailTextField.getText().trim()
            );
            
            double price = calculate_price(selectedRow, seatNumber);
            Ticket ticket = new Ticket(selectedRow, seatNumber, price, person);
            
            mark_seat_as_sold(selectedRow, seatNumber);
            add_ticket(ticket);
            ticket.save();
            
            // Enhanced success feedback
            showSuccessMessage(String.format("🎉 Seat %c%d booked successfully for %s %s!", 
                selectedRow, seatNumber, person.get_name(), person.get_surname()));
            showInfoMessage(String.format("💰 Price: £%.2f | Ticket saved to: Tickets/%c%d.txt", 
                price, selectedRow, seatNumber));
            
            // Clear form and update UI
            clearInputFields();
            updateStatistics();
            updateSeatMapDisplay();
            
            // Add celebration animation
            animateSuccess();
        } else {
            showErrorMessage(String.format("❌ Seat %c%d is not available.", selectedRow, seatNumber));
        }
    }

    private void cancelSeat() {
        if (!validateSeatSelection()) {
            return;
        }

        int seatNumber = seatComboBox.getValue();

        if (isValidSeat(selectedRow, seatNumber) && !is_Seat_available(selectedRow, seatNumber)) {
            Ticket ticket = find_ticket(selectedRow, seatNumber);
            if (ticket != null) {
                mark_seat_as_available(selectedRow, seatNumber);
                ticket.delete_ticket_file();
                remove_ticket(selectedRow, seatNumber);
                
                showSuccessMessage(String.format("✅ Seat %c%d cancelled successfully!", selectedRow, seatNumber));
                showInfoMessage(String.format("💸 Refund amount: £%.2f", ticket.get_price()));
                
                updateStatistics();
                updateSeatMapDisplay();
                clearSelection();
                
                // Add cancellation animation
                animateCancellation();
            } else {
                showErrorMessage("❌ Ticket not found for this seat.");
            }
        } else {
            showErrorMessage(String.format("❌ Seat %c%d is already available.", selectedRow, seatNumber));
        }
    }

    private boolean validateBookingForm() {
        String name = nameTextField.getText().trim();
        String surname = surnameTextField.getText().trim();
        String email = emailTextField.getText().trim();

        if (name.isEmpty()) {
            showErrorMessage("❌ Please enter your first name.");
            nameTextField.requestFocus();
            return false;
        }
        if (surname.isEmpty()) {
            showErrorMessage("❌ Please enter your last name.");
            surnameTextField.requestFocus();
            return false;
        }
        if (email.isEmpty()) {
            showErrorMessage("❌ Please enter your email address.");
            emailTextField.requestFocus();
            return false;
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            showErrorMessage("❌ Please enter a valid email address.");
            emailTextField.requestFocus();
            return false;
        }
        if (selectedRow == '\0') {
            showErrorMessage("❌ Please select a row.");
            return false;
        }
        if (seatComboBox.getValue() == null) {
            showErrorMessage("❌ Please select a seat number.");
            return false;
        }

        return true;
    }

    private boolean validateSeatSelection() {
        if (selectedRow == '\0') {
            showErrorMessage("❌ Please select a row to cancel.");
            return false;
        }
        if (seatComboBox.getValue() == null) {
            showErrorMessage("❌ Please select a seat to cancel.");
            return false;
        }
        return true;
    }

    private Ticket find_ticket(char row, int seatNumber) {
            for (Ticket ticket : sold_tickets) {
            if (ticket.get_row() == row && ticket.get_seat() == seatNumber) {
                return ticket;
            }
        }
        return null;
    }

    private void updateSeatMapDisplay() {
        // Update the visual representation of seats
        if (seatMapGrid != null) {
            for (int i = 1; i < seatMapGrid.getChildren().size(); i++) { // Skip header row
                HBox rowBox = (HBox) seatMapGrid.getChildren().get(i);
                if (rowBox != null) {
                    char rowChar = (char) ('A' + i - 1);
                    int rowIndex = rowChar - 'A';
                    
                    for (int j = 1; j < rowBox.getChildren().size(); j++) { // Skip row label
                        Button seatButton = (Button) rowBox.getChildren().get(j);
                        if (seatButton != null) {
                            int seatNumber = j;
                            
                            // Update seat button appearance based on availability
                            if (seatNumber <= seats[rowIndex].length) {
                                if (seats[rowIndex][seatNumber - 1] == 1) {
                                    // Occupied seat
                                    seatButton.getStyleClass().clear();
                                    seatButton.getStyleClass().add("seat-button");
                                    seatButton.getStyleClass().add("occupied");
                                    seatButton.setText("✗");
                                } else {
                                    // Available seat
                                    seatButton.getStyleClass().clear();
                                    seatButton.getStyleClass().add("seat-button");
                                    seatButton.setText(String.valueOf(seatNumber));
                                }
                            }
                        }
                    }
                }
            }
        }
        updateStatistics();
    }

    private boolean isValidSeat(char row, int seatNumber) {
        int rowIndex = row - 'A';
        return rowIndex >= 0 && rowIndex < seats.length && seatNumber >= 1 && seatNumber <= seats[rowIndex].length;
    }

    private boolean is_Seat_available(char row, int seatNumber) {
        int rowIndex = row - 'A';
        int seatIndex = seatNumber - 1;
        return seats[rowIndex][seatIndex] == 0;
    }

    private void mark_seat_as_sold(char row, int seatNumber) {
        int rowIndex = row - 'A';
        int seatIndex = seatNumber - 1;
        seats[rowIndex][seatIndex] = 1;
    }

    private void mark_seat_as_available(char row, int seatNumber) {
        int rowIndex = row - 'A';
        int seatIndex = seatNumber - 1;
        seats[rowIndex][seatIndex] = 0;
    }

    private static double calculate_price(char row, int seat) {
        double price;
        switch (row) {
            case 'A':
            case 'B':
            case 'C':
            case 'D':
                if (seat >= 1 && seat <= 5) {
                    price = 200.0;
                } else if (seat >= 6 && seat <= 9) {
                    price = 150.0;
                } else if ((row == 'A' || row == 'D') && seat >= 10 && seat <= 14) {
                    price = 180.0;
                } else if ((row == 'B' || row == 'C') && seat >= 10 && seat <= 12) {
                    price = 180.0;
                } else {
                    price = 0.0;
                }
                break;
            default:
                price = 0.0;
        }
        return price;
    }

    private void add_ticket(Ticket ticket) {
        Ticket[] new_tickets = new Ticket[sold_tickets.length + 1];
        System.arraycopy(sold_tickets, 0, new_tickets, 0, sold_tickets.length);
        new_tickets[new_tickets.length - 1] = ticket;
        sold_tickets = new_tickets;
    }

    private void remove_ticket(char row, int seatNumber) {
        Ticket[] new_tickets = new Ticket[sold_tickets.length - 1];
        int index = 0;
        for (Ticket ticket : sold_tickets) {
            if (ticket.get_row() != row || ticket.get_seat() != seatNumber) {
                new_tickets[index++] = ticket;
            }
        }
        sold_tickets = new_tickets;
    }

    private void clearInputFields() {
        nameTextField.clear();
        surnameTextField.clear();
        emailTextField.clear();
        clearSelection();
    }

    private void clearSelection() {
        rowARadioButton.setSelected(false);
        rowBRadioButton.setSelected(false);
        rowCRadioButton.setSelected(false);
        rowDRadioButton.setSelected(false);
        seatComboBox.getSelectionModel().clearSelection();
        priceLabel.setText("");
        selectedRow = '\0';
        selectedSeat = -1;
    }
    
    // Modern UI Enhancement Methods
    
    private void animateButtonPress(javafx.scene.control.Control control) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(150), control);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(0.95);
        scaleTransition.setToY(0.95);
        scaleTransition.setAutoReverse(true);
        scaleTransition.setCycleCount(2);
        scaleTransition.play();
    }

    private void animatePriceUpdate() {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), priceLabel);
        fadeTransition.setFromValue(0.5);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
    }

    private void animateSuccess() {
        // Add success animation to the entire booking panel
        Glow glow = new Glow();
        glow.setLevel(0.5);
        buyButton.setEffect(glow);
        
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(1000), e -> buyButton.setEffect(null))
        );
        timeline.play();
    }

    private void animateCancellation() {
        // Add cancellation animation
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.RED);
        shadow.setRadius(10);
        cancelButton.setEffect(shadow);
        
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(1000), e -> cancelButton.setEffect(null))
        );
        timeline.play();
    }
    
    private void updateStatistics() {
        int totalSeats = 0;
        int bookedSeats = 0;
        
        for (int[] row : seats) {
            totalSeats += row.length;
            for (int seat : row) {
                if (seat == 1) bookedSeats++;
            }
        }
        
        int availableSeats = totalSeats - bookedSeats;
        double occupancyRate = totalSeats > 0 ? (double) bookedSeats / totalSeats : 0.0;
        
        totalSeatsLabel.setText(String.valueOf(totalSeats));
        bookedSeatsLabel.setText(String.valueOf(bookedSeats));
        availableSeatsLabel.setText(String.valueOf(availableSeats));
        
        if (occupancyProgressBar != null) {
            occupancyProgressBar.setProgress(occupancyRate);
        }
        
        if (occupancyLabel != null) {
            occupancyLabel.setText(String.format("%.1f%%", occupancyRate * 100));
        }
    }
    
    private void setupInputValidation() {
        // Email validation with visual feedback
        emailTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                if (newValue.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                    emailTextField.setStyle("-fx-border-color: #10b981; -fx-border-width: 2;");
                } else {
                    emailTextField.setStyle("-fx-border-color: #ef4444; -fx-border-width: 2;");
                }
            } else {
                emailTextField.setStyle("-fx-border-color: #d1d5db; -fx-border-width: 2;");
            }
        });
        
        // Name validation
        nameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                nameTextField.setStyle("-fx-border-color: #10b981; -fx-border-width: 2;");
            } else {
                nameTextField.setStyle("-fx-border-color: #d1d5db; -fx-border-width: 2;");
            }
        });
        
        // Surname validation
        surnameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                surnameTextField.setStyle("-fx-border-color: #10b981; -fx-border-width: 2;");
            } else {
                surnameTextField.setStyle("-fx-border-color: #d1d5db; -fx-border-width: 2;");
            }
        });
    }

    private void addModernEffects() {
        // Add subtle animations to form elements
        addHoverEffects();
        addFocusEffects();
    }

    private void addHoverEffects() {
        // Add hover effects to buttons
        buyButton.setOnMouseEntered(e -> {
            buyButton.setScaleX(1.02);
            buyButton.setScaleY(1.02);
        });
        buyButton.setOnMouseExited(e -> {
            buyButton.setScaleX(1.0);
            buyButton.setScaleY(1.0);
        });
    }

    private void addFocusEffects() {
        // Add focus effects to input fields
        nameTextField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                nameTextField.setStyle("-fx-border-color: #3b82f6; -fx-border-width: 2;");
            }
        });
    }
    
    private void showSuccessMessage(String message) {
        outputTextArea.appendText("\n" + message + "\n");
        updateStatistics();
    }
    
    private void showInfoMessage(String message) {
        outputTextArea.appendText("\n" + message + "\n");
    }
    
    private void showErrorMessage(String message) {
        outputTextArea.appendText("\n" + message + "\n");
    }
    
    private void showModernPopup(String title, String content) {
        Stage popupStage = new Stage();
        popupStage.setTitle(title);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initStyle(StageStyle.UTILITY);
        
        // Main container with CSS styling
        VBox mainContainer = new VBox();
        mainContainer.getStyleClass().add("popup-container");
        
        // Header section
        HBox headerBox = new HBox();
        headerBox.getStyleClass().add("popup-header");
        
        Label iconLabel = new Label("📋");
        iconLabel.getStyleClass().add("popup-icon");
        
        VBox titleBox = new VBox();
        titleBox.setSpacing(5);
        
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("popup-title");
        
        Label subtitleLabel = new Label("Professional Report");
        subtitleLabel.getStyleClass().add("popup-subtitle");
        
        titleBox.getChildren().addAll(titleLabel, subtitleLabel);
        headerBox.getChildren().addAll(iconLabel, titleBox);
        
        // Content section with card styling
        VBox contentCard = new VBox();
        contentCard.getStyleClass().add("popup-card");
        
        // Content header
        HBox contentHeader = new HBox();
        contentHeader.getStyleClass().add("popup-content-header");
        
        Label contentIcon = new Label("📊");
        contentIcon.getStyleClass().add("popup-content-icon");
        
        Label contentTitle = new Label("Report Details");
        contentTitle.getStyleClass().add("popup-content-title");
        
        contentHeader.getChildren().addAll(contentIcon, contentTitle);
        
        // Content area with CSS styling
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStyleClass().add("popup-scroll-pane");
        
        TextArea contentArea = new TextArea(content);
        contentArea.setEditable(false);
        contentArea.setWrapText(true);
        contentArea.getStyleClass().add("popup-text-area");
        
        scrollPane.setContent(contentArea);
        
        contentCard.getChildren().addAll(contentHeader, scrollPane);
        
        // Footer with action buttons
        HBox footerBox = new HBox();
        footerBox.getStyleClass().add("popup-footer");
        
        Button printButton = new Button("🖨️ Print");
        printButton.getStyleClass().addAll("popup-button", "popup-button-secondary");
        printButton.setOnAction(e -> {
            showInfoMessage("Print functionality would be implemented here");
        });
        
        Button closeButton = new Button("✓ Close");
        closeButton.getStyleClass().addAll("popup-button", "popup-button-primary");
        closeButton.setOnAction(e -> popupStage.close());
        
        footerBox.getChildren().addAll(printButton, closeButton);
        
        mainContainer.getChildren().addAll(headerBox, contentCard, footerBox);
        
        Scene popupScene = new Scene(mainContainer, 600, 500);
        
        // Load CSS stylesheet for the popup scene
        try {
            String cssPath = getClass().getResource("/org/example/PlaneManagement/gui/styles.css").toExternalForm();
            popupScene.getStylesheets().add(cssPath);
        } catch (Exception e) {
            System.out.println("Warning: Could not load CSS for popup: " + e.getMessage());
        }
        
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }
    
    private void showSearchDialog() {
        Stage searchStage = new Stage();
        searchStage.setTitle("Search Booking");
        searchStage.initModality(Modality.APPLICATION_MODAL);
        searchStage.initStyle(StageStyle.UTILITY);
        
        // Main container with CSS styling
        VBox mainContainer = new VBox();
        mainContainer.getStyleClass().add("popup-container");
        
        // Header section
        HBox headerBox = new HBox();
        headerBox.getStyleClass().add("popup-header");
        
        Label iconLabel = new Label("🔎");
        iconLabel.getStyleClass().add("popup-icon");
        
        VBox titleBox = new VBox();
        titleBox.setSpacing(5);
        
        Label titleLabel = new Label("Search Booking");
        titleLabel.getStyleClass().add("popup-title");
        
        Label subtitleLabel = new Label("Find passenger information");
        subtitleLabel.getStyleClass().add("popup-subtitle");
        
        titleBox.getChildren().addAll(titleLabel, subtitleLabel);
        headerBox.getChildren().addAll(iconLabel, titleBox);
        
        // Search input card
        VBox searchCard = new VBox();
        searchCard.getStyleClass().add("popup-card");
        
        // Search input section
        VBox inputSection = new VBox();
        inputSection.setSpacing(10);
        
        Label inputLabel = new Label("Search Criteria");
        inputLabel.getStyleClass().add("popup-section-title");
        
        TextField searchField = new TextField();
        searchField.setPromptText("Enter passenger name or email address");
        searchField.getStyleClass().add("popup-input-field");
        
        Button searchBtn = new Button("🔍 Search");
        searchBtn.getStyleClass().addAll("popup-button", "popup-button-success");
        
        inputSection.getChildren().addAll(inputLabel, searchField, searchBtn);
        
        // Results section
        VBox resultsSection = new VBox();
        resultsSection.setSpacing(10);
        
        Label resultsLabel = new Label("Search Results");
        resultsLabel.getStyleClass().add("popup-section-title");
        
        ScrollPane resultsScrollPane = new ScrollPane();
        resultsScrollPane.getStyleClass().add("popup-results-scroll");
        
        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setWrapText(true);
        resultArea.getStyleClass().add("popup-text-area");
        resultArea.setText("Enter search criteria above to find passenger information...");
        
        resultsScrollPane.setContent(resultArea);
        
        resultsSection.getChildren().addAll(resultsLabel, resultsScrollPane);
        
        searchCard.getChildren().addAll(inputSection, resultsSection);
        
        // Search functionality
        searchBtn.setOnAction(e -> {
            String searchTerm = searchField.getText().trim();
            if (!searchTerm.isEmpty()) {
                String results = searchBooking(searchTerm);
                resultArea.setText(results);
            } else {
                resultArea.setText("Please enter a search term...");
            }
        });
        
        // Footer with action buttons
        HBox footerBox = new HBox();
        footerBox.getStyleClass().add("popup-footer");
        
        Button clearButton = new Button("🗑️ Clear");
        clearButton.getStyleClass().addAll("popup-button", "popup-button-secondary");
        clearButton.setOnAction(e -> {
            searchField.clear();
            resultArea.setText("Enter search criteria above to find passenger information...");
        });
        
        Button closeButton = new Button("✓ Close");
        closeButton.getStyleClass().addAll("popup-button", "popup-button-primary");
        closeButton.setOnAction(e -> searchStage.close());
        
        footerBox.getChildren().addAll(clearButton, closeButton);
        
        mainContainer.getChildren().addAll(headerBox, searchCard, footerBox);
        
        Scene searchScene = new Scene(mainContainer, 500, 450);
        
        // Load CSS stylesheet for the search scene
        try {
            String cssPath = getClass().getResource("/org/example/PlaneManagement/gui/styles.css").toExternalForm();
            searchScene.getStylesheets().add(cssPath);
        } catch (Exception e) {
            System.out.println("Warning: Could not load CSS for search dialog: " + e.getMessage());
        }
        
        searchStage.setScene(searchScene);
        searchStage.showAndWait();
    }
    
    private String searchBooking(String searchTerm) {
        StringBuilder result = new StringBuilder();
        result.append("Search Results for: ").append(searchTerm).append("\n");
        result.append("=====================================\n");
        
        boolean found = false;
        for (Ticket ticket : sold_tickets) {
            if (ticket.get_person().get_name().toLowerCase().contains(searchTerm.toLowerCase()) ||
                ticket.get_person().get_surname().toLowerCase().contains(searchTerm.toLowerCase()) ||
                ticket.get_person().get_email().toLowerCase().contains(searchTerm.toLowerCase())) {
                result.append("Found: ").append(ticket.get_person().get_name()).append(" ")
                      .append(ticket.get_person().get_surname()).append("\n");
                result.append("Email: ").append(ticket.get_person().get_email()).append("\n");
                result.append("Seat: ").append(ticket.get_row()).append(ticket.get_seat()).append("\n");
                result.append("Price: £").append(ticket.get_price()).append("\n\n");
                found = true;
            }
        }
        
        if (!found) {
            result.append("No bookings found matching your search criteria.");
        }
        
        return result.toString();
    }
    
    private String getFirstAvailableSeatInfo() {
        StringBuilder result = new StringBuilder();
        result.append("AVAILABLE SEAT SEARCH\n");
        result.append("====================\n\n");
        
        for (int i = 0; i < seats.length; i++) {
            for (int j = 0; j < seats[i].length; j++) {
                if (seats[i][j] == 0) {
                    char row = (char) ('A' + i);
                    int seatNumber = j + 1;
                    double price = calculate_price(row, seatNumber);
                    result.append("✅ Available seat found: ").append(row).append(seatNumber).append("\n");
                    result.append("💰 Price: £").append(price).append("\n");
                    result.append("📍 Location: Row ").append(row).append(", Seat ").append(seatNumber).append("\n");
                    return result.toString();
                }
            }
        }
        return "❌ No available seats found.";
    }
    
    private String getSeatMapDisplay() {
        StringBuilder result = new StringBuilder();
        result.append("FULL SEAT MAP\n");
        result.append("=============\n\n");
        
        // Header
        result.append("   ");
        for (int j = 1; j <= 14; j++) {
            result.append(String.format("%2d ", j));
        }
        result.append("\n");
        
        // Rows
        for (int i = 0; i < seats.length; i++) {
            char row = (char) ('A' + i);
            result.append(row).append("  ");
            for (int j = 0; j < seats[i].length; j++) {
                if (seats[i][j] == 0) {
                    result.append("🟢 ");
                } else {
                    result.append("🔴 ");
                }
            }
            result.append("\n");
        }
        
        result.append("\nLegend: 🟢 Available, 🔴 Occupied\n");
        return result.toString();
    }
    
    private String getTicketsReport() {
        StringBuilder result = new StringBuilder();
        result.append("BOOKING REPORT\n");
        result.append("==============\n\n");
        
        if (sold_tickets.length == 0) {
            result.append("No tickets sold yet.\n");
        } else {
            double totalRevenue = 0;
            result.append("Total tickets sold: ").append(sold_tickets.length).append("\n\n");
            
            for (Ticket ticket : sold_tickets) {
                result.append("Seat: ").append(ticket.get_row()).append(ticket.get_seat()).append("\n");
                result.append("Passenger: ").append(ticket.get_person().get_name()).append(" ")
                      .append(ticket.get_person().get_surname()).append("\n");
                result.append("Email: ").append(ticket.get_person().get_email()).append("\n");
                result.append("Price: £").append(ticket.get_price()).append("\n");
                result.append("-------------------\n");
                totalRevenue += ticket.get_price();
            }
            
            result.append("\nTotal Revenue: £").append(String.format("%.2f", totalRevenue)).append("\n");
        }
        
        return result.toString();
    }
}