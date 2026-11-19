
/*
 * Class Name  : RepositoryConfiguration
 * Description : Configures repository and service beans.
 * Author      : Ashish Srivastava
 * Date        : 19/11/2026
 */
package com.quantitymeasurement.config;

import com.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.quantitymeasurement.repository.QuantityMeasurementJpaRepository;
import com.quantitymeasurement.repository.QuantityMeasurementJpaRepositoryAdapter;
import com.quantitymeasurement.repository.RepositoryFactory;
import com.quantitymeasurement.service.IQuantityMeasurementService;
import com.quantitymeasurement.service.QuantityMeasurementServiceImpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class RepositoryConfiguration {

    @Bean
    public IQuantityMeasurementRepository quantityMeasurementRepository(
            DataSource dataSource,
            QuantityMeasurementJpaRepository jpaRepository,
            @Value("${app.repository.type:JPA}") String repositoryType) {

        return switch (repositoryType.trim().toUpperCase()) {
            case "JPA" ->
                    new QuantityMeasurementJpaRepositoryAdapter(jpaRepository);

            case "DATABASE", "CACHE" ->
                    RepositoryFactory.createRepository(repositoryType, dataSource);

            default ->
                    throw new IllegalArgumentException(
                            "Unsupported repository type: " + repositoryType);
        };
    }

    @Bean
    public IQuantityMeasurementService quantityMeasurementService(
            IQuantityMeasurementRepository repository) {

        return new QuantityMeasurementServiceImpl(repository);
    }
}
