package com.adrian.tradeengine.validation;

import com.adrian.tradeengine.model.Trade;

public class TradeValidator {

    public boolean isValid(Trade trade) {
        return trade != null
                && hasTradeId(trade)
                && hasPositiveNominal(trade)
                && hasCurrency(trade)
                && hasCounterparty(trade)
                && hasPortfolio(trade);
    }

    public void validateOrThrow(Trade trade) {
        if (trade == null) {
            throw new IllegalArgumentException("Trade must not be null");
        }
        if (!hasTradeId(trade)) {
            throw new IllegalArgumentException("Trade ID must not be blank");
        }
        if (!hasPositiveNominal(trade)) {
            throw new IllegalArgumentException("Nominal must be greater than zero");
        }
        if (!hasCurrency(trade)) {
            throw new IllegalArgumentException("Currency must not be blank");
        }
        if (!hasCounterparty(trade)) {
            throw new IllegalArgumentException("Counterparty must not be null");
        }
        if (!hasPortfolio(trade)) {
            throw new IllegalArgumentException("Portfolio must not be null");
        }
    }

    private boolean hasTradeId(Trade trade) {
        return trade.getTradeId() != null && !trade.getTradeId().isBlank();
    }

    private boolean hasPositiveNominal(Trade trade) {
        return trade.getNominal() > 0;
    }

    private boolean hasCurrency(Trade trade) {
        return trade.getCurrency() != null && !trade.getCurrency().isBlank();
    }

    private boolean hasCounterparty(Trade trade) {
        return trade.getCounterparty() != null;
    }

    private boolean hasPortfolio(Trade trade) {
        return trade.getPortfolio() != null;
    }
}