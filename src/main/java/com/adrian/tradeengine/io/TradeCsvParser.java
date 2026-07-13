package com.adrian.tradeengine.io;

import com.adrian.tradeengine.model.Counterparty;
import com.adrian.tradeengine.model.Portfolio;
import com.adrian.tradeengine.model.ProductType;
import com.adrian.tradeengine.model.Trade;
import com.adrian.tradeengine.model.BondTradeDetails;
import com.adrian.tradeengine.model.EquityTradeDetails;
import com.adrian.tradeengine.model.FxTradeDetails;
import com.adrian.tradeengine.model.ProductDetails;

public class TradeCsvParser {

    public Trade parseLine(String line) {
        String[] parts = line.split(",");

        String tradeId = parts[0];
        ProductType productType = ProductType.valueOf(parts[1]);
        double nominal = Double.parseDouble(parts[2]);
        String currency = parts[3];
        Counterparty counterparty = new Counterparty(parts[4]);
        Portfolio portfolio = new Portfolio(parts[5]);
        ProductDetails productDetails = createDefaultProductDetails(productType);

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
    
    private ProductDetails createDefaultProductDetails(ProductType productType) {
        if (productType == ProductType.FX) {
            return new FxTradeDetails("EUR/USD", "2026-07-15", 1.08);
        }

        if (productType == ProductType.BOND) {
            return new BondTradeDetails("DE0001234567", "Issuer A", "2030-12-31", 0.035);
        }

        if (productType == ProductType.EQUITY) {
            return new EquityTradeDetails("AAPL", "NASDAQ", 100, 195.50);
        }

        return null;
    }
}