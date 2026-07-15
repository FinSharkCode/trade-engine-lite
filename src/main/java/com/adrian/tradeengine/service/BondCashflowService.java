package com.adrian.tradeengine.service;

import java.util.ArrayList;
import java.util.List;

public class BondCashflowService {

    public List<Cashflow> generateAnnualCashflows(
            double nominal,
            double couponRate,
            int yearsToMaturity
    ) {
        List<Cashflow> cashflows = new ArrayList<>();

        double coupon = nominal * couponRate;

        for (int year = 1; year <= yearsToMaturity; year++) {
            double amount = coupon;

            if (year == yearsToMaturity) {
                amount += nominal;
            }

            cashflows.add(new Cashflow(year, amount));
        }

        return cashflows;
    }
}