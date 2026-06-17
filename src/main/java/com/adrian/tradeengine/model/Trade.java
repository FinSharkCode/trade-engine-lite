package com.adrian.tradeengine.model;

public class Trade {

    private final String tradeId;
    private final ProductType productType;
    private final double nominal;
    private final String currency;
    private final Counterparty counterparty;
    private final Portfolio portfolio;

    public Trade(String tradeId,
                 ProductType productType,
                 double nominal,
                 String currency,
                 Counterparty counterparty,
                 Portfolio portfolio) {
        this.tradeId = tradeId;
        this.productType = productType;
        this.nominal = nominal;
        this.currency = currency;
        this.counterparty = counterparty;
        this.portfolio = portfolio;
    }

    public String getTradeId() {
        return tradeId;
    }

    public ProductType getProductType() {
        return productType;
    }

    public double getNominal() {
        return nominal;
    }

    public String getCurrency() {
        return currency;
    }

    public Counterparty getCounterparty() {
        return counterparty;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }
}