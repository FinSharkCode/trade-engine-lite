package com.adrian.tradeengine.service;

import com.adrian.tradeengine.model.BondTradeDetails;
import com.adrian.tradeengine.model.Counterparty;
import com.adrian.tradeengine.model.EquityTradeDetails;
import com.adrian.tradeengine.model.FxTradeDetails;
import com.adrian.tradeengine.model.Portfolio;
import com.adrian.tradeengine.model.ProductDetails;
import com.adrian.tradeengine.model.ProductType;
import com.adrian.tradeengine.model.Trade;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class H2TradeRepository implements TradeRepository {

    private final String jdbcUrl;

    public H2TradeRepository() {
        this(defaultJdbcUrl());
    }

    public H2TradeRepository(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
        loadDriver();
        createTableIfMissing();
        addProductDetailColumnsIfMissing();
    }

    @Override
    public void save(Trade trade) {
        String sql = "INSERT INTO trades " +
                "(trade_id, product_type, nominal, currency, counterparty_name, portfolio_name, " +
                "fx_currency_pair, fx_settlement_date, fx_exchange_rate, " +
                "bond_isin, bond_issuer, bond_maturity_date, bond_coupon_rate, " +
                "equity_ticker, equity_exchange, equity_quantity, equity_price) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, trade.getTradeId());
            statement.setString(2, trade.getProductType().name());
            statement.setDouble(3, trade.getNominal());
            statement.setString(4, trade.getCurrency());
            statement.setString(5, trade.getCounterparty().getName());
            statement.setString(6, trade.getPortfolio().getName());

            setProductDetailParameters(statement, trade.getProductDetails());

            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Could not save trade " + trade.getTradeId(), exception);
        }
    }

    @Override
    public boolean existsByTradeId(String tradeId) {
        String sql = "SELECT trade_id FROM trades WHERE trade_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, tradeId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Could not check trade ID " + tradeId, exception);
        }
    }

    @Override
    public Trade findByTradeId(String tradeId) {
        String sql = selectAllColumnsSql() + " WHERE trade_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, tradeId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRowToTrade(resultSet);
                }
                return null;
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Could not load trade " + tradeId, exception);
        }
    }

    @Override
    public List<Trade> findAll() {
        String sql = selectAllColumnsSql() + " ORDER BY trade_id";

        List<Trade> trades = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                trades.add(mapRowToTrade(resultSet));
            }

            return trades;
        } catch (SQLException exception) {
            throw new RuntimeException("Could not load trades", exception);
        }
    }

    @Override
    public int count() {
        String sql = "SELECT COUNT(*) FROM trades";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException exception) {
            throw new RuntimeException("Could not count trades", exception);
        }
    }

    @Override
    public void clear() {
        String sql = "DELETE FROM trades";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Could not clear trades", exception);
        }
    }

    private String selectAllColumnsSql() {
        return "SELECT trade_id, product_type, nominal, currency, counterparty_name, portfolio_name, " +
                "fx_currency_pair, fx_settlement_date, fx_exchange_rate, " +
                "bond_isin, bond_issuer, bond_maturity_date, bond_coupon_rate, " +
                "equity_ticker, equity_exchange, equity_quantity, equity_price " +
                "FROM trades";
    }

    private void setProductDetailParameters(PreparedStatement statement, ProductDetails details) throws SQLException {
        if (details instanceof FxTradeDetails) {
            FxTradeDetails fx = (FxTradeDetails) details;

            statement.setString(7, fx.getCurrencyPair());
            statement.setString(8, fx.getSettlementDate());
            statement.setDouble(9, fx.getExchangeRate());

            statement.setString(10, null);
            statement.setString(11, null);
            statement.setString(12, null);
            statement.setObject(13, null);

            statement.setString(14, null);
            statement.setString(15, null);
            statement.setObject(16, null);
            statement.setObject(17, null);
            return;
        }

        if (details instanceof BondTradeDetails) {
            BondTradeDetails bond = (BondTradeDetails) details;

            statement.setString(7, null);
            statement.setString(8, null);
            statement.setObject(9, null);

            statement.setString(10, bond.getIsin());
            statement.setString(11, bond.getIssuer());
            statement.setString(12, bond.getMaturityDate());
            statement.setDouble(13, bond.getCouponRate());

            statement.setString(14, null);
            statement.setString(15, null);
            statement.setObject(16, null);
            statement.setObject(17, null);
            return;
        }

        if (details instanceof EquityTradeDetails) {
            EquityTradeDetails equity = (EquityTradeDetails) details;

            statement.setString(7, null);
            statement.setString(8, null);
            statement.setObject(9, null);

            statement.setString(10, null);
            statement.setString(11, null);
            statement.setString(12, null);
            statement.setObject(13, null);

            statement.setString(14, equity.getTicker());
            statement.setString(15, equity.getExchange());
            statement.setInt(16, equity.getQuantity());
            statement.setDouble(17, equity.getPrice());
            return;
        }

        statement.setString(7, null);
        statement.setString(8, null);
        statement.setObject(9, null);
        statement.setString(10, null);
        statement.setString(11, null);
        statement.setString(12, null);
        statement.setObject(13, null);
        statement.setString(14, null);
        statement.setString(15, null);
        statement.setObject(16, null);
        statement.setObject(17, null);
    }

    private Trade mapRowToTrade(ResultSet resultSet) throws SQLException {
        ProductType productType = ProductType.valueOf(resultSet.getString("product_type"));

        return new Trade(
                resultSet.getString("trade_id"),
                productType,
                resultSet.getDouble("nominal"),
                resultSet.getString("currency"),
                new Counterparty(resultSet.getString("counterparty_name")),
                new Portfolio(resultSet.getString("portfolio_name")),
                mapRowToProductDetails(resultSet, productType)
        );
    }

    private ProductDetails mapRowToProductDetails(ResultSet resultSet, ProductType productType) throws SQLException {
        if (productType == ProductType.FX) {
            String currencyPair = resultSet.getString("fx_currency_pair");
            String settlementDate = resultSet.getString("fx_settlement_date");
            Double exchangeRate = getNullableDouble(resultSet, "fx_exchange_rate");

            if (currencyPair != null && settlementDate != null && exchangeRate != null) {
                return new FxTradeDetails(currencyPair, settlementDate, exchangeRate);
            }

            return new FxTradeDetails("EUR/USD", "2026-07-15", 1.08);
        }

        if (productType == ProductType.BOND) {
            String isin = resultSet.getString("bond_isin");
            String issuer = resultSet.getString("bond_issuer");
            String maturityDate = resultSet.getString("bond_maturity_date");
            Double couponRate = getNullableDouble(resultSet, "bond_coupon_rate");

            if (isin != null && issuer != null && maturityDate != null && couponRate != null) {
                return new BondTradeDetails(isin, issuer, maturityDate, couponRate);
            }

            return new BondTradeDetails("DE0001234567", "Issuer A", "2030-12-31", 0.035);
        }

        if (productType == ProductType.EQUITY) {
            String ticker = resultSet.getString("equity_ticker");
            String exchange = resultSet.getString("equity_exchange");
            Integer quantity = getNullableInteger(resultSet, "equity_quantity");
            Double price = getNullableDouble(resultSet, "equity_price");

            if (ticker != null && exchange != null && quantity != null && price != null) {
                return new EquityTradeDetails(ticker, exchange, quantity, price);
            }

            return new EquityTradeDetails("AAPL", "NASDAQ", 100, 195.50);
        }

        return null;
    }

    private Double getNullableDouble(ResultSet resultSet, String columnName) throws SQLException {
        double value = resultSet.getDouble(columnName);
        if (resultSet.wasNull()) {
            return null;
        }
        return value;
    }

    private Integer getNullableInteger(ResultSet resultSet, String columnName) throws SQLException {
        int value = resultSet.getInt(columnName);
        if (resultSet.wasNull()) {
            return null;
        }
        return value;
    }

    private void createTableIfMissing() {
        String sql = "CREATE TABLE IF NOT EXISTS trades (" +
                "trade_id VARCHAR(100) PRIMARY KEY, " +
                "product_type VARCHAR(50) NOT NULL, " +
                "nominal DOUBLE NOT NULL, " +
                "currency VARCHAR(20) NOT NULL, " +
                "counterparty_name VARCHAR(255) NOT NULL, " +
                "portfolio_name VARCHAR(255) NOT NULL" +
                ")";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Could not create trades table", exception);
        }
    }

    private void addProductDetailColumnsIfMissing() {
        addColumnIfMissing("fx_currency_pair", "VARCHAR(50)");
        addColumnIfMissing("fx_settlement_date", "VARCHAR(50)");
        addColumnIfMissing("fx_exchange_rate", "DOUBLE");

        addColumnIfMissing("bond_isin", "VARCHAR(50)");
        addColumnIfMissing("bond_issuer", "VARCHAR(255)");
        addColumnIfMissing("bond_maturity_date", "VARCHAR(50)");
        addColumnIfMissing("bond_coupon_rate", "DOUBLE");

        addColumnIfMissing("equity_ticker", "VARCHAR(50)");
        addColumnIfMissing("equity_exchange", "VARCHAR(50)");
        addColumnIfMissing("equity_quantity", "INT");
        addColumnIfMissing("equity_price", "DOUBLE");
    }

    private void addColumnIfMissing(String columnName, String columnType) {
        if (columnExists(columnName)) {
            return;
        }

        String sql = "ALTER TABLE trades ADD COLUMN " + columnName + " " + columnType;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Could not add column " + columnName, exception);
        }
    }

    private boolean columnExists(String columnName) {
        try (Connection connection = getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();

            try (ResultSet columns = metaData.getColumns(null, null, "TRADES", columnName.toUpperCase())) {
                return columns.next();
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Could not inspect trades table metadata", exception);
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl);
    }

    private void loadDriver() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException exception) {
            throw new RuntimeException("H2 driver not found", exception);
        }
    }

    private static String defaultJdbcUrl() {
        File dataDirectory = new File(System.getProperty("user.home"), "trade-engine-lite-data");
        if (!dataDirectory.exists()) {
            dataDirectory.mkdirs();
        }

        File databaseFile = new File(dataDirectory, "trades");
        String databasePath = databaseFile.getAbsolutePath().replace("\\", "/");

        return "jdbc:h2:file:" + databasePath;
    }
}