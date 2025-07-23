package org.example.model;

public class MonthStats {

    private String month;
    private int numOfClients;
    private int profit;

    public MonthStats(String month, int numOfClients, int profit) {
        this.month = month;
        this.numOfClients = numOfClients;
        this.profit = profit;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getNumOfClients() {
        return numOfClients;
    }

    public void setNumOfClients(int numOfClients) {
        this.numOfClients = numOfClients;
    }

    public int getProfit() {
        return profit;
    }

    public void setProfit(int profit) {
        this.profit = profit;
    }
}
