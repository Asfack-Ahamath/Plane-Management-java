package org.example.PlaneManagement.common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Ticket {
    private char row;
    private int seat;
    private double price;
    private Person person;

    // This is the constructor that sets up a new Ticket with a row, seat, price, and person
    public Ticket(char row, int seat, double price, Person person) {
        this.row = row;
        this.seat = seat;
        this.price = price;
        this.person = person;
    }

    // These are getters and setters for the Ticket's row, seat, price, and person
    public char get_row() {
        return row;
    }

    public void set_row(char row) {
        this.row = row;
    }

    public int get_seat() {
        return seat;
    }

    public void set_seat(int seat) {
        this.seat = seat;
    }

    public double get_price() {
        return price;
    }

    public void set_price(double price) {
        this.price = price;
    }

    public Person get_person() {
        return person;
    }

    public void set_person(Person person) {
        this.person = person;
    }

    // This method deletes the ticket file for this Ticket
    public void delete_ticket_file() {
        String fileName = "Tickets/" + row + String.valueOf(seat) + ".txt";
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
    }

    // This method saves the Ticket info to a file
    public void save() {
        // If the "Tickets" folder doesn't exist, create it
        File folder = new File("Tickets");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // Build the file name using the Ticket's row and seat
        String fileName = "Tickets/" + row + String.valueOf(seat) + ".txt";
        try (FileWriter writer = new FileWriter(fileName)) {
            // Write the ticket info to the file in a neat format
            writer.write("**********************************\n");
            writer.write("*          PLANE TICKET          *\n");
            writer.write("**********************************\n\n");

            writer.write("==================================\n");
            writer.write("        Person Information\n");
            writer.write("==================================\n\n");
            writer.write("   Name  :  " + person.get_name() + "\n");
            writer.write("Surname  :  " + person.get_surname() + "\n");
            writer.write("  Email  :  " + person.get_email() + "\n");
            writer.write("\n==================================\n");

            writer.write("        Ticket Information        \n");
            writer.write("==================================\n\n");
            writer.write("   Row        Seat        Price   \n");
            writer.write("─────────────────────────────\n");
            writer.write(String.format("   %2s          %2d         £%.1f%n", row, seat, price));
            writer.write("─────────────────────────────\n");

            System.out.println("Ticket Information Saved to File : " + fileName + "\n");
        } catch (IOException e) {
            System.out.println("Error occurred while saving ticket information to file: " + e.getMessage());
        }
    }

    // This toString method returns a string with the Ticket's key info
    @Override
    public String toString() {
        return "Ticket: Row " + row + ", Seat " + seat + ", Price: £" + price + "\n" + person;
    }
}