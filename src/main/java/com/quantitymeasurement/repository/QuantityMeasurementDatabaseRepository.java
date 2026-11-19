
/*
 * Class Name  : QuantityMeasurementDatabaseRepository
 * Description : Provides JDBC-based persistent measurement storage.
 * Author      : Ashish Srivastava
 * Date        : 19/11/2026
 */
package com.quantitymeasurement.repository;

import com.quantitymeasurement.entity.QuantityMeasurementEntity;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class QuantityMeasurementDatabaseRepository
        implements IQuantityMeasurementRepository {

    private final DataSource dataSource;

    // Injects the database connection pool and initializes the schema.
    public QuantityMeasurementDatabaseRepository(DataSource dataSource) {
        this.dataSource = Objects.requireNonNull(
                dataSource, "DataSource cannot be null");

        new DatabaseSchemaInitializer(dataSource).initializeSchema();
    }

    // Saves a measurement record using a parameterized SQL query.
    @Override
    public void save(QuantityMeasurementEntity entity) {
        Objects.requireNonNull(entity, "Entity cannot be null");

        String sql = """
                INSERT INTO quantity_measurements
                (id, `first_value`, first_unit, second_value, second_unit,
                 operation, `result`, result_unit, error_message)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement statement =
                         connection.prepareStatement(sql)) {

                statement.setString(1, entity.getId());
                statement.setDouble(2, entity.getFirstValue());
                statement.setString(3, entity.getFirstUnit());
                setNullableDouble(statement, 4, entity.getSecondValue());
                statement.setString(5, entity.getSecondUnit());
                statement.setString(6, entity.getOperation());
                setNullableDouble(statement, 7, entity.getResult());
                statement.setString(8, entity.getResultUnit());
                statement.setString(9, entity.getErrorMessage());

                statement.executeUpdate();
                connection.commit();

            } catch (SQLException exception) {
                rollback(connection, exception);
                throw exception;
            }

        } catch (SQLException exception) {
            throw new IllegalStateException(
                    "Unable to save measurement record", exception);
        }
    }

    // Retrieves all measurement records from the database.
    @Override
    public List<QuantityMeasurementEntity> findAll() {
        String sql = """
                SELECT id, `first_value`, first_unit, second_value,
                       second_unit, operation, `result`, result_unit,
                       error_message
                FROM quantity_measurements
                ORDER BY created_at DESC
                """;

        List<QuantityMeasurementEntity> records = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                records.add(mapEntity(resultSet));
            }

            return records;

        } catch (SQLException exception) {
            throw new IllegalStateException(
                    "Unable to retrieve measurement records", exception);
        }
    }

    // Retrieves a measurement record using its UUID.
    @Override
    public QuantityMeasurementEntity findById(String id) {
        if (id == null) {
            return null;
        }

        String sql = """
                SELECT id, `first_value`, first_unit, second_value,
                       second_unit, operation, `result`, result_unit,
                       error_message
                FROM quantity_measurements
                WHERE id = ?
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? mapEntity(resultSet) : null;
            }

        } catch (SQLException exception) {
            throw new IllegalStateException(
                    "Unable to retrieve measurement record", exception);
        }
    }

    // Deletes all measurement records in a single transaction.
    @Override
    public void clear() {
        String sql = "DELETE FROM quantity_measurements";

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement statement =
                         connection.prepareStatement(sql)) {

                statement.executeUpdate();
                connection.commit();

            } catch (SQLException exception) {
                rollback(connection, exception);
                throw exception;
            }

        } catch (SQLException exception) {
            throw new IllegalStateException(
                    "Unable to clear measurement records", exception);
        }
    }

    // Reconstructs an entity while preserving its database ID.
    private QuantityMeasurementEntity mapEntity(ResultSet resultSet)
            throws SQLException {

        return QuantityMeasurementEntity.fromDatabase(
                resultSet.getString("id"),
                resultSet.getDouble("first_value"),
                resultSet.getString("first_unit"),
                getNullableDouble(resultSet, "second_value"),
                resultSet.getString("second_unit"),
                resultSet.getString("operation"),
                getNullableDouble(resultSet, "result"),
                resultSet.getString("result_unit"),
                resultSet.getString("error_message")
        );
    }

    // Handles nullable numeric database columns.
    private void setNullableDouble(
            PreparedStatement statement, int index, Double value)
            throws SQLException {

        if (value == null) {
            statement.setNull(index, Types.DOUBLE);
        } else {
            statement.setDouble(index, value);
        }
    }

    // Reads nullable numeric database columns.
    private Double getNullableDouble(ResultSet resultSet, String column)
            throws SQLException {

        double value = resultSet.getDouble(column);
        return resultSet.wasNull() ? null : value;
    }

    // Rolls back failed database transactions.
    private void rollback(
            Connection connection, SQLException originalException) {

        try {
            connection.rollback();
        } catch (SQLException rollbackException) {
            originalException.addSuppressed(rollbackException);
        }
    }
}
