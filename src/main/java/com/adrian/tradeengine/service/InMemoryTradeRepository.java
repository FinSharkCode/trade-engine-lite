package com.adrian.tradeengine.service;

import com.adrian.tradeengine.model.Trade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InMemoryTradeRepository implements TradeRepository {

    private final List<Trade> trades = new ArrayList<>();

    @Override
    public void save(Trade trade) {
        trades.add(trade);
    }

    @Override
    public boolean existsByTradeId(String tradeId) {
        for (Trade trade : trades) {
            if (trade.getTradeId().equals(tradeId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Trade> findAll() {
        return Collections.unmodifiableList(trades);
    }

    @Override
    public int count() {
        return trades.size();
    }

    @Override
    public void clear() {
        trades.clear();
    }
}