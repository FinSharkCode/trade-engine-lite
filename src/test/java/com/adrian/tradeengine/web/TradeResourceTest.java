package com.adrian.tradeengine.web;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.adrian.tradeengine.service.InMemoryTradeRepository;

public class TradeResourceTest {

    @Test
    void shouldStoreValidTrade() {
        TradeResource resource = new TradeResource(new InMemoryTradeRepository());

        String response = resource.createTrade(
                "T100,FX,1000000,EUR,Bank A,Portfolio 1"
        );

        assertTrue(response.contains("Trade stored"));
        assertTrue(response.contains("T100"));
    }

    @Test
    void shouldReturnStoredTrades() {
        TradeResource resource = new TradeResource(new InMemoryTradeRepository());

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
        TradeResource resource = new TradeResource(new InMemoryTradeRepository());

        String response = resource.createTrade(
                "T102,FX,0,EUR,Bank A,Portfolio 1"
        );

        assertTrue(response.contains("Trade is invalid"));
    }
    
    @Test
    void shouldRejectDuplicateTradeId() {
        TradeResource resource = new TradeResource(new InMemoryTradeRepository());

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
        TradeResource resource = new TradeResource(new InMemoryTradeRepository());

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
    
    @Test
    void shouldReturnSingleTradeAsXml() {
        TradeResource resource = new TradeResource(new InMemoryTradeRepository());

        resource.createTrade(
                "T_XML_SINGLE,FX,1000000,EUR,Bank XML Single,Portfolio XML Single"
        );

        String response = resource.getTradeAsXml("T_XML_SINGLE");

        assertTrue(response.contains("<trades>"));
        assertTrue(response.contains("<tradeId>T_XML_SINGLE</tradeId>"));
        assertTrue(response.contains("<counterparty>"));
        assertTrue(response.contains("Bank XML Single"));
        assertTrue(response.contains("Portfolio XML Single"));
    }
    
    @Test
    void shouldReturnBondPrice() {
        TradeResource resource = new TradeResource(new InMemoryTradeRepository());

        resource.createTrade(
                "BOND_PRICE_RESOURCE,BOND,1000000,EUR,Bank Bond,Portfolio Bond,DE0001234567,Issuer A,2030-12-31,0.03"
        );

        String response = resource.getBondPrice(
                "BOND_PRICE_RESOURCE",
                0.04,
                5
        );

        assertTrue(response.contains("Present value for trade BOND_PRICE_RESOURCE"));
        assertTrue(response.contains("955481.78"));
    }

    @Test
    void shouldReturnPricingErrorWhenTradeIsNotBond() {
        TradeResource resource = new TradeResource(new InMemoryTradeRepository());

        resource.createTrade(
                "FX_PRICE_RESOURCE,FX,1000000,EUR,Bank FX,Portfolio FX,EUR/USD,2026-07-15,1.08"
        );

        String response = resource.getBondPrice(
                "FX_PRICE_RESOURCE",
                0.04,
                5
        );

        assertTrue(response.contains("Pricing error"));
        assertTrue(response.contains("not a bond"));
    }

    @Test
    void shouldReturnPricingErrorWhenTradeDoesNotExist() {
        TradeResource resource = new TradeResource(new InMemoryTradeRepository());

        String response = resource.getBondPrice(
                "UNKNOWN_PRICE_RESOURCE",
                0.04,
                5
        );

        assertTrue(response.contains("Pricing error"));
        assertTrue(response.contains("Trade not found"));
    }
}