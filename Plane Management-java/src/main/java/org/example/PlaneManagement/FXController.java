package org.example.PlaneManagement;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class FXController {
    // FXML elements
    @FXML private TextField nameTextField;
    @FXML private TextField surnameTextField;
    @FXML private TextField emailTextField;
    @FXML private RadioButton rowARadioButton;
    @FXML private RadioButton rowBRadioButton;
    @FXML private RadioButton rowCRadioButton;
    @FXML private RadioButton rowDRadioButton;
    @FXML private ComboBox<Character> rowComboBox;
    @FXML private ComboBox<Integer> seatComboBox;
    @FXML private Label priceLabel;
    @FXML private Button buyButton;
    @FXML private Button cancelButton;
    @FXML private Button findButton;
    @FXML private Button showButton;
    @FXML private Button printButton;
    @FXML private Button searchButton;
    @FXML private TextArea outputTextArea;

    // Plane seating and ticket data
    private static final int[][] seats = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, // Row A
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, // Row B
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, // Row C
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0} // Row D
    };

    private void clearSelection() {
        // Clear selected row and seat, and price label
        rowARadioButton.setSelected(false);
        rowBRadioButton.setSelected(false);
        rowCRadioButton.setSelected(false);
        rowDRadioButton.setSelected(false);
        seatComboBox.getSelectionModel().clearSelection();
        priceLabel.setText(""); // Clear the price label
    }
    private static Ticket[] sold_tickets = new Ticket[0];

    @FXML
    public void initialize() {
        // Initialize FXML elements and event handlers
        String asterisks = "*".repeat(76);
        String welcomeMessage = "\n\t\tWelcome to the Plane Management Application\n";
        outputTextArea.setText(asterisks + "\n" + welcomeMessage + "\n" + asterisks + "\n");
        // Set up a ToggleGroup for the row radio buttons
        ToggleGroup rowToggleGroup = new ToggleGroup();
        rowARadioButton.setToggleGroup(rowToggleGroup);
        rowBRadioButton.setToggleGroup(rowToggleGroup);
        rowCRadioButton.setToggleGroup(rowToggleGroup);
        rowDRadioButton.setToggleGroup(rowToggleGroup);

        // Add event handlers for the row radio buttons
        rowARadioButton.setOnAction(e -> {
            updateSeatComboBox();
            updatePrice();
        });
        rowBRadioButton.setOnAction(e -> {
            updateSeatComboBox();
            updatePrice();
        });
        rowCRadioButton.setOnAction(e -> {
            updateSeatComboBox();
            updatePrice();
        });
        rowDRadioButton.setOnAction(e -> {
            updateSeatComboBox();
            updatePrice();
        });

        buyButton.setOnAction(e -> {
            clearSelection();
            buySeat();
        });

        cancelButton.setOnAction(e -> {
            clearSelection();
            cancelSeat();
        });

        findButton.setOnAction(e -> {
            clearSelection();
            findFirstAvailable();
        });

        showButton.setOnAction(e -> {
            clearSelection();
            showSeatingPlan();
        });

        printButton.setOnAction(e -> {
            clearSelection();
            printTicketsInfo();
        });

        searchButton.setOnAction(e -> {
            clearSelection();
            searchTicket();
        });

        seatComboBox.setOnAction(e -> updatePrice());
        buyButton.setOnAction(e -> buySeat());
        cancelButton.setOnAction(e -> cancelSeat());
        findButton.setOnAction(e -> findFirstAvailable());
        showButton.setOnAction(e -> showSeatingPlan());
        printButton.setOnAction(e -> printTicketsInfo());
        searchButton.setOnAction(e -> searchTicket());
    }

    private void updateSeatComboBox() {
        // Update seat combo box based on selected row
        char selectedRow = getSelectedRow();
        if (selectedRow != '\0') {
            int rowIndex = selectedRow - 'A';
            int maxSeats = seats[rowIndex].length;
            seatComboBox.getItems().clear();
            for (int i = 1; i <= maxSeats; i++) {
                seatComboBox.getItems().add(i);        }
        }
    }

    private void updatePrice() {
        // Update price label based on selected row and seat
        char selectedRow = getSelectedRow();
        if (selectedRow != '\0' && seatComboBox.getValue() != null) {
            int selectedSeat = seatComboBox.getValue();
            double price = calculate_price(selectedRow, selectedSeat);
            priceLabel.setText(String.format("£%.2f", price));
        }
    }

    private void buySeat() {
        // Check if a row and seat are selected
        char selectedRow = getSelectedRow();
        if (selectedRow == '\0') {
            outputTextArea.appendText("\nPlease select a row.\n");
            return;
        }
        if (seatComboBox.getValue() == null) {
            outputTextArea.appendText("\nPlease select a seat.\n");
            return;
        }

        int selectedSeat = seatComboBox.getValue();
        String name = nameTextField.getText();
        String surname = surnameTextField.getText();
        String email = emailTextField.getText();

        // Validate if name, surname, and email are not empty
        if (name.isEmpty()) {
            outputTextArea.appendText("\nPlease enter a name.\n");
            return;
        }
        if (surname.isEmpty()) {
            outputTextArea.appendText("\nPlease enter a surname.\n");
            return;
        }
        if (email.isEmpty()) {
            outputTextArea.appendText("\nPlease enter an email.\n");
            return;
        }

        // Validate email format
        if (!email.isEmpty() && email.contains("@") && email.contains(".")) {
            // Email is valid, continue with the booking process
            if (isValidSeat(selectedRow, selectedSeat) && is_Seat_available(selectedRow, selectedSeat)) {
                Person person = new Person(name, surname, email);
                double price = calculate_price(selectedRow, selectedSeat);
                Ticket ticket = new Ticket(selectedRow, selectedSeat, price, person);
                mark_seat_as_sold(selectedRow, selectedSeat);
                add_ticket(ticket);

                // Show message and ticket name
                outputTextArea.appendText("\nSeat has been booked successfully!!");
                String ticketName = String.format("Tickets/%c%01d.txt", selectedRow, selectedSeat);
                outputTextArea.appendText("\n\nTicket Information Saved to File : " + ticketName + "\n");

                // Save ticket (assuming Ticket class has a save() method)
                ticket.save();

                clearInputFields();
            } else {
                outputTextArea.appendText("\nSeat not available.");
            }
        } else {
            outputTextArea.appendText("\nPlease enter a valid email address.\n");
        }
    }

    private Ticket find_ticket(char row, int seatNumber) {
        // Find a ticket based on row and seat number
        for (Ticket ticket : sold_tickets) {
            if (ticket.get_row() == row && ticket.get_seat() == seatNumber) {
                return ticket;
            }
        }
        return null;
    }
    private void cancelSeat() {
        // Check if a row and seat are selected
        char selectedRow = getSelectedRow();
        if (selectedRow == '\0') {
            outputTextArea.appendText("\nPlease select a row to cancel.\n");
            return;
        }
        if (seatComboBox.getValue() == null) {
            outputTextArea.appendText("\nPlease select a seat to cancel.\n");
            return;
        }

        int selectedSeat = seatComboBox.getValue();

        if (isValidSeat(selectedRow, selectedSeat) && !is_Seat_available(selectedRow, selectedSeat)) {
            mark_seat_as_available(selectedRow, selectedSeat);
            Ticket ticket = find_ticket(selectedRow, selectedSeat);
            if (ticket != null) {
                ticket.delete_ticket_file();
                remove_ticket(selectedRow, selectedSeat);
                outputTextArea.appendText("\nSeat Cancelled Successfully!\n");
            } else {
                outputTextArea.appendText("\nTicket Not Found.\n");
            }
        } else {
            outputTextArea.appendText("\nThis Seat is Already Available.\n");
        }
        clearSelection();
    }

    private void findFirstAvailable() {
        // Find the first available seat
        for (int row = 0; row < seats.length; row++) {
            for (int seat = 0; seat < seats[row].length; seat++) {
                if (is_Seat_available((char) ('A' + row), seat + 1)) {
                    outputTextArea.appendText("\nFirst Available Seat : " + (char) ('A' + row) + (seat + 1) + "\n");
                    return;
                }
            }
        }
        outputTextArea.appendText("\nNo seats available.");
        clearSelection();
    }

    private void showSeatingPlan() {
        // Display the seating plan
        outputTextArea.clear();
        outputTextArea.appendText("\n\t\t             ================");
        outputTextArea.appendText("\n\t\t               SEATING PLAN");
        outputTextArea.appendText("\n\t\t             ================\n");
        outputTextArea.appendText("\n  ");

        // Print seat numbers
        for (int seatNum = 1; seatNum <= 14; seatNum++) {
            outputTextArea.appendText(String.format("  %2d ", seatNum));
        }
        outputTextArea.appendText("\n\n");

        // Print seat availability for each row
        for (int row = 0; row < seats.length; row++) {
            outputTextArea.appendText((char) ('A' + row) + "  ");
            for (int seat = 0; seat < seats[row].length; seat++) {
                char symbol = seats[row][seat] == 0 ? 'O' : 'X';
                outputTextArea.appendText("  " + symbol + "  ");
            }
            outputTextArea.appendText("\n");

            // Check if current row is 'B' or 'C' and if so, print spaces for the remaining seats
            if ((char) ('A' + row) == 'B' || (char) ('A' + row) == 'C') {
                outputTextArea.appendText(""); // Print 4 spaces for the remaining 2 seats
            }
        }
        clearSelection();
    }

    public void printTicketsInfo() {
        // Print information about sold tickets
        outputTextArea.clear();
        // Check if soldTickets array is empty
        if (sold_tickets.length == 0) {
            outputTextArea.appendText("\nNo Ticket Information Available!");
            outputTextArea.appendText("\n\nTotal Sales = £0.00\n");
        } else {
            double totalPrice = 0;
            outputTextArea.appendText("\n                             TICKETS INFORMATION\n");
            outputTextArea.appendText("\n============================================================================");
            outputTextArea.appendText(String.format("\n| %-4s | %-4s | %-7s | %-12s | %-12s | %-18s |", "Row", "Seat", "Price", "Name", "Surname", "Email"));
            outputTextArea.appendText("\n============================================================================");

            // Iterate over soldTickets and print details
            for (Ticket ticket : sold_tickets) {
                totalPrice += ticket.get_price();
                outputTextArea.appendText(String.format("\n| %-4s | %-4d | £%-6.2f | %-12s | %-12s | %-18s |",
                        String.valueOf(ticket.get_row()), ticket.get_seat(), ticket.get_price(),
                        ticket.get_person().get_name(), ticket.get_person().get_surname(), ticket.get_person().get_email()));
            }
            outputTextArea.appendText("\n============================================================================");
            outputTextArea.appendText(String.format("\n\nTotal Sales :   £%.2f\n", totalPrice));
        }
        clearSelection();
    }

    private void searchTicket() {
        // Search for a ticket based on row and seat selection
        // Check if a row and seat are selected
        char selectedRow = getSelectedRow();
        if (selectedRow == '\0') {
            outputTextArea.appendText("\nPlease select a row to search.\n");
            return;
        }
        if (seatComboBox.getValue() == null) {
            outputTextArea.appendText("\nPlease select a seat to search.\n");
            return;
        }

        int selectedSeat = seatComboBox.getValue();

        for (Ticket ticket : sold_tickets) {
            outputTextArea.clear();
            if (ticket.get_row() == selectedRow && ticket.get_seat() == selectedSeat) {
                Person person = ticket.get_person();
                outputTextArea.appendText("\n==================================");
                outputTextArea.appendText("\n        Person Information");
                outputTextArea.appendText("\n==================================\n");
                outputTextArea.appendText("\n   Name  :  " + person.get_name());
                outputTextArea.appendText("\nSurname  :  " + person.get_surname());
                outputTextArea.appendText("\n  Email  :  " + person.get_email());
                outputTextArea.appendText("\n\n==================================");
                outputTextArea.appendText("\n        Ticket Information");
                outputTextArea.appendText("\n==================================\n");
                outputTextArea.appendText("\n   Row        Seat        Price   ");
                outputTextArea.appendText("\n──────────────────────────────────");
                outputTextArea.appendText(String.format("\n   %2s          %2d         £%.1f%n", ticket.get_row(), ticket.get_seat(), ticket.get_price()));
                outputTextArea.appendText("──────────────────────────────────");
                return;
            }
        }
        outputTextArea.appendText("\nTicket Not Found.\n");
        clearSelection();
    }

    private char getSelectedRow() {
        // Get the selected row based on radio button selection
        if (rowARadioButton.isSelected()) {
            return 'A';
        } else if (rowBRadioButton.isSelected()) {
            return 'B';
        } else if (rowCRadioButton.isSelected()) {
            return 'C';
        } else if (rowDRadioButton.isSelected()) {
            return 'D';
        } else {
            return '\0'; // Return null character if no row is selected
        }
    }

    private boolean isValidSeat(char row, int seatNumber) {
        // Check if the seat is valid based on row and seat number
        int rowIndex = row - 'A';
        return rowIndex >= 0 && rowIndex < seats.length && seatNumber >= 1 && seatNumber <= seats[rowIndex].length;
    }

    private boolean is_Seat_available(char row, int seatNumber) {
        // Check if the seat is available
        int rowIndex = row - 'A';
        int seatIndex = seatNumber - 1;
        return seats[rowIndex][seatIndex] == 0;
    }

    private void mark_seat_as_sold(char row, int seatNumber) {
        // Mark the seat as sold
        int rowIndex = row - 'A';
        int seatIndex = seatNumber - 1;
        seats[rowIndex][seatIndex] = 1;
    }

    private void mark_seat_as_available(char row, int seatNumber) {
        // Mark the seat as available
        int rowIndex = row - 'A';
        int seatIndex = seatNumber - 1;
        seats[rowIndex][seatIndex] = 0;
    }

    private static double calculate_price(char row, int seat) {
        // Calculate the price of the seat based on row and seat number
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
                    price = 0.0; // Invalid seat, set price to 0 or handle accordingly
                }
                break;

            default:
                price = 0.0; // Invalid row, set price to 0 or handle accordingly
        }

        return price;
    }

    private void add_ticket(Ticket ticket) {
        // Add a ticket to the sold_tickets array
        Ticket[] new_tickets = new Ticket[sold_tickets.length + 1];
        System.arraycopy(sold_tickets, 0, new_tickets, 0, sold_tickets.length);
        new_tickets[new_tickets.length - 1] = ticket;
        sold_tickets = new_tickets;
    }

    private void remove_ticket(char row, int seatNumber) {
        // Remove a ticket from the sold_tickets array based on row and seat number
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
        // Clear the input fields and reset selections
        nameTextField.clear();
        surnameTextField.clear();
        emailTextField.clear();
        rowARadioButton.setSelected(false);
        rowBRadioButton.setSelected(false);
        rowCRadioButton.setSelected(false);
        rowDRadioButton.setSelected(false);
        seatComboBox.getSelectionModel().clearSelection();
        priceLabel.setText("");
    }
}