package com.adrian.tradeengine.validation;

import com.adrian.tradeengine.model.Trade;

public class TradeValidator {

    public boolean isValid(Trade trade) {
        if (trade == null) {
            return false;
        }
        if (trade.getTradeId() == null || trade.getTradeId().isBlank()) {
            return false;
        }
        if (trade.getNominal() <= 0) {
            return false;
        }
        if (trade.getCurrency() == null || trade.getCurrency().isBlank()) {
            return false;
        }
        if (trade.getCounterparty() == null) {
            return false;
        }
        if (trade.getPortfolio() == null) {
            return false;
        }
        return true;
    }
}
