package com.adrian.tradeengine.service;

import com.adrian.tradeengine.model.BondTradeDetails;
import com.adrian.tradeengine.model.ProductType;
import com.adrian.tradeengine.model.Trade;

import java.util.List;

public class TradePricingService {

    private final TradeRepository repository;
    private final BondCashflowService cashflowService;
    private final BondPricingService pricingService;

    public TradePricingService(
            TradeRepository repository,
            BondCashflowService cashflowService,
            BondPricingService pricingService
    ) {
        this.repository = repository;
        this.cashflowService = cashflowService;
        this.pricingService = pricingService;
    }

    public double priceBondTrade(
            String tradeId,
            double discountRate,
            int yearsToMaturity
    ) {
        Trade trade = repository.findByTradeId(tradeId);

        if (trade == null) {
            throw new IllegalArgumentException("Trade not found: " + tradeId);
        }

        if (trade.getProductType() != ProductType.BOND) {
            throw new IllegalArgumentException("Trade is not a bond: " + tradeId);
        }

        if (!(trade.getProductDetails() instanceof BondTradeDetails)) {
            throw new IllegalArgumentException("Bond trade details are missing or invalid: " + tradeId);
        }

        BondTradeDetails details = (BondTradeDetails) trade.getProductDetails();

        List<Cashflow> cashflows = cashflowService.generateAnnualCashflows(
                trade.getNominal(),
                details.getCouponRate(),
                yearsToMaturity
        );

        return pricingService.presentValue(cashflows, discountRate);
    }
}