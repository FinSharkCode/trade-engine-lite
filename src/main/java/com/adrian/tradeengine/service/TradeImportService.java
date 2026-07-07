package com.adrian.tradeengine.service;

import com.adrian.tradeengine.io.TradeCsvParser;
import com.adrian.tradeengine.model.Trade;
import com.adrian.tradeengine.validation.TradeValidator;

import java.util.List;
import java.util.stream.Collectors;

public class TradeImportService {

    private final TradeCsvParser parser;
    private final TradeValidator validator;

    public TradeImportService(TradeCsvParser parser, TradeValidator validator) {
        this.parser = parser;
        this.validator = validator;
    }

    public List<Trade> importValidTrades(List<String> lines) {
        return lines.stream()
                .map(parser::parseLine)
                .filter(validator::isValid)
                .collect(Collectors.toList());
    }
}