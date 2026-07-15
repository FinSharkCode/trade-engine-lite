package com.adrian.tradeengine.web;

import com.adrian.tradeengine.io.TradeCsvParser;
import com.adrian.tradeengine.model.Trade;
import com.adrian.tradeengine.service.H2TradeRepository;
import com.adrian.tradeengine.service.TradeRepository;
import com.adrian.tradeengine.service.TradeXmlExporter;
import com.adrian.tradeengine.validation.TradeValidator;
import com.adrian.tradeengine.service.BondCashflowService;
import com.adrian.tradeengine.service.BondPricingService;
import com.adrian.tradeengine.service.TradePricingService;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import java.util.Locale;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import javax.ws.rs.PathParam;

@Path("/trades")
public class TradeResource {

    private static final TradeRepository DEFAULT_REPOSITORY = new H2TradeRepository();

    private final TradeRepository repository;
    private final TradeCsvParser parser = new TradeCsvParser();
    private final TradeValidator validator = new TradeValidator();
    private final TradeXmlExporter xmlExporter = new TradeXmlExporter();
    private final TradePricingService tradePricingService;

    public TradeResource() {
        this(DEFAULT_REPOSITORY);
    }

    TradeResource(TradeRepository repository) {
        this.repository = repository;
        this.tradePricingService = new TradePricingService(
                repository,
                new BondCashflowService(),
                new BondPricingService()
        );
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String createTrade(String csvLine) {
        Trade trade = parser.parseLine(csvLine);

        if (!validator.isValid(trade)) {
            return "Trade is invalid";
        }

        if (repository.existsByTradeId(trade.getTradeId())) {
            return "Trade already exists: " + trade.getTradeId();
        }

        repository.save(trade);

        return "Trade stored: " + trade.getTradeId();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getTrades() {
        List<Trade> trades = repository.findAll();

        if (trades.isEmpty()) {
            return "No trades stored";
        }

        StringBuilder result = new StringBuilder();

        for (Trade trade : trades) {
            result.append(formatTrade(trade)).append(System.lineSeparator());
        }

        return result.toString();
    }

    @GET
    @Path("/xml")
    @Produces(MediaType.APPLICATION_XML)
    public String getTradesAsXml() {
        return xmlExporter.exportTrades(repository.findAll());
    }

    private String formatTrade(Trade trade) {
        return trade.getTradeId()
                + " | " + trade.getProductType()
                + " | " + trade.getNominal()
                + " | " + trade.getCurrency()
                + " | " + trade.getCounterparty().getName()
                + " | " + trade.getPortfolio().getName();
    }
    
    @GET
    @Path("/{tradeId}/xml")
    @Produces(MediaType.APPLICATION_XML)
    public String getTradeAsXml(@PathParam("tradeId") String tradeId) {
        Trade trade = repository.findByTradeId(tradeId);

        if (trade == null) {
            return "<error>Trade not found: " + tradeId + "</error>";
        }

        return xmlExporter.exportSingleTrade(trade);
    }
    
    @GET
    @Path("/{tradeId}/price")
    @Produces(MediaType.TEXT_PLAIN)
    public String getBondPrice(
            @PathParam("tradeId") String tradeId,
            @QueryParam("discountRate") @DefaultValue("0.04") double discountRate,
            @QueryParam("yearsToMaturity") @DefaultValue("5") int yearsToMaturity
    ) {
        try {
            double presentValue = tradePricingService.priceBondTrade(
                    tradeId,
                    discountRate,
                    yearsToMaturity
            );

            return "Present value for trade "
                    + tradeId
                    + ": "
                    + String.format(Locale.US, "%.2f", presentValue);
        } catch (IllegalArgumentException exception) {
            return "Pricing error: " + exception.getMessage();
        }
    }
    
}