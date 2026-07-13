package com.adrian.tradeengine.io;

import com.adrian.tradeengine.model.BondTradeDetails;
import com.adrian.tradeengine.model.Counterparty;
import com.adrian.tradeengine.model.EquityTradeDetails;
import com.adrian.tradeengine.model.FxTradeDetails;
import com.adrian.tradeengine.model.Portfolio;
import com.adrian.tradeengine.model.ProductDetails;
import com.adrian.tradeengine.model.ProductType;
import com.adrian.tradeengine.model.Trade;

public class TradeCsvParser {

    public Trade parseLine(String line) {
        String[] parts = line.split(",");

        String tradeId = value(parts, 0);
        ProductType productType = ProductType.valueOf(value(parts, 1));
        double nominal = Double.parseDouble(value(parts, 2));
        String currency = value(parts, 3);
        Counterparty counterparty = new Counterparty(value(parts, 4));
        Portfolio portfolio = new Portfolio(value(parts, 5));
        ProductDetails productDetails = parseProductDetails(productType, parts);

        return new Trade(
                tradeId,
                productType,
                nominal,
                currency,
                counterparty,
                portfolio,
                productDetails
        );
    }

    private ProductDetails parseProductDetails(ProductType productType, String[] parts) {
        if (productType == ProductType.FX) {
            return parseFxDetails(parts);
        }

        if (productType == ProductType.BOND) {
            return parseBondDetails(parts);
        }

        if (productType == ProductType.EQUITY) {
            return parseEquityDetails(parts);
        }

        return null;
    }

    private ProductDetails parseFxDetails(String[] parts) {
        if (parts.length >= 9) {
            return new FxTradeDetails(
                    value(parts, 6),
                    value(parts, 7),
                    Double.parseDouble(value(parts, 8))
            );
        }

        return new FxTradeDetails("EUR/USD", "2026-07-15", 1.08);
    }

    private ProductDetails parseBondDetails(String[] parts) {
        if (parts.length >= 10) {
            return new BondTradeDetails(
                    value(parts, 6),
                    value(parts, 7),
                    value(parts, 8),
                    Double.parseDouble(value(parts, 9))
            );
        }

        return new BondTradeDetails("DE0001234567", "Issuer A", "2030-12-31", 0.035);
    }

    private ProductDetails parseEquityDetails(String[] parts) {
        if (parts.length >= 10) {
            return new EquityTradeDetails(
                    value(parts, 6),
                    value(parts, 7),
                    Integer.parseInt(value(parts, 8)),
                    Double.parseDouble(value(parts, 9))
            );
        }

        return new EquityTradeDetails("AAPL", "NASDAQ", 100, 195.50);
    }

    private String value(String[] parts, int index) {
        return parts[index].trim();
    }
}