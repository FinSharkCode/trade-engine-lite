package com.adrian.tradeengine.service;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BondPricingServiceTest {

    private final BondPricingService service = new BondPricingService();

    @Test
    void shouldCalculatePresentValueOfCashflows() {
        List<Cashflow> cashflows = Arrays.asList(
                new Cashflow(1, 30_000.0),
                new Cashflow(2, 30_000.0),
                new Cashflow(3, 30_000.0),
                new Cashflow(4, 30_000.0),
                new Cashflow(5, 1_030_000.0)
        );

        double presentValue = service.presentValue(cashflows, 0.04);

        assertEquals(955_481.78, presentValue, 0.01);
    }

    @Test
    void shouldReturnNominalWhenCouponRateEqualsDiscountRate() {
        BondCashflowService cashflowService = new BondCashflowService();

        List<Cashflow> cashflows = cashflowService.generateAnnualCashflows(
                1_000_000.0,
                0.04,
                5
        );

        double presentValue = service.presentValue(cashflows, 0.04);

        assertEquals(1_000_000.0, presentValue, 0.01);
    }

    @Test
    void shouldPriceBelowNominalWhenDiscountRateIsAboveCouponRate() {
        BondCashflowService cashflowService = new BondCashflowService();

        List<Cashflow> cashflows = cashflowService.generateAnnualCashflows(
                1_000_000.0,
                0.03,
                5
        );

        double presentValue = service.presentValue(cashflows, 0.04);

        assertTrue(presentValue < 1_000_000.0);
    }

    @Test
    void shouldPriceAboveNominalWhenDiscountRateIsBelowCouponRate() {
        BondCashflowService cashflowService = new BondCashflowService();

        List<Cashflow> cashflows = cashflowService.generateAnnualCashflows(
                1_000_000.0,
                0.05,
                5
        );

        double presentValue = service.presentValue(cashflows, 0.04);

        assertTrue(presentValue > 1_000_000.0);
    }
}