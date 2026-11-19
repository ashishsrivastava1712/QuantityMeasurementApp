
/*
 * Class Name  : DatabaseSchemaInitializer
 * Description : Automatically creates the measurement database table.
 * Author      : Ashish Srivastava
 * Date        : 19/11/2026
 */
package com.quantitymeasurement.repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class DatabaseSchemaInitializer {

    private final DataSource dataSource;

    // Injects the configured database connection pool.
    public DatabaseSchemaInitializer(DataSource dataSource) {
        this.dataSource = Objects.requireNonNull(
                dataSource, "DataSource cannot be null");
    }

    // Creates the measurement history table if it does not exist.
    public void initializeSchema() {
        String createTableSQL = """
        CREATE TABLE IF NOT EXISTS quantity_measurements (
            id VARCHAR(36) PRIMARY KEY,
            `first_value` DOUBLE NOT NULL,
            first_unit VARCHAR(50) NOT NULL,
            second_value DOUBLE NULL,
            second_unit VARCHAR(50) NULL,
            operation VARCHAR(30) NOT NULL,
            `result` DOUBLE NULL,
            result_unit VARCHAR(50) NULL,
            error_message VARCHAR(1000) NULL,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        )
        """;

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(createTableSQL);

        } catch (SQLException exception) {
            throw new IllegalStateException(
                    "Unable to initialize measurement database schema",
                    exception
            );
        }
    }
}
