package com.adrian.tradeengine.service;

import java.util.List;

public class BondPricingService {

    public double presentValue(List<Cashflow> cashflows, double discountRate) {
        double presentValue = 0.0;

        for (Cashflow cashflow : cashflows) {
            presentValue += cashflow.getAmount()
                    / Math.pow(1.0 + discountRate, cashflow.getYear());
        }

        return presentValue;
    }
}