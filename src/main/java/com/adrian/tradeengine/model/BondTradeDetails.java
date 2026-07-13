package com.adrian.tradeengine.model;

public class BondTradeDetails implements ProductDetails {

    private final String isin;
    private final String issuer;
    private final String maturityDate;
    private final double couponRate;

    public BondTradeDetails(String isin, String issuer, String maturityDate, double couponRate) {
        this.isin = isin;
        this.issuer = issuer;
        this.maturityDate = maturityDate;
        this.couponRate = couponRate;
    }

    public String getIsin() {
        return isin;
    }

    public String getIssuer() {
        return issuer;
    }

    public String getMaturityDate() {
        return maturityDate;
    }

    public double getCouponRate() {
        return couponRate;
    }
}