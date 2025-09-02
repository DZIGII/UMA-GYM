package org.example.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Subscription {

    private int id;
    private String name;
    private int price;
    private boolean treadmillIncluded;
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private User user;
    private Employee employee;
    private List<Payment> payments;

    public Subscription(String name, int price, boolean treadmillIncluded, LocalDate dateStart, LocalDate dateEnd, User user, Employee employee) {
        this.name = name;
        this.price = price;
        this.treadmillIncluded = treadmillIncluded;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.user = user;
        this.employee = employee;
        this.payments = new ArrayList<>();
    }


    public Subscription(int id, String name, int price, boolean treadmillIncluded, LocalDate dateStart, LocalDate dateEnd, User user, Employee employee) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.treadmillIncluded = treadmillIncluded;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.user = user;
        this.employee = employee;
        this.payments = new ArrayList<>();
    }

    public Subscription() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String isTreadmillIncluded() {
        return treadmillIncluded ? "Da" : "Ne";
    }

    public void setTreadmillIncluded(boolean treadmillIncluded) {
        this.treadmillIncluded = treadmillIncluded;
    }

    public LocalDate getDateStart() {
        return dateStart;
    }

    public void setDateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
    }

    public LocalDate getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public void addPayment(Payment payment) {
        payments.add(payment);
    }

    public String getDebt() {

        int sum = 0;
        for (Payment p : payments) {
            sum += p.getAmountPaid();
        }

        return (price - sum) + " din";
    }

    public String getFirstName() {
        return user.getFirstName();
    }

    public String getLastName() {
        return user.getLastName();
    }

    public String getDateOfBirth() {
        return user.getDateOfBirth().toString();
    }

    public String getEmployeeFullName() {
        return employee.getFirstName() + " " + employee.getLastNmae();
    }

    public String getEndIn() {
        long daysBetween = ChronoUnit.DAYS.between(LocalDate.now(), dateEnd);
        return daysBetween + " dana";
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", treadmillIncluded=" + treadmillIncluded +
                ", dateStart=" + dateStart +
                ", dateEnd=" + dateEnd +
                ", user=" + user +
                ", employee=" + employee +
                ", payments=" + payments +
                '}';
    }
}
