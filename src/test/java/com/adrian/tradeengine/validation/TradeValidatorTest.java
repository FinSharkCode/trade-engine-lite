package com.adrian.tradeengine.validation;

import com.adrian.tradeengine.model.Counterparty;
import com.adrian.tradeengine.model.FxTradeDetails;
import com.adrian.tradeengine.model.Portfolio;
import com.adrian.tradeengine.model.ProductType;
import com.adrian.tradeengine.model.Trade;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TradeValidatorTest {

    private final TradeValidator validator = new TradeValidator();

    @Test
    void shouldReturnTrueForValidTrade() {
        Trade trade = new Trade(
                "T1",
                ProductType.FX,
                1_000_000.0,
                "EUR",
                new Counterparty("Bank A"),
                new Portfolio("Portfolio 1"),
                new FxTradeDetails("EUR/USD", "2026-07-15", 1.08)
        );

        assertTrue(validator.isValid(trade));
    }

    @Test
    void shouldReturnFalseWhenTradeIdIsBlank() {
        Trade trade = new Trade(
                "",
                ProductType.FX,
                1_000_000.0,
                "EUR",
                new Counterparty("Bank A"),
                new Portfolio("Portfolio 1"),
                new FxTradeDetails("EUR/USD", "2026-07-15", 1.08)
        );

        assertFalse(validator.isValid(trade));
    }

    @Test
    void shouldReturnFalseWhenNominalIsNotPositive() {
        Trade trade = new Trade(
                "T2",
                ProductType.FX,
                0.0,
                "EUR",
                new Counterparty("Bank A"),
                new Portfolio("Portfolio 1"),
                new FxTradeDetails("EUR/USD", "2026-07-15", 1.08)
        );

        assertFalse(validator.isValid(trade));
    }

    @Test
    void shouldReturnFalseWhenCurrencyIsBlank() {
        Trade trade = new Trade(
                "T3",
                ProductType.FX,
                500_000.0,
                "",
                new Counterparty("Bank A"),
                new Portfolio("Portfolio 1"),
                new FxTradeDetails("EUR/USD", "2026-07-15", 1.08)
        );

        assertFalse(validator.isValid(trade));
    }

    @Test
    void shouldReturnFalseWhenCounterpartyIsMissing() {
        Trade trade = new Trade(
                "T4",
                ProductType.FX,
                500_000.0,
                "USD",
                null,
                new Portfolio("Portfolio 1"),
                new FxTradeDetails("EUR/USD", "2026-07-15", 1.08)
        );

        assertFalse(validator.isValid(trade));
    }

    @Test
    void shouldReturnFalseWhenPortfolioIsMissing() {
        Trade trade = new Trade(
                "T5",
                ProductType.FX,
                500_000.0,
                "USD",
                new Counterparty("Bank A"),
                null,
                new FxTradeDetails("EUR/USD", "2026-07-15", 1.08)
        );

        assertFalse(validator.isValid(trade));
    }

    @Test
    void shouldNotThrowForValidTrade() {
        Trade trade = new Trade(
                "T6",
                ProductType.FX,
                1_500_000.0,
                "EUR",
                new Counterparty("Bank A"),
                new Portfolio("Portfolio 1"),
                new FxTradeDetails("EUR/USD", "2026-07-15", 1.08)
        );

        validator.validateOrThrow(trade);
    }

    @Test
    void shouldThrowWhenTradeIdIsBlank() {
        Trade trade = new Trade(
                "",
                ProductType.FX,
                1_500_000.0,
                "EUR",
                new Counterparty("Bank A"),
                new Portfolio("Portfolio 1"),
                new FxTradeDetails("EUR/USD", "2026-07-15", 1.08)
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validateOrThrow(trade)
        );

        assertTrue(exception.getMessage().contains("Trade ID"));
    }
}