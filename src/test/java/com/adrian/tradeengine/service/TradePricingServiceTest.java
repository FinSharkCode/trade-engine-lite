package com.adrian.tradeengine.service;

import com.adrian.tradeengine.model.BondTradeDetails;
import com.adrian.tradeengine.model.Counterparty;
import com.adrian.tradeengine.model.FxTradeDetails;
import com.adrian.tradeengine.model.Portfolio;
import com.adrian.tradeengine.model.ProductType;
import com.adrian.tradeengine.model.Trade;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TradePricingServiceTest {

    @Test
    void shouldPriceBondTradeFromRepository() {
        TradeRepository repository = new InMemoryTradeRepository();

        Trade bondTrade = new Trade(
                "BOND_PRICE_1",
                ProductType.BOND,
                1_000_000.0,
                "EUR",
                new Counterparty("Bank Bond"),
                new Portfolio("Portfolio Bond"),
                new BondTradeDetails("DE0001234567", "Issuer A", "2030-12-31", 0.03)
        );

        repository.save(bondTrade);

        TradePricingService service = new TradePricingService(
                repository,
                new BondCashflowService(),
                new BondPricingService()
        );

        double presentValue = service.priceBondTrade(
                "BOND_PRICE_1",
                0.04,
                5
        );

        assertEquals(955_481.78, presentValue, 0.01);
    }

    @Test
    void shouldThrowWhenTradeDoesNotExist() {
        TradeRepository repository = new InMemoryTradeRepository();

        TradePricingService service = new TradePricingService(
                repository,
                new BondCashflowService(),
                new BondPricingService()
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.priceBondTrade("UNKNOWN", 0.04, 5)
        );
    }

    @Test
    void shouldThrowWhenTradeIsNotBond() {
        TradeRepository repository = new InMemoryTradeRepository();

        Trade fxTrade = new Trade(
                "FX_PRICE_1",
                ProductType.FX,
                1_000_000.0,
                "EUR",
                new Counterparty("Bank FX"),
                new Portfolio("Portfolio FX"),
                new FxTradeDetails("EUR/USD", "2026-07-15", 1.08)
        );

        repository.save(fxTrade);

        TradePricingService service = new TradePricingService(
                repository,
                new BondCashflowService(),
                new BondPricingService()
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.priceBondTrade("FX_PRICE_1", 0.04, 5)
        );
    }
}