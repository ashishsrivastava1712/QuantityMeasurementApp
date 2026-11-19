/*
 * Class Name  : RepositoryConfiguration
 * Description : Configures repository, service, and controller beans.
 * Author      : Ashish Srivastava
 * Date        : 19/11/2026
 */
package com.quantitymeasurement.config;

import com.quantitymeasurement.controller.QuantityMeasurementController;
import com.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.quantitymeasurement.repository.RepositoryFactory;
import com.quantitymeasurement.service.IQuantityMeasurementService;
import com.quantitymeasurement.service.QuantityMeasurementServiceImpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class RepositoryConfiguration {

    // Selects CACHE or DATABASE using application properties.
    @Bean
    public IQuantityMeasurementRepository quantityMeasurementRepository(
            DataSource dataSource,
            @Value("${app.repository.type:DATABASE}") String repositoryType) {

        return RepositoryFactory.createRepository(
                repositoryType, dataSource);
    }

    // Injects the selected repository into the service.
    @Bean
    public IQuantityMeasurementService quantityMeasurementService(
            IQuantityMeasurementRepository repository) {

        return new QuantityMeasurementServiceImpl(repository);
    }

    // Injects the service into the controller.
    @Bean
    public QuantityMeasurementController quantityMeasurementController(
            IQuantityMeasurementService service) {

        return new QuantityMeasurementController(service);
    }
}