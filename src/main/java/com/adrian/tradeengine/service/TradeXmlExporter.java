package com.adrian.tradeengine.service;

import com.adrian.tradeengine.model.Trade;

import java.util.List;

public class TradeXmlExporter {

    public String exportTrades(List<Trade> trades) {
        StringBuilder xml = new StringBuilder();

        xml.append("<trades>").append(System.lineSeparator());

        for (Trade trade : trades) {
            xml.append(exportTrade(trade));
        }

        xml.append("</trades>");

        return xml.toString();
    }

    private String exportTrade(Trade trade) {
        StringBuilder xml = new StringBuilder();

        xml.append("    <trade>").append(System.lineSeparator());
        xml.append("        <tradeId>").append(escapeXml(trade.getTradeId())).append("</tradeId>").append(System.lineSeparator());
        xml.append("        <productType>").append(trade.getProductType()).append("</productType>").append(System.lineSeparator());
        xml.append("        <nominal>").append(trade.getNominal()).append("</nominal>").append(System.lineSeparator());
        xml.append("        <currency>").append(escapeXml(trade.getCurrency())).append("</currency>").append(System.lineSeparator());
        xml.append("        <counterparty>").append(System.lineSeparator());
        xml.append("            <name>").append(escapeXml(trade.getCounterparty().getName())).append("</name>").append(System.lineSeparator());
        xml.append("        </counterparty>").append(System.lineSeparator());
        xml.append("        <portfolio>").append(System.lineSeparator());
        xml.append("            <name>").append(escapeXml(trade.getPortfolio().getName())).append("</name>").append(System.lineSeparator());
        xml.append("        </portfolio>").append(System.lineSeparator());
        xml.append("    </trade>").append(System.lineSeparator());

        return xml.toString();
    }

    private String escapeXml(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}