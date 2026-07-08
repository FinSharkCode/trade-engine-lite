package com.adrian.tradeengine.service;

import com.adrian.tradeengine.model.Counterparty;
import com.adrian.tradeengine.model.Portfolio;
import com.adrian.tradeengine.model.ProductType;
import com.adrian.tradeengine.model.Trade;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class H2TradeRepositoryTest {

    @Test
    void shouldSaveAndLoadTradeFromH2() {
        String jdbcUrl = "jdbc:h2:mem:trade_repo_test_" + System.nanoTime() + ";DB_CLOSE_DELAY=-1";
        TradeRepository repository = new H2TradeRepository(jdbcUrl);

        Trade trade = new Trade(
                "H2_T1",
                ProductType.FX,
                1_000_000.0,
                "EUR",
                new Counterparty("Bank H2"),
                new Portfolio("Portfolio H2")
        );

        repository.save(trade);

        assertEquals(true, repository.existsByTradeId("H2_T1"));
        assertEquals(1, repository.count());

        List<Trade> trades = repository.findAll();

        assertEquals(1, trades.size());
        assertEquals("H2_T1", trades.get(0).getTradeId());
        assertEquals("Bank H2", trades.get(0).getCounterparty().getName());
        assertEquals("Portfolio H2", trades.get(0).getPortfolio().getName());
    }

    @Test
    void shouldKeepDataAcrossRepositoryInstancesForSameDatabase() {
        String jdbcUrl = "jdbc:h2:mem:trade_repo_test_" + System.nanoTime() + ";DB_CLOSE_DELAY=-1";

        TradeRepository firstRepository = new H2TradeRepository(jdbcUrl);

        Trade trade = new Trade(
                "H2_T2",
                ProductType.BOND,
                2_000_000.0,
                "EUR",
                new Counterparty("Bank Persist"),
                new Portfolio("Portfolio Persist")
        );

        firstRepository.save(trade);

        TradeRepository secondRepository = new H2TradeRepository(jdbcUrl);

        assertEquals(true, secondRepository.existsByTradeId("H2_T2"));
        assertEquals(1, secondRepository.findAll().size());
    }
}