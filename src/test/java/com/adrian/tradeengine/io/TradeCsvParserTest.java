package com.adrian.tradeengine.io;

import com.adrian.tradeengine.model.ProductType;
import com.adrian.tradeengine.model.Trade;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TradeCsvParserTest {

    private final TradeCsvParser parser = new TradeCsvParser();

    @Test
    void shouldParseCsvLineToTrade() {
        String line = "T1,FX,1000000,EUR,Bank A,Portfolio 1";

        Trade trade = parser.parseLine(line);

        assertEquals("T1", trade.getTradeId());
        assertEquals(ProductType.FX, trade.getProductType());
        assertEquals(1_000_000.0, trade.getNominal());
        assertEquals("EUR", trade.getCurrency());
        assertEquals("Bank A", trade.getCounterparty().getName());
        assertEquals("Portfolio 1", trade.getPortfolio().getName());
    }
}