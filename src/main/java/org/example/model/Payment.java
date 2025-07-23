package org.example.model;

import java.time.LocalDate;

public class Payment {

    private User user;
    private Subscription subscription;
    private Employee employee;
    private LocalDate paymentDate;
    private int amountPaid;

    public Payment(User user, Subscription subscription, Employee employee, LocalDate paymentDate, int amountPaid) {
        this.user = user;
        this.subscription = subscription;
        this.employee = employee;
        this.paymentDate = paymentDate;
        this.amountPaid = amountPaid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public int getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(int amountPaid) {
        this.amountPaid = amountPaid;
    }

}
