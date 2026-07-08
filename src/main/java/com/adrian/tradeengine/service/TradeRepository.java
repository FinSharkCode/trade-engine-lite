package com.adrian.tradeengine.service;

import com.adrian.tradeengine.model.Trade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TradeRepository {

    private final List<Trade> trades = new ArrayList<>();

    public void save(Trade trade) {
        trades.add(trade);
    }

    public List<Trade> findAll() {
        return Collections.unmodifiableList(trades);
    }

    public int count() {
        return trades.size();
    }

    public void clear() {
        trades.clear();
    }
}