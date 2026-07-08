package com.adrian.tradeengine.web;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TradeResourceTest {

    @Test
    void shouldStoreValidTrade() {
        TradeResource resource = new TradeResource();

        String response = resource.createTrade(
                "T100,FX,1000000,EUR,Bank A,Portfolio 1"
        );

        assertTrue(response.contains("Trade stored"));
        assertTrue(response.contains("T100"));
    }

    @Test
    void shouldReturnStoredTrades() {
        TradeResource resource = new TradeResource();

        resource.createTrade(
                "T101,BOND,2000000,EUR,Bank B,Portfolio 2"
        );

        String response = resource.getTrades();

        assertTrue(response.contains("T101"));
        assertTrue(response.contains("BOND"));
        assertTrue(response.contains("Portfolio 2"));
    }

    @Test
    void shouldRejectInvalidTrade() {
        TradeResource resource = new TradeResource();

        String response = resource.createTrade(
                "T102,FX,0,EUR,Bank A,Portfolio 1"
        );

        assertTrue(response.contains("Trade is invalid"));
    }
    
    @Test
    void shouldRejectDuplicateTradeId() {
        TradeResource resource = new TradeResource();

        resource.createTrade(
                "T200,FX,1000000,EUR,Bank A,Portfolio 1"
        );

        String response = resource.createTrade(
                "T200,FX,1500000,EUR,Bank B,Portfolio 2"
        );

        assertTrue(response.contains("Trade already exists"));
        assertTrue(response.contains("T200"));
    }
    
    @Test
    void shouldReturnStoredTradesAsXml() {
        TradeResource resource = new TradeResource();

        resource.createTrade(
                "T300,EQUITY,750000,USD,Bank C,Portfolio XML"
        );

        String response = resource.getTradesAsXml();

        assertTrue(response.contains("<trades>"));
        assertTrue(response.contains("<tradeId>T300</tradeId>"));
        assertTrue(response.contains("<productType>EQUITY</productType>"));
        assertTrue(response.contains("<currency>USD</currency>"));
        assertTrue(response.contains("<counterparty>"));
        assertTrue(response.contains("<portfolio>"));
        assertTrue(response.contains("Portfolio XML"));
    }
}