package com.adrian.tradeengine.service;

import com.adrian.tradeengine.model.ProductType;
import com.adrian.tradeengine.model.Trade;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TradeAnalytics {

    public List<Trade> filterByProductType(List<Trade> trades, ProductType productType) {
        return trades.stream()
                .filter(trade -> trade.getProductType() == productType)
                .toList();
    }

    public Map<String, List<Trade>> groupByCounterparty(List<Trade> trades) {
        return trades.stream()
                .collect(Collectors.groupingBy(trade -> trade.getCounterparty().getName()));
    }

    public Map<String, Double> sumNominalByPortfolio(List<Trade> trades) {
        return trades.stream()
                .collect(Collectors.groupingBy(
                        trade -> trade.getPortfolio().getName(),
                        Collectors.summingDouble(Trade::getNominal)
                ));
    }
}