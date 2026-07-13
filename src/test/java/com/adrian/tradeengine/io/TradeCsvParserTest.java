package com.adrian.tradeengine.io;

import com.adrian.tradeengine.model.BondTradeDetails;
import com.adrian.tradeengine.model.EquityTradeDetails;
import com.adrian.tradeengine.model.FxTradeDetails;
import com.adrian.tradeengine.model.ProductType;
import com.adrian.tradeengine.model.Trade;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TradeCsvParserTest {

    private final TradeCsvParser parser = new TradeCsvParser();

    @Test
    void shouldParseCsvLineToTrade() {
        Trade trade = parser.parseLine(
                "T1,FX,1000000,EUR,Bank A,Portfolio 1"
        );

        assertEquals("T1", trade.getTradeId());
        assertEquals(ProductType.FX, trade.getProductType());
        assertEquals(1_000_000.0, trade.getNominal());
        assertEquals("EUR", trade.getCurrency());
        assertEquals("Bank A", trade.getCounterparty().getName());
        assertEquals("Portfolio 1", trade.getPortfolio().getName());
        assertTrue(trade.getProductDetails() instanceof FxTradeDetails);
    }

    @Test
    void shouldParseFxTradeDetails() {
        Trade trade = parser.parseLine(
                "FX1,FX,1000000,EUR,Bank FX,Portfolio FX,EUR/USD,2026-07-15,1.08"
        );

        assertEquals(ProductType.FX, trade.getProductType());
        assertTrue(trade.getProductDetails() instanceof FxTradeDetails);

        FxTradeDetails details = (FxTradeDetails) trade.getProductDetails();

        assertEquals("EUR/USD", details.getCurrencyPair());
        assertEquals("2026-07-15", details.getSettlementDate());
        assertEquals(1.08, details.getExchangeRate());
    }

    @Test
    void shouldParseBondTradeDetails() {
        Trade trade = parser.parseLine(
                "BOND1,BOND,2000000,EUR,Bank Bond,Portfolio Bond,DE0001234567,Issuer A,2030-12-31,0.035"
        );

        assertEquals(ProductType.BOND, trade.getProductType());
        assertTrue(trade.getProductDetails() instanceof BondTradeDetails);

        BondTradeDetails details = (BondTradeDetails) trade.getProductDetails();

        assertEquals("DE0001234567", details.getIsin());
        assertEquals("Issuer A", details.getIssuer());
        assertEquals("2030-12-31", details.getMaturityDate());
        assertEquals(0.035, details.getCouponRate());
    }

    @Test
    void shouldParseEquityTradeDetails() {
        Trade trade = parser.parseLine(
                "EQ1,EQUITY,500000,USD,Bank Equity,Portfolio Equity,AAPL,NASDAQ,100,195.50"
        );

        assertEquals(ProductType.EQUITY, trade.getProductType());
        assertTrue(trade.getProductDetails() instanceof EquityTradeDetails);

        EquityTradeDetails details = (EquityTradeDetails) trade.getProductDetails();

        assertEquals("AAPL", details.getTicker());
        assertEquals("NASDAQ", details.getExchange());
        assertEquals(100, details.getQuantity());
        assertEquals(195.50, details.getPrice());
    }
}