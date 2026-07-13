package com.adrian.tradeengine.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TradeTest {

    @Test
    void shouldCreateTradeCorrectly() {
        Counterparty counterparty = new Counterparty("Bank A");
        Portfolio portfolio = new Portfolio("Portfolio 1");
        FxTradeDetails productDetails = new FxTradeDetails(
                "EUR/USD",
                "2026-07-15",
                1.08
        );

        Trade trade = new Trade(
                "T1",
                ProductType.FX,
                1_000_000.0,
                "EUR",
                counterparty,
                portfolio,
                productDetails
        );

        assertEquals("T1", trade.getTradeId());
        assertEquals(ProductType.FX, trade.getProductType());
        assertEquals(1_000_000.0, trade.getNominal());
        assertEquals("EUR", trade.getCurrency());
        assertEquals("Bank A", trade.getCounterparty().getName());
        assertEquals("Portfolio 1", trade.getPortfolio().getName());
        assertEquals(productDetails, trade.getProductDetails());
    }
}