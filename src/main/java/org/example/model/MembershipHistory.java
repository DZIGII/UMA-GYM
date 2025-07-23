package org.example.model;

public class MembershipHistory {

    private User user;
    private int month;
    private int year;
    private Subscription subscription;
    private Employee employee;

    public MembershipHistory(User user, int month, int year, Subscription subscription, Employee employee) {
        this.user = user;
        this.month = month;
        this.year = year;
        this.subscription = subscription;
        this.employee = employee;
    }

    public MembershipHistory() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return "MembershipHistory{" +
                "user=" + user +
                ", month=" + month +
                ", year=" + year +
                ", subscription=" + subscription +
                ", employee=" + employee +
                '}';
    }
}
