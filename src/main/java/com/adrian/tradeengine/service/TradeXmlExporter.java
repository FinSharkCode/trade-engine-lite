package com.adrian.tradeengine.service;

import com.adrian.tradeengine.model.BondTradeDetails;
import com.adrian.tradeengine.model.EquityTradeDetails;
import com.adrian.tradeengine.model.FxTradeDetails;
import com.adrian.tradeengine.model.ProductDetails;
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

    public String exportSingleTrade(Trade trade) {
        StringBuilder xml = new StringBuilder();

        xml.append("<trades>").append(System.lineSeparator());
        xml.append(exportTrade(trade));
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

        xml.append(exportProductDetails(trade.getProductDetails()));

        xml.append("    </trade>").append(System.lineSeparator());

        return xml.toString();
    }

    private String exportProductDetails(ProductDetails productDetails) {
        if (productDetails instanceof FxTradeDetails) {
            return exportFxDetails((FxTradeDetails) productDetails);
        }

        if (productDetails instanceof BondTradeDetails) {
            return exportBondDetails((BondTradeDetails) productDetails);
        }

        if (productDetails instanceof EquityTradeDetails) {
            return exportEquityDetails((EquityTradeDetails) productDetails);
        }

        return "        <productDetails />" + System.lineSeparator();
    }

    private String exportFxDetails(FxTradeDetails details) {
        StringBuilder xml = new StringBuilder();

        xml.append("        <productDetails>").append(System.lineSeparator());
        xml.append("            <fxDetails>").append(System.lineSeparator());
        xml.append("                <currencyPair>").append(escapeXml(details.getCurrencyPair())).append("</currencyPair>").append(System.lineSeparator());
        xml.append("                <settlementDate>").append(escapeXml(details.getSettlementDate())).append("</settlementDate>").append(System.lineSeparator());
        xml.append("                <exchangeRate>").append(details.getExchangeRate()).append("</exchangeRate>").append(System.lineSeparator());
        xml.append("            </fxDetails>").append(System.lineSeparator());
        xml.append("        </productDetails>").append(System.lineSeparator());

        return xml.toString();
    }

    private String exportBondDetails(BondTradeDetails details) {
        StringBuilder xml = new StringBuilder();

        xml.append("        <productDetails>").append(System.lineSeparator());
        xml.append("            <bondDetails>").append(System.lineSeparator());
        xml.append("                <isin>").append(escapeXml(details.getIsin())).append("</isin>").append(System.lineSeparator());
        xml.append("                <issuer>").append(escapeXml(details.getIssuer())).append("</issuer>").append(System.lineSeparator());
        xml.append("                <maturityDate>").append(escapeXml(details.getMaturityDate())).append("</maturityDate>").append(System.lineSeparator());
        xml.append("                <couponRate>").append(details.getCouponRate()).append("</couponRate>").append(System.lineSeparator());
        xml.append("            </bondDetails>").append(System.lineSeparator());
        xml.append("        </productDetails>").append(System.lineSeparator());

        return xml.toString();
    }

    private String exportEquityDetails(EquityTradeDetails details) {
        StringBuilder xml = new StringBuilder();

        xml.append("        <productDetails>").append(System.lineSeparator());
        xml.append("            <equityDetails>").append(System.lineSeparator());
        xml.append("                <ticker>").append(escapeXml(details.getTicker())).append("</ticker>").append(System.lineSeparator());
        xml.append("                <exchange>").append(escapeXml(details.getExchange())).append("</exchange>").append(System.lineSeparator());
        xml.append("                <quantity>").append(details.getQuantity()).append("</quantity>").append(System.lineSeparator());
        xml.append("                <price>").append(details.getPrice()).append("</price>").append(System.lineSeparator());
        xml.append("            </equityDetails>").append(System.lineSeparator());
        xml.append("        </productDetails>").append(System.lineSeparator());

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