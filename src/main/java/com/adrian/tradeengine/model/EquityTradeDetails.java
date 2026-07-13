package com.adrian.tradeengine.model;

public class EquityTradeDetails implements ProductDetails {

    private final String ticker;
    private final String exchange;
    private final int quantity;
    private final double price;

    public EquityTradeDetails(String ticker, String exchange, int quantity, double price) {
        this.ticker = ticker;
        this.exchange = exchange;
        this.quantity = quantity;
        this.price = price;
    }

    public String getTicker() {
        return ticker;
    }

    public String getExchange() {
        return exchange;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
}