package com.adrian.tradeengine.io;

import com.adrian.tradeengine.model.Counterparty;
import com.adrian.tradeengine.model.Portfolio;
import com.adrian.tradeengine.model.ProductType;
import com.adrian.tradeengine.model.Trade;

public class TradeCsvParser {

    public Trade parseLine(String line) {
        String[] parts = line.split(",");

        String tradeId = parts[0];
        ProductType productType = ProductType.valueOf(parts[1]);
        double nominal = Double.parseDouble(parts[2]);
        String currency = parts[3];
        Counterparty counterparty = new Counterparty(parts[4]);
        Portfolio portfolio = new Portfolio(parts[5]);

        return new Trade(
                tradeId,
                productType,
                nominal,
                currency,
                counterparty,
                portfolio
        );
    }
}