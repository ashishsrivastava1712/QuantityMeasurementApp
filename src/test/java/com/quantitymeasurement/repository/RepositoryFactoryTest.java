
/*
 * Class Name  : RepositoryFactoryTest
 * Description : Tests repository selection using RepositoryFactory.
 * Author      : Ashish Srivastava
 * Date        : 19/11/2026
 */
package com.quantitymeasurement.repository;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import static org.junit.jupiter.api.Assertions.*;

class RepositoryFactoryTest {

    @Test
    void testCacheRepositorySelection() {

        IQuantityMeasurementRepository repository =
                RepositoryFactory.createRepository("CACHE", null);

        assertNotNull(repository);
        assertInstanceOf(
                QuantityMeasurementCacheRepository.class, repository);

        assertSame(
                QuantityMeasurementCacheRepository.getInstance(),
                repository);
    }

    @Test
    void testCacheSelectionIsCaseInsensitive() {

        IQuantityMeasurementRepository repository =
                RepositoryFactory.createRepository(" cache ", null);

        assertSame(
                QuantityMeasurementCacheRepository.getInstance(),
                repository);
    }

    @Test
    void testDatabaseRepositorySelection() {

        DriverManagerDataSource dataSource =
                new DriverManagerDataSource();

        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl(
                "jdbc:h2:mem:factory_test;DB_CLOSE_DELAY=-1");
        dataSource.setUsername("sa");
        dataSource.setPassword("");

        IQuantityMeasurementRepository repository =
                RepositoryFactory.createRepository(
                        "DATABASE", dataSource);

        assertNotNull(repository);
        assertInstanceOf(
                QuantityMeasurementDatabaseRepository.class,
                repository);
    }

    @Test
    void testNullRepositoryType() {

        assertThrows(
                IllegalArgumentException.class,
                () -> RepositoryFactory.createRepository(null, null));
    }

    @Test
    void testEmptyRepositoryType() {

        assertThrows(
                IllegalArgumentException.class,
                () -> RepositoryFactory.createRepository(" ", null));
    }

    @Test
    void testUnsupportedRepositoryType() {

        assertThrows(
                IllegalArgumentException.class,
                () -> RepositoryFactory.createRepository("INVALID", null));
    }

    @Test
    void testDatabaseSelectionWithNullDataSource() {

        assertThrows(
                NullPointerException.class,
                () -> RepositoryFactory.createRepository(
                        "DATABASE", null));
    }
}
