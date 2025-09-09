package org.example.PlaneManagement.cli;

import org.example.PlaneManagement.common.Person;
import org.example.PlaneManagement.common.Ticket;
import java.util.Scanner;

public class PlaneManagement {
    // 2D array representing the seating arrangement of the plane
    private static final int[][] seats = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, // Row A
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, // Row B
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, // Row C
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0} // Row D
    };

    // Array to store the sold tickets
    private static Ticket[] sold_tickets = new Ticket[0];

    public static void main(String[] args) {
        // Check if a welcome message should be displayed based on command-line arguments
        boolean displayWelcomeMessage = args.length == 0 || !args[0].equals("false");

        if (displayWelcomeMessage) {
            System.out.println("\n==========================================================");
            System.out.println("\t\tWelcome to the Plane Management Application");
        }

        Scanner scanner = new Scanner(System.in);

        while (true) {
            show_menu();
            System.out.print("Please select an option : ");
            int choice = get_valid_choice(scanner);
            scanner.nextLine(); // Consume any leftover newline character here

            switch (choice) {
                case 0:
                    // Exit the application
                    System.out.println("**********************************************************");
                    System.out.println("\n  Thank you for using the Plane Management Application.");
                    System.out.println("\n\t\t\t\t   Exiting Console UI...\n");
                    System.out.println("==========================================================");
                    return;
                case 1:
                    // Buy a seat
                    buy_seat(scanner);
                    enter_to_continue(scanner);
                    break;
                case 2:
                    // Cancel a seat
                    cancel_seat(scanner);
                    enter_to_continue(scanner);
                    break;
                case 3:
                    // Find the first available seat
                    find_first_available();
                    enter_to_continue(scanner);
                    break;
                case 4:
                    // Show the seating plan
                    show_seating_plan();
                    enter_to_continue(scanner);
                    break;
                case 5:
                    // Print tickets information and total sales
                    print_tickets_info();
                    enter_to_continue(scanner);
                    break;
                case 6:
                    // Search for a ticket
                    search_ticket(scanner);
                    enter_to_continue(scanner);
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

    // Prompt the user to press Enter to continue
    private static void enter_to_continue(Scanner scanner) {
        System.out.println("**********************************************************");
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    // Display the menu options
    private static void show_menu() {
        System.out.println("**********************************************************");
        System.out.println("*                      MENU OPTIONS                      *");
        System.out.println("**********************************************************");
        System.out.println("\t1) Buy a seat");
        System.out.println("\t2) Cancel a seat");
        System.out.println("\t3) Find first available seat");
        System.out.println("\t4) Show seating plan");
        System.out.println("\t5) Print tickets information and total sales");
        System.out.println("\t6) Search ticket");
        System.out.println("\t0) Quit");
        System.out.println("**********************************************************");
    }

    // Buy a seat
    private static void buy_seat(Scanner scanner) {
        char row = get_valid_row(scanner);
        if (row == '\0') {
            System.out.println("Invalid row, please try again.");
            return;
        }

        int seatNumber = get_valid_seat_number(scanner, row);
        if (seatNumber == -1) {
            System.out.println("Invalid seat number, please try again.");
            return;
        }

        if (is_Seat_available(row, seatNumber)) {
            String name = get_valid_name(scanner);
            String surname = get_valid_surname(scanner);
            String email = get_valid_email(scanner);

            Person person = new Person(name, surname, email);
            double price = calculate_price(row, seatNumber);
            Ticket ticket = new Ticket(row, seatNumber, price, person);
            mark_seat_as_sold(row, seatNumber);
            add_ticket(ticket);
            System.out.println("\nSeat has been booked successfully!!");
            ticket.save();
        } else {
            System.out.println("Seat not available.");
        }
    }

    // Cancel a seat
    private static void cancel_seat(Scanner scanner) {
        char row = get_valid_row(scanner);
        if (row == '\0') {
            System.out.println("Invalid row, please try again.");
            return;
        }

        int seatNumber = get_valid_seat_number(scanner, row);
        if (seatNumber == -1) {
            System.out.println("Invalid seat number, please try again.");
            return;
        }

        if (!is_Seat_available(row, seatNumber)) {
            System.out.println("\nAre you sure you want to cancel the seat ?");
            System.out.print("\t\t\t\t\t\t(Type Yes or No) : ");
            String confirmation = scanner.nextLine().trim().toLowerCase();

            if (confirmation.equals("yes")) {
                Ticket cancelledTicket = null;
                for (Ticket ticket : sold_tickets) {
                    if (ticket.get_row() == row && ticket.get_seat() == seatNumber) {
                        cancelledTicket = ticket;
                        break;
                    }
                }

                if (cancelledTicket != null) {
                    mark_seat_as_available(row, seatNumber);
                    remove_ticket(row, seatNumber);
                    cancelledTicket.delete_ticket_file(); // Delete the ticket file
                    System.out.println("\n\t\t       Seat Cancelled Successfully!\n");
                } else {
                    System.out.println("\n\t\t\t   No Ticket Found for this Seat.\n");
                }
            } else if (confirmation.equals("no")) {
                System.out.println("\n\t\t\t     Seat Cancellation Aborted.\n");
            } else {
                System.out.println("\n\t\t\tInvalid Input. Seat Cancellation Aborted.\n");
            }
        } else {
            System.out.println("\n\t\t\t This Seat is Already Available.\n");
        }
    }

    // Find the first available seat
    private static void find_first_available() {
        for (int row = 0; row < seats.length; row++) {
            for (int seat = 0; seat < seats[row].length; seat++) {
                if (is_Seat_available((char) ('A' + row), seat + 1)) {
                    System.out.println("**********************************************************");
                    System.out.println("\n\t\t        First Available Seat : " + (char) ('A' + row) + (seat + 1) + "\n");
                    return;
                }
            }
        }
        System.out.println("No seats available.");
    }

    // Show the seating plan
    private static void show_seating_plan() {
        System.out.println("**********************************************************\n");
        System.out.println("                   ────────────────");
        System.out.println("                     SEATING PLAN");
        System.out.println("                   ────────────────\n");
        System.out.print("\t");

        // Determine the maximum number of seats in any row to align the headers
        int maxSeats = 0;
        for (int[] row : seats) {
            if (row.length > maxSeats) {
                maxSeats = row.length;
            }
        }

        // Print seat numbers
        for (int seatNum = 1; seatNum <= maxSeats; seatNum++) {
            System.out.print(seatNum + "\t");
        }
        System.out.println("\n");

        // Print seat availability for each row
        for (int row = 0; row < seats.length; row++) {
            System.out.print((char) ('A' + row) + "\t");
            for (int seat = 0; seat < seats[row].length; seat++) {
                char symbol = seats[row][seat] == 0 ? 'O' : 'X'; // Assuming 0 means available, 1 means taken
                System.out.print(symbol + "\t");
            }
            System.out.println();

            // Check if current row is 'D' and if so, print an additional newline
            if ((char) ('A' + row) == 'D') {
                System.out.println();
            }
        }
    }

    // Print tickets information and total sales
    public static void print_tickets_info() {
        // Check if soldTickets array is empty
        if (sold_tickets.length == 0) {
            System.out.println("**********************************************************");
            System.out.println("\n\t\t     No Ticket Information Available!");
            System.out.println("\n\t\t\t\t   Total Sales = £0.00\n");
        } else {
            double total_price = 0;
            System.out.println("**********************************************************\n");
            System.out.println("                                   TICKETS INFORMATION");
            System.out.println("=============================================================================================");
            System.out.printf("| %-4s | %-4s | %-7s | %-15s | %-20s | %-25s |\n", "Row", "Seat", "Price", "Name", "Surname", "Email");
            System.out.println("=============================================================================================");

            // Iterate over soldTickets and print details
            for (Ticket ticket : sold_tickets) {
                total_price += ticket.get_price();
                System.out.printf("| %-4s | %-4d | £%-6.2f | %-15s | %-20s | %-25s |\n",
                        String.valueOf(ticket.get_row()), ticket.get_seat(), ticket.get_price(),
                        ticket.get_person().get_name(), ticket.get_person().get_surname(), ticket.get_person().get_email());
            }
            System.out.println("=============================================================================================");
            System.out.printf("\nTotal Sales :   £%.2f\n", total_price);
            System.out.println();
        }
    }

    // Search for a ticket
    private static void search_ticket(Scanner scanner) {
        char row = get_valid_row(scanner);
        if (row == '\0') {
            System.out.println("Invalid row, please try again.");
            return;
        }

        int seatNumber = get_valid_seat_number(scanner, row);
        if (seatNumber == -1) {
            System.out.println("Invalid seat number, please try again.");
            return;
        }

        for (Ticket ticket : sold_tickets) {
            if (ticket.get_row() == row && ticket.get_seat() == seatNumber) {
                Person person = ticket.get_person(); // Assuming Ticket class has getPerson() method
                System.out.println("\n==================================");
                System.out.println("        Person Information");
                System.out.println("==================================");
                System.out.println("   Name  :  " + person.get_name());
                System.out.println("Surname  :  " + person.get_surname());
                System.out.println("  Email  :  " + person.get_email());
                System.out.println("==================================");
                System.out.println("        Ticket Information");
                System.out.println("==================================");
                System.out.println("   Row        Seat        Price   ");
                System.out.println("──────────────────────────────────");
                System.out.printf("   %2s          %2d         £%.1f%n", ticket.get_row(), ticket.get_seat(), ticket.get_price());
                System.out.println("──────────────────────────────────\n");
                return;
            }
        }
        System.out.println("\n\t\t\t\t\t Ticket Not Found.\n");
    }

    // Check if a seat is valid based on the row and seat number
    private static boolean isValidSeat(char row, int seatNumber) {
        int rowIndex = row - 'A';
        return rowIndex >= 0 && rowIndex < seats.length && seatNumber >= 1 && seatNumber <= seats[rowIndex].length;
    }

    // Check if a seat is available
    private static boolean is_Seat_available(char row, int seatNumber) {
        int rowIndex = row - 'A';
        int seatIndex = seatNumber - 1;
        return seats[rowIndex][seatIndex] == 0;
    }

    // Mark a seat as sold
    private static void mark_seat_as_sold(char row, int seatNumber) {
        int rowIndex = row - 'A';
        int seatIndex = seatNumber - 1;
        seats[rowIndex][seatIndex] = 1;
    }

    // Mark a seat as available
    private static void mark_seat_as_available(char row, int seatNumber) {
        int rowIndex = row - 'A';
        int seatIndex = seatNumber - 1;
        seats[rowIndex][seatIndex] = 0;
    }

    // Calculate the price of a seat based on the row and seat number
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
                    price = 0.0; // Invalid seat, set price to 0 or handle accordingly
                }
                break;

            default:
                price = 0.0; // Invalid row, set price to 0 or handle accordingly
        }

        return price;
    }

    // Add a ticket to the sold_tickets array
    private static void add_ticket(Ticket ticket) {
        Ticket[] new_tickets = new Ticket[sold_tickets.length + 1];
        System.arraycopy(sold_tickets, 0, new_tickets, 0, sold_tickets.length);
        new_tickets[new_tickets.length - 1] = ticket;
        sold_tickets = new_tickets;
    }

    // Remove a ticket from the sold_tickets array
    private static void remove_ticket(char row, int seatNumber) {
        Ticket[] new_tickets = new Ticket[sold_tickets.length - 1];
        int index = 0;
        for (Ticket ticket : sold_tickets) {
            if (ticket.get_row() != row || ticket.get_seat() != seatNumber) {
                new_tickets[index++] = ticket;
            }
        }
        sold_tickets = new_tickets;
    }

    // Get a valid menu choice from the user
    private static int get_valid_choice(Scanner scanner) {
        while (true) {
            try {
                int choice = scanner.nextInt();
                if (choice >= 0 && choice <= 6) {
                    return choice;
                } else {
                    System.out.println("Invalid choice, please try again.");
                    System.out.print("Please select an option: "); // Prompt again after an invalid choice
                }
            } catch (Exception e) {
                System.out.println("Invalid input, please try again.");
                scanner.nextLine(); // Clear the invalid input before prompting again
                System.out.print("Please select an option: ");
            }
        }
    }

    // Get a valid row input from the user
    private static char get_valid_row(Scanner scanner) {
        while (true) {
            System.out.println("**********************************************************");
            System.out.print("\nEnter Row (A - D) : ");
            String input = scanner.nextLine().toUpperCase();
            if (input.length() == 1 && input.charAt(0) >= 'A' && input.charAt(0) <= 'D') {
                return input.charAt(0);
            }
            System.out.println("Invalid row input, please enter a row between A and D.");
        }
    }

    // Get a valid seat number input from the user
    private static int get_valid_seat_number(Scanner scanner, char row) {
        int rowIndex = row - 'A';
        int maxSeats = seats[rowIndex].length;
        while (true) {
            System.out.print("Enter Seat Number : ");
            try {
                int seat_number = scanner.nextInt();
                scanner.nextLine(); // Consume the remaining newline character
                if (seat_number >= 1 && seat_number <= maxSeats) {
                    return seat_number;
                } else {
                    System.out.println("Invalid seat number, please try again.");
                }
            } catch (Exception e) {
                System.out.println("Invalid seat number input, please try again.");
                scanner.nextLine(); // Clear the invalid input
            }
        }
    }

    // Get a valid name input from the user
    private static String get_valid_name(Scanner scanner) {
        while (true) {
            System.out.print("\n       Enter Name : ");
            String name = scanner.nextLine().trim();
            if (!name.isEmpty()) {
                return name;
            } else {
                System.out.println("Name cannot be empty, please try again.");
            }
        }
    }

    // Get a valid surname input from the user
    private static String get_valid_surname(Scanner scanner) {
        while (true) {
            System.out.print("    Enter Surname : ");
            String surname = scanner.nextLine().trim();
            if (!surname.isEmpty()) {
                return surname;
            } else {
                System.out.println("Surname cannot be empty, please try again.");
            }
        }
    }

    // Get a valid email input from the user
    private static String get_valid_email(Scanner scanner) {
        while (true) {
            System.out.print("      Enter Email : ");
            String email = scanner.nextLine().trim();
            if (!email.isEmpty() && email.contains("@") && email.contains(".")) {
                return email;
            } else {
                System.out.println("Invalid email format, please try again.");
            }
        }
    }
}