package com.adrian.tradeengine.service;

import com.adrian.tradeengine.model.Trade;

import java.util.List;

public interface TradeRepository {

    void save(Trade trade);

    boolean existsByTradeId(String tradeId);

    Trade findByTradeId(String tradeId);

    List<Trade> findAll();

    int count();

    void clear();
}
