package com.adrian.tradeengine.service;

import com.adrian.tradeengine.model.Counterparty;
import com.adrian.tradeengine.model.Portfolio;
import com.adrian.tradeengine.model.ProductType;
import com.adrian.tradeengine.model.Trade;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TradeAnalyticsTest {

    private final TradeAnalytics analytics = new TradeAnalytics();

    @Test
    void shouldFilterByProductType() {
        List<Trade> trades = List.of(
                new Trade("T1", ProductType.FX, 1_000_000.0, "EUR",
                        new Counterparty("Bank A"), new Portfolio("Portfolio 1")),
                new Trade("T2", ProductType.BOND, 2_000_000.0, "EUR",
                        new Counterparty("Bank B"), new Portfolio("Portfolio 1")),
                new Trade("T3", ProductType.FX, 500_000.0, "USD",
                        new Counterparty("Bank A"), new Portfolio("Portfolio 2"))
        );

        List<Trade> fxTrades = analytics.filterByProductType(trades, ProductType.FX);

        assertEquals(2, fxTrades.size());
    }

    @Test
    void shouldGroupByCounterparty() {
        List<Trade> trades = List.of(
                new Trade("T1", ProductType.FX, 1_000_000.0, "EUR",
                        new Counterparty("Bank A"), new Portfolio("Portfolio 1")),
                new Trade("T2", ProductType.BOND, 2_000_000.0, "EUR",
                        new Counterparty("Bank B"), new Portfolio("Portfolio 1")),
                new Trade("T3", ProductType.FX, 500_000.0, "USD",
                        new Counterparty("Bank A"), new Portfolio("Portfolio 2"))
        );

        Map<String, List<Trade>> grouped = analytics.groupByCounterparty(trades);

        assertEquals(2, grouped.size());
        assertEquals(2, grouped.get("Bank A").size());
        assertEquals(1, grouped.get("Bank B").size());
    }

    @Test
    void shouldSumNominalByPortfolio() {
        List<Trade> trades = List.of(
                new Trade("T1", ProductType.FX, 1_000_000.0, "EUR",
                        new Counterparty("Bank A"), new Portfolio("Portfolio 1")),
                new Trade("T2", ProductType.BOND, 2_000_000.0, "EUR",
                        new Counterparty("Bank B"), new Portfolio("Portfolio 1")),
                new Trade("T3", ProductType.FX, 500_000.0, "USD",
                        new Counterparty("Bank A"), new Portfolio("Portfolio 2"))
        );

        Map<String, Double> sums = analytics.sumNominalByPortfolio(trades);

        assertEquals(3_000_000.0, sums.get("Portfolio 1"));
        assertEquals(500_000.0, sums.get("Portfolio 2"));
    }
}