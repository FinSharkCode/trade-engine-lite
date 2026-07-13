package com.adrian.tradeengine.validation;

import com.adrian.tradeengine.model.BondTradeDetails;
import com.adrian.tradeengine.model.EquityTradeDetails;
import com.adrian.tradeengine.model.FxTradeDetails;
import com.adrian.tradeengine.model.ProductType;
import com.adrian.tradeengine.model.Trade;

public class TradeValidator {

    public boolean isValid(Trade trade) {
        return trade != null
                && hasTradeId(trade)
                && hasProductType(trade)
                && hasPositiveNominal(trade)
                && hasCurrency(trade)
                && hasCounterparty(trade)
                && hasPortfolio(trade)
                && hasValidProductDetails(trade);
    }

    public void validateOrThrow(Trade trade) {
        if (trade == null) {
            throw new IllegalArgumentException("Trade must not be null");
        }
        if (!hasTradeId(trade)) {
            throw new IllegalArgumentException("Trade ID must not be blank");
        }
        if (!hasProductType(trade)) {
            throw new IllegalArgumentException("Product type must not be null");
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
        if (!hasValidProductDetails(trade)) {
            throw new IllegalArgumentException("Product details must match product type and must be valid");
        }
    }

    private boolean hasTradeId(Trade trade) {
        return hasText(trade.getTradeId());
    }

    private boolean hasProductType(Trade trade) {
        return trade.getProductType() != null;
    }

    private boolean hasPositiveNominal(Trade trade) {
        return trade.getNominal() > 0;
    }

    private boolean hasCurrency(Trade trade) {
        return hasText(trade.getCurrency());
    }

    private boolean hasCounterparty(Trade trade) {
        return trade.getCounterparty() != null;
    }

    private boolean hasPortfolio(Trade trade) {
        return trade.getPortfolio() != null;
    }

    private boolean hasValidProductDetails(Trade trade) {
        if (trade.getProductType() == ProductType.FX) {
            return hasValidFxDetails(trade);
        }

        if (trade.getProductType() == ProductType.BOND) {
            return hasValidBondDetails(trade);
        }

        if (trade.getProductType() == ProductType.EQUITY) {
            return hasValidEquityDetails(trade);
        }

        return false;
    }

    private boolean hasValidFxDetails(Trade trade) {
        if (!(trade.getProductDetails() instanceof FxTradeDetails)) {
            return false;
        }

        FxTradeDetails details = (FxTradeDetails) trade.getProductDetails();

        return hasText(details.getCurrencyPair())
                && hasText(details.getSettlementDate())
                && details.getExchangeRate() > 0;
    }

    private boolean hasValidBondDetails(Trade trade) {
        if (!(trade.getProductDetails() instanceof BondTradeDetails)) {
            return false;
        }

        BondTradeDetails details = (BondTradeDetails) trade.getProductDetails();

        return hasText(details.getIsin())
                && hasText(details.getIssuer())
                && hasText(details.getMaturityDate())
                && details.getCouponRate() >= 0;
    }

    private boolean hasValidEquityDetails(Trade trade) {
        if (!(trade.getProductDetails() instanceof EquityTradeDetails)) {
            return false;
        }

        EquityTradeDetails details = (EquityTradeDetails) trade.getProductDetails();

        return hasText(details.getTicker())
                && hasText(details.getExchange())
                && details.getQuantity() > 0
                && details.getPrice() > 0;
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}