package org.example.model;

import java.time.LocalDate;

public class Employee {

    private int id;
    private String firstName;
    private String lastNmae;
    private LocalDate dateOfBirth;
    private String userName;
    private String password;
    private boolean isAdmin;
    private String phoneNumber;

    public Employee(int id, String firstName, String lastNmae, LocalDate dateOfBirth, String userName, boolean isAdmin, String phoneNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastNmae = lastNmae;
        this.dateOfBirth = dateOfBirth;
        this.userName = userName;
        this.isAdmin = isAdmin;
        this.phoneNumber = phoneNumber;
    }

    public Employee() {
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

    public String getLastNmae() {
        return lastNmae;
    }

    public void setLastNmae(String lastNmae) {
        this.lastNmae = lastNmae;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
