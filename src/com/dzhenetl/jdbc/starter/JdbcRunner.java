package com.dzhenetl.jdbc.starter;

import com.dzhenetl.jdbc.starter.util.ConnectionManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JdbcRunner {
    public static void main(String[] args) throws SQLException {
        try {
            checkMetaData();
        } finally {
            ConnectionManager.closePool();
        }
    }

    private static void checkMetaData() throws SQLException {
        try (Connection connection = ConnectionManager.get()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet catalogs = metaData.getCatalogs();
            while (catalogs.next()) {
                String catalog = catalogs.getString(1);
                ResultSet schemas = metaData.getSchemas();
                while (schemas.next()) {
                    String schema = schemas.getString("TABLE_SCHEM");
                    ResultSet tables = metaData.getTables(catalog, schema, "%", new String[] {"TABLE"});
                    if (schema.equals("public")) {
                        while (tables.next()) {
                            System.out.println(tables.getString("TABLE_NAME"));
                        }
                    }
                }

            }
        }
    }

    private static List<Long> getFlightsBetween(LocalDateTime start, LocalDateTime end) throws SQLException {
        String sql = "SELECT id FROM flight WHERE departure_date BETWEEN ? AND ?";
        try (Connection connection = ConnectionManager.get();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setFetchSize(50);
            statement.setQueryTimeout(10);
            statement.setTimestamp(1, Timestamp.valueOf(start));
            statement.setTimestamp(2, Timestamp.valueOf(end));
            ResultSet resultSet = statement.executeQuery();
            List<Long> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(resultSet.getObject("id", Long.class));
            }
            return result;
        }
    }

    private static List<Long> getTicketsByFlightId(Long flightId) throws SQLException {
        String sql = "SELECT id from ticket where flight_id = ?";
        List<Long> result = new ArrayList<>();
        try (Connection connection = ConnectionManager.get();
             PreparedStatement prepareStatement = connection.prepareStatement(sql)) {
            prepareStatement.setLong(1, flightId);
            ResultSet resultSet = prepareStatement.executeQuery();
            while (resultSet.next()) {
                result.add(resultSet.getObject("id", Long.class));
            }
        }
        return result;
    }
}

