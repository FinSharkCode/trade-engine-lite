package com.adrian.tradeengine.service;

import com.adrian.tradeengine.model.Counterparty;
import com.adrian.tradeengine.model.Portfolio;
import com.adrian.tradeengine.model.ProductType;
import com.adrian.tradeengine.model.Trade;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TradeXmlExporterTest {

    private final TradeXmlExporter exporter = new TradeXmlExporter();

    @Test
    void shouldExportTradeAsXml() {
        List<Trade> trades = Arrays.asList(
                new Trade(
                        "T1",
                        ProductType.FX,
                        1_000_000.0,
                        "EUR",
                        new Counterparty("Bank A"),
                        new Portfolio("Portfolio 1")
                )
        );

        String xml = exporter.exportTrades(trades);

        assertTrue(xml.contains("<trades>"));
        assertTrue(xml.contains("<trade>"));
        assertTrue(xml.contains("<tradeId>T1</tradeId>"));
        assertTrue(xml.contains("<productType>FX</productType>"));
        assertTrue(xml.contains("<nominal>1000000.0</nominal>"));
        assertTrue(xml.contains("<currency>EUR</currency>"));
        assertTrue(xml.contains("<counterparty>"));
        assertTrue(xml.contains("<name>Bank A</name>"));
        assertTrue(xml.contains("<portfolio>"));
        assertTrue(xml.contains("<name>Portfolio 1</name>"));
        assertTrue(xml.contains("</trades>"));
    }

    @Test
    void shouldEscapeSpecialXmlCharacters() {
        List<Trade> trades = Arrays.asList(
                new Trade(
                        "T2",
                        ProductType.FX,
                        500_000.0,
                        "EUR",
                        new Counterparty("Bank A & Co"),
                        new Portfolio("Portfolio <Test>")
                )
        );

        String xml = exporter.exportTrades(trades);

        assertTrue(xml.contains("Bank A &amp; Co"));
        assertTrue(xml.contains("Portfolio &lt;Test&gt;"));
    }
}