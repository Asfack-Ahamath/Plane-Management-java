package org.example.PlaneManagement;

class Person {
    // Variables to hold the person's basic information
    private String name;
    private String surname;
    private String email;

    // Constructor: Sets up a person with a name, surname, and email when created
    public Person(String name, String surname, String email) {
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    // Getters and Setters: These let you safely access and update the person's info
    public String get_name() {
        return name;
    }

    public void set_name(String name) {
        this.name = name;
    }

    public String get_surname() {
        return surname;
    }

    public void set_surname(String surname) {
        this.surname = surname;
    }

    public String get_email() {
        return email;
    }

    public void set_email(String email) {
        this.email = email;
    }

    // Converts person's info to a string format, handy for printing or logging
    @Override
    public String toString() {
        return "Name: " + name + " " + surname + ", Email: " + email;
    }
}
