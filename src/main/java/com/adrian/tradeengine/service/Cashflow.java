package com.adrian.tradeengine.service;

public class Cashflow {

    private final int year;
    private final double amount;

    public Cashflow(int year, double amount) {
        this.year = year;
        this.amount = amount;
    }

    public int getYear() {
        return year;
    }

    public double getAmount() {
        return amount;
    }
}