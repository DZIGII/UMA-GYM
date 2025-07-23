package org.example.model;

import java.time.LocalDate;

public class User {

    private int id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String note;
    private Subscription subscription;

    public User(int id, String firstName, String lastName, LocalDate dateOfBirth, String note) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDate getEndDate() {
        return subscription.getDateEnd();
    }

    public int getDebt() {
        return Integer.parseInt(subscription.getDebt());
    }
}
