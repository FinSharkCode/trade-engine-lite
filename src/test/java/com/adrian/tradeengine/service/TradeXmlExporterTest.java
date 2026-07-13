package com.adrian.tradeengine.service;

import com.adrian.tradeengine.model.BondTradeDetails;
import com.adrian.tradeengine.model.Counterparty;
import com.adrian.tradeengine.model.EquityTradeDetails;
import com.adrian.tradeengine.model.FxTradeDetails;
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
    void shouldExportFxTradeDetailsAsXml() {
        List<Trade> trades = Arrays.asList(
                new Trade(
                        "FX1",
                        ProductType.FX,
                        1_000_000.0,
                        "EUR",
                        new Counterparty("Bank FX"),
                        new Portfolio("Portfolio FX"),
                        new FxTradeDetails("EUR/USD", "2026-07-15", 1.08)
                )
        );

        String xml = exporter.exportTrades(trades);

        assertTrue(xml.contains("<trades>"));
        assertTrue(xml.contains("<tradeId>FX1</tradeId>"));
        assertTrue(xml.contains("<productType>FX</productType>"));
        assertTrue(xml.contains("<productDetails>"));
        assertTrue(xml.contains("<fxDetails>"));
        assertTrue(xml.contains("<currencyPair>EUR/USD</currencyPair>"));
        assertTrue(xml.contains("<settlementDate>2026-07-15</settlementDate>"));
        assertTrue(xml.contains("<exchangeRate>1.08</exchangeRate>"));
        assertTrue(xml.contains("</trades>"));
    }

    @Test
    void shouldExportBondTradeDetailsAsXml() {
        List<Trade> trades = Arrays.asList(
                new Trade(
                        "BOND1",
                        ProductType.BOND,
                        2_000_000.0,
                        "EUR",
                        new Counterparty("Bank Bond"),
                        new Portfolio("Portfolio Bond"),
                        new BondTradeDetails("DE0001234567", "Issuer A", "2030-12-31", 0.035)
                )
        );

        String xml = exporter.exportTrades(trades);

        assertTrue(xml.contains("<tradeId>BOND1</tradeId>"));
        assertTrue(xml.contains("<productType>BOND</productType>"));
        assertTrue(xml.contains("<productDetails>"));
        assertTrue(xml.contains("<bondDetails>"));
        assertTrue(xml.contains("<isin>DE0001234567</isin>"));
        assertTrue(xml.contains("<issuer>Issuer A</issuer>"));
        assertTrue(xml.contains("<maturityDate>2030-12-31</maturityDate>"));
        assertTrue(xml.contains("<couponRate>0.035</couponRate>"));
    }

    @Test
    void shouldExportEquityTradeDetailsAsXml() {
        List<Trade> trades = Arrays.asList(
                new Trade(
                        "EQ1",
                        ProductType.EQUITY,
                        500_000.0,
                        "USD",
                        new Counterparty("Bank Equity"),
                        new Portfolio("Portfolio Equity"),
                        new EquityTradeDetails("AAPL", "NASDAQ", 100, 195.50)
                )
        );

        String xml = exporter.exportTrades(trades);

        assertTrue(xml.contains("<tradeId>EQ1</tradeId>"));
        assertTrue(xml.contains("<productType>EQUITY</productType>"));
        assertTrue(xml.contains("<productDetails>"));
        assertTrue(xml.contains("<equityDetails>"));
        assertTrue(xml.contains("<ticker>AAPL</ticker>"));
        assertTrue(xml.contains("<exchange>NASDAQ</exchange>"));
        assertTrue(xml.contains("<quantity>100</quantity>"));
        assertTrue(xml.contains("<price>195.5</price>"));
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
                        new Portfolio("Portfolio <Test>"),
                        new FxTradeDetails("EUR/USD", "2026-07-15", 1.08)
                )
        );

        String xml = exporter.exportTrades(trades);

        assertTrue(xml.contains("Bank A &amp; Co"));
        assertTrue(xml.contains("Portfolio &lt;Test&gt;"));
    }

    @Test
    void shouldEscapeSpecialXmlCharactersInProductDetails() {
        List<Trade> trades = Arrays.asList(
                new Trade(
                        "FX_SPECIAL",
                        ProductType.FX,
                        1_000_000.0,
                        "EUR",
                        new Counterparty("Bank A"),
                        new Portfolio("Portfolio 1"),
                        new FxTradeDetails("EUR/USD & GBP/USD", "2026-07-15", 1.08)
                )
        );

        String xml = exporter.exportTrades(trades);

        assertTrue(xml.contains("EUR/USD &amp; GBP/USD"));
    }
}