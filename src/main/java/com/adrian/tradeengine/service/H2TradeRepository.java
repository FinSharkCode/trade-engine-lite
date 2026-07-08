package com.adrian.tradeengine.service;

import com.adrian.tradeengine.model.Counterparty;
import com.adrian.tradeengine.model.Portfolio;
import com.adrian.tradeengine.model.ProductType;
import com.adrian.tradeengine.model.Trade;

import java.io.File;
import java.sql.Connection;
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
    }

    @Override
    public void save(Trade trade) {
        String sql = "INSERT INTO trades " +
                "(trade_id, product_type, nominal, currency, counterparty_name, portfolio_name) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, trade.getTradeId());
            statement.setString(2, trade.getProductType().name());
            statement.setDouble(3, trade.getNominal());
            statement.setString(4, trade.getCurrency());
            statement.setString(5, trade.getCounterparty().getName());
            statement.setString(6, trade.getPortfolio().getName());

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
    public List<Trade> findAll() {
        String sql = "SELECT trade_id, product_type, nominal, currency, counterparty_name, portfolio_name " +
                "FROM trades ORDER BY trade_id";

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

    private Trade mapRowToTrade(ResultSet resultSet) throws SQLException {
        return new Trade(
                resultSet.getString("trade_id"),
                ProductType.valueOf(resultSet.getString("product_type")),
                resultSet.getDouble("nominal"),
                resultSet.getString("currency"),
                new Counterparty(resultSet.getString("counterparty_name")),
                new Portfolio(resultSet.getString("portfolio_name"))
        );
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