
/*
 * Class Name  : MySQLPersistenceVerification
 * Description : Verifies actual MySQL persistence using JDBC.
 * Author      : Ashish Srivastava
 * Date        : 19/11/2026
 */
package com.quantitymeasurement.repository;

import com.quantitymeasurement.entity.QuantityMeasurementEntity;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.List;

public class MySQLPersistenceVerification {

    public static void main(String[] args) {

        String url = System.getenv().getOrDefault(
                "DB_URL",
                "jdbc:mysql://localhost:3306/quantity_measurement_db");

        String username = System.getenv().getOrDefault(
                "DB_USERNAME", "root");

        String password = System.getenv("DB_PASSWORD");

        if (password == null) {
            throw new IllegalStateException(
                    "DB_PASSWORD environment variable is missing");
        }

        DriverManagerDataSource dataSource =
                new DriverManagerDataSource();

        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(
                "com.mysql.cj.jdbc.Driver");

        IQuantityMeasurementRepository repository =
                new QuantityMeasurementDatabaseRepository(dataSource);

        QuantityMeasurementEntity entity =
                new QuantityMeasurementEntity(
                        5.0, "FEET",
                        3.0, "FEET",
                        "ADDITION",
                        8.0, "FEET");

        repository.save(entity);

        System.out.println("Saved ID: " + entity.getId());

        QuantityMeasurementEntity retrieved =
                repository.findById(entity.getId());

        if (retrieved == null) {
            throw new AssertionError("Record was not retrieved");
        }

        System.out.println("Retrieved ID: " + retrieved.getId());
        System.out.println("Retrieved Result: " + retrieved.getResult());
        System.out.println("Retrieved Unit: " + retrieved.getResultUnit());

        List<QuantityMeasurementEntity> records =
                repository.findAll();

        System.out.println("Total Database Records: " + records.size());

        System.out.println("MYSQL PERSISTENCE VERIFIED SUCCESSFULLY");
    }
}
