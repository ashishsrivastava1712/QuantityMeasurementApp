
/*
 * Class Name  : RepositoryFactory
 * Description : Creates the configured measurement repository.
 * Author      : Ashish Srivastava
 * Date        : 19/11/2026
 */
package com.quantitymeasurement.repository;

import javax.sql.DataSource;
import java.util.Locale;
import java.util.Objects;

public final class RepositoryFactory {

    private RepositoryFactory() {
    }

    // Creates a repository based on the selected storage strategy.
    public static IQuantityMeasurementRepository createRepository(
            String repositoryType, DataSource dataSource) {

        if (repositoryType == null || repositoryType.isBlank()) {
            throw new IllegalArgumentException(
                    "Repository type cannot be null or empty");
        }

        return switch (repositoryType.trim().toUpperCase(Locale.ROOT)) {
            case "CACHE" -> QuantityMeasurementCacheRepository.getInstance();

            case "DATABASE" -> new QuantityMeasurementDatabaseRepository(
                    Objects.requireNonNull(
                            dataSource, "DataSource cannot be null"));

            default -> throw new IllegalArgumentException(
                    "Unsupported repository type: " + repositoryType);
        };
    }
}
