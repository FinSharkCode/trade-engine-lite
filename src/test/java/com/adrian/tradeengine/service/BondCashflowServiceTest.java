package com.adrian.tradeengine.service;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BondCashflowServiceTest {

    private final BondCashflowService service = new BondCashflowService();

    @Test
    void shouldGenerateAnnualFixedCouponCashflows() {
        List<Cashflow> cashflows = service.generateAnnualCashflows(
                1_000_000.0,
                0.03,
                5
        );

        assertEquals(5, cashflows.size());

        assertEquals(1, cashflows.get(0).getYear());
        assertEquals(30_000.0, cashflows.get(0).getAmount());

        assertEquals(2, cashflows.get(1).getYear());
        assertEquals(30_000.0, cashflows.get(1).getAmount());

        assertEquals(3, cashflows.get(2).getYear());
        assertEquals(30_000.0, cashflows.get(2).getAmount());

        assertEquals(4, cashflows.get(3).getYear());
        assertEquals(30_000.0, cashflows.get(3).getAmount());

        assertEquals(5, cashflows.get(4).getYear());
        assertEquals(1_030_000.0, cashflows.get(4).getAmount());
    }

    @Test
    void shouldGenerateSingleFinalCashflowForZeroCouponBond() {
        List<Cashflow> cashflows = service.generateAnnualCashflows(
                1_000_000.0,
                0.0,
                3
        );

        assertEquals(3, cashflows.size());

        assertEquals(0.0, cashflows.get(0).getAmount());
        assertEquals(0.0, cashflows.get(1).getAmount());
        assertEquals(1_000_000.0, cashflows.get(2).getAmount());
    }
}