package com.adrian.tradeengine.service;

import com.adrian.tradeengine.model.Counterparty;
import com.adrian.tradeengine.model.FxTradeDetails;
import com.adrian.tradeengine.model.Portfolio;
import com.adrian.tradeengine.model.ProductType;
import com.adrian.tradeengine.model.Trade;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TradeRepositoryTest {

    @Test
    void shouldSaveAndReturnTrades() {
        TradeRepository repository = new InMemoryTradeRepository();

        Trade trade = new Trade(
                "T1",
                ProductType.FX,
                1_000_000.0,
                "EUR",
                new Counterparty("Bank A"),
                new Portfolio("Portfolio 1"),
                new FxTradeDetails("EUR/USD", "2026-07-15", 1.08)
        );

        repository.save(trade);

        List<Trade> trades = repository.findAll();

        assertEquals(1, trades.size());
        assertEquals("T1", trades.get(0).getTradeId());
        assertEquals(1, repository.count());
        assertTrue(trades.get(0).getProductDetails() instanceof FxTradeDetails);
    }

    @Test
    void shouldDetectExistingTradeId() {
        TradeRepository repository = new InMemoryTradeRepository();

        Trade trade = new Trade(
                "T2",
                ProductType.FX,
                1_000_000.0,
                "EUR",
                new Counterparty("Bank A"),
                new Portfolio("Portfolio 1"),
                new FxTradeDetails("EUR/USD", "2026-07-15", 1.08)
        );

        repository.save(trade);

        assertEquals(true, repository.existsByTradeId("T2"));
        assertEquals(false, repository.existsByTradeId("UNKNOWN"));
    }
}