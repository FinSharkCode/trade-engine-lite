package com.adrian.tradeengine.service;

import com.adrian.tradeengine.io.TradeCsvParser;
import com.adrian.tradeengine.model.Trade;
import com.adrian.tradeengine.validation.TradeValidator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TradeImportServiceTest {

    private final TradeImportService importService = new TradeImportService(
            new TradeCsvParser(),
            new TradeValidator()
    );

    @Test
    void shouldImportOnlyValidTrades() {
        List<String> lines = List.of(
                "T1,FX,1000000,EUR,Bank A,Portfolio 1",
                "T2,BOND,2000000,EUR,Bank B,Portfolio 1",
                "T3,FX,0,USD,Bank A,Portfolio 2"
        );

        List<Trade> trades = importService.importValidTrades(lines);

        assertEquals(2, trades.size());
        assertEquals("T1", trades.get(0).getTradeId());
        assertEquals("T2", trades.get(1).getTradeId());
    }
}