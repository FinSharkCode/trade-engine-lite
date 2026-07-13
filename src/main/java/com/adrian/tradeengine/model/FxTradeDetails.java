package com.adrian.tradeengine.model;

public class FxTradeDetails implements ProductDetails {

    private final String currencyPair;
    private final String settlementDate;
    private final double exchangeRate;

    public FxTradeDetails(String currencyPair, String settlementDate, double exchangeRate) {
        this.currencyPair = currencyPair;
        this.settlementDate = settlementDate;
        this.exchangeRate = exchangeRate;
    }

    public String getCurrencyPair() {
        return currencyPair;
    }

    public String getSettlementDate() {
        return settlementDate;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }
}