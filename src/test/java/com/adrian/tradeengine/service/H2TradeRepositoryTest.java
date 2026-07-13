package com.adrian.tradeengine.service;

import com.adrian.tradeengine.model.BondTradeDetails;
import com.adrian.tradeengine.model.Counterparty;
import com.adrian.tradeengine.model.EquityTradeDetails;
import com.adrian.tradeengine.model.FxTradeDetails;
import com.adrian.tradeengine.model.Portfolio;
import com.adrian.tradeengine.model.ProductDetails;
import com.adrian.tradeengine.model.ProductType;
import com.adrian.tradeengine.model.Trade;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class H2TradeRepositoryTest {

    @Test
    void shouldSaveAndLoadFxTradeDetailsFromH2() {
        String jdbcUrl = "jdbc:h2:mem:trade_repo_test_" + System.nanoTime() + ";DB_CLOSE_DELAY=-1";
        TradeRepository repository = new H2TradeRepository(jdbcUrl);

        Trade trade = new Trade(
                "H2_FX_1",
                ProductType.FX,
                1_000_000.0,
                "EUR",
                new Counterparty("Bank H2 FX"),
                new Portfolio("Portfolio H2 FX"),
                new FxTradeDetails("EUR/USD", "2026-07-15", 1.08)
        );

        repository.save(trade);

        Trade loadedTrade = repository.findByTradeId("H2_FX_1");

        assertEquals("H2_FX_1", loadedTrade.getTradeId());
        assertEquals(ProductType.FX, loadedTrade.getProductType());
        assertTrue(loadedTrade.getProductDetails() instanceof FxTradeDetails);

        FxTradeDetails details = (FxTradeDetails) loadedTrade.getProductDetails();

        assertEquals("EUR/USD", details.getCurrencyPair());
        assertEquals("2026-07-15", details.getSettlementDate());
        assertEquals(1.08, details.getExchangeRate());
    }

    @Test
    void shouldSaveAndLoadBondTradeDetailsFromH2() {
        String jdbcUrl = "jdbc:h2:mem:trade_repo_test_" + System.nanoTime() + ";DB_CLOSE_DELAY=-1";
        TradeRepository repository = new H2TradeRepository(jdbcUrl);

        Trade trade = new Trade(
                "H2_BOND_1",
                ProductType.BOND,
                2_000_000.0,
                "EUR",
                new Counterparty("Bank H2 Bond"),
                new Portfolio("Portfolio H2 Bond"),
                new BondTradeDetails("DE0001234567", "Issuer A", "2030-12-31", 0.035)
        );

        repository.save(trade);

        Trade loadedTrade = repository.findByTradeId("H2_BOND_1");

        assertEquals("H2_BOND_1", loadedTrade.getTradeId());
        assertEquals(ProductType.BOND, loadedTrade.getProductType());
        assertTrue(loadedTrade.getProductDetails() instanceof BondTradeDetails);

        BondTradeDetails details = (BondTradeDetails) loadedTrade.getProductDetails();

        assertEquals("DE0001234567", details.getIsin());
        assertEquals("Issuer A", details.getIssuer());
        assertEquals("2030-12-31", details.getMaturityDate());
        assertEquals(0.035, details.getCouponRate());
    }

    @Test
    void shouldSaveAndLoadEquityTradeDetailsFromH2() {
        String jdbcUrl = "jdbc:h2:mem:trade_repo_test_" + System.nanoTime() + ";DB_CLOSE_DELAY=-1";
        TradeRepository repository = new H2TradeRepository(jdbcUrl);

        Trade trade = new Trade(
                "H2_EQ_1",
                ProductType.EQUITY,
                500_000.0,
                "USD",
                new Counterparty("Bank H2 Equity"),
                new Portfolio("Portfolio H2 Equity"),
                new EquityTradeDetails("AAPL", "NASDAQ", 100, 195.50)
        );

        repository.save(trade);

        Trade loadedTrade = repository.findByTradeId("H2_EQ_1");

        assertEquals("H2_EQ_1", loadedTrade.getTradeId());
        assertEquals(ProductType.EQUITY, loadedTrade.getProductType());
        assertTrue(loadedTrade.getProductDetails() instanceof EquityTradeDetails);

        EquityTradeDetails details = (EquityTradeDetails) loadedTrade.getProductDetails();

        assertEquals("AAPL", details.getTicker());
        assertEquals("NASDAQ", details.getExchange());
        assertEquals(100, details.getQuantity());
        assertEquals(195.50, details.getPrice());
    }

    @Test
    void shouldKeepDataAcrossRepositoryInstancesForSameDatabase() {
        String jdbcUrl = "jdbc:h2:mem:trade_repo_test_" + System.nanoTime() + ";DB_CLOSE_DELAY=-1";

        TradeRepository firstRepository = new H2TradeRepository(jdbcUrl);

        Trade trade = new Trade(
                "H2_PERSIST_1",
                ProductType.BOND,
                2_000_000.0,
                "EUR",
                new Counterparty("Bank Persist"),
                new Portfolio("Portfolio Persist"),
                new BondTradeDetails("DE0007654321", "Issuer Persist", "2035-12-31", 0.045)
        );

        firstRepository.save(trade);

        TradeRepository secondRepository = new H2TradeRepository(jdbcUrl);

        assertEquals(true, secondRepository.existsByTradeId("H2_PERSIST_1"));
        assertEquals(1, secondRepository.findAll().size());

        Trade loadedTrade = secondRepository.findByTradeId("H2_PERSIST_1");

        assertEquals("H2_PERSIST_1", loadedTrade.getTradeId());
        assertTrue(loadedTrade.getProductDetails() instanceof BondTradeDetails);

        BondTradeDetails details = (BondTradeDetails) loadedTrade.getProductDetails();

        assertEquals("DE0007654321", details.getIsin());
        assertEquals("Issuer Persist", details.getIssuer());
        assertEquals("2035-12-31", details.getMaturityDate());
        assertEquals(0.045, details.getCouponRate());
    }
}