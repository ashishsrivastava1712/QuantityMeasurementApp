/*
 * Class Name  : QuantityMeasurementDatabaseRepositoryTest
 * Description : Tests JDBC database repository operations.
 * Author      : Ashish Srivastava
 * Date        : 19/11/2026
 */
package com.quantitymeasurement.repository;

import com.quantitymeasurement.entity.QuantityMeasurementEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementDatabaseRepositoryTest {

    private QuantityMeasurementDatabaseRepository repository;

    @BeforeEach
    void setUp() {
        DataSource dataSource = createTestDataSource();

        repository = new QuantityMeasurementDatabaseRepository(dataSource);
        repository.clear();
    }

    private DataSource createTestDataSource() {
        DriverManagerDataSource dataSource =
                new DriverManagerDataSource();

        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl(
                "jdbc:h2:mem:" + UUID.randomUUID()
                        + ";DB_CLOSE_DELAY=-1");
        dataSource.setUsername("sa");
        dataSource.setPassword("");

        return dataSource;
    }

    @Test
    void testSaveAndFindById() {
        QuantityMeasurementEntity entity =
                new QuantityMeasurementEntity(
                        10.0, "FEET", "CONVERT", 120.0, "INCHES");

        repository.save(entity);

        QuantityMeasurementEntity retrieved =
                repository.findById(entity.getId());

        assertNotNull(retrieved);
        assertEquals(entity.getId(), retrieved.getId());
        assertEquals(10.0, retrieved.getFirstValue());
        assertEquals("FEET", retrieved.getFirstUnit());
        assertEquals(120.0, retrieved.getResult());
        assertEquals("INCHES", retrieved.getResultUnit());
    }

    @Test
    void testFindAllReturnsSavedRecords() {
        QuantityMeasurementEntity first =
                new QuantityMeasurementEntity(
                        10.0, "FEET", "CONVERT", 120.0, "INCHES");

        QuantityMeasurementEntity second =
                new QuantityMeasurementEntity(
                        5.0, "KILOGRAMS", "CONVERT", 5000.0, "GRAMS");

        repository.save(first);
        repository.save(second);

        List<QuantityMeasurementEntity> records =
                repository.findAll();

        assertEquals(2, records.size());
    }

    @Test
    void testSaveAndRetrieveErrorRecord() {
        QuantityMeasurementEntity entity =
                new QuantityMeasurementEntity(
                        10.0, "FEET", null, null,
                        "ADD", "Incompatible measurement units");

        repository.save(entity);

        QuantityMeasurementEntity retrieved =
                repository.findById(entity.getId());

        assertNotNull(retrieved);
        assertFalse(retrieved.isSuccessful());
        assertNull(retrieved.getSecondValue());
        assertEquals(
                "Incompatible measurement units",
                retrieved.getErrorMessage());
    }

    @Test
    void testFindByIdReturnsNullForUnknownId() {
        assertNull(repository.findById(UUID.randomUUID().toString()));
    }

    @Test
    void testClearDeletesAllRecords() {
        repository.save(
                new QuantityMeasurementEntity(
                        10.0, "FEET", "CONVERT", 120.0, "INCHES"));

        repository.clear();

        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void testFindByIdUsesParameterizedQuery() {
        String unusualId = "test' OR '1'='1";

        QuantityMeasurementEntity entity =
                QuantityMeasurementEntity.fromDatabase(
                        unusualId,
                        10.0, "FEET",
                        null, null,
                        "CONVERT",
                        120.0, "INCHES",
                        null);

        repository.save(entity);

        QuantityMeasurementEntity retrieved =
                repository.findById(unusualId);

        assertNotNull(retrieved);
        assertEquals(unusualId, retrieved.getId());

        assertNull(repository.findById("unknown-id"));
    }
}