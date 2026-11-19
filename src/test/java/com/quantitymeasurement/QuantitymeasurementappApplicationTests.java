
/*
 * Class Name  : QuantitymeasurementappApplicationTests
 * Description : Validates Spring Boot context and dependency injection.
 * Author      : Ashish Srivastava
 * Date        : 19/11/2026
 */
package com.quantitymeasurement;

import com.quantitymeasurement.controller.QuantityMeasurementController;
import com.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.quantitymeasurement.repository.QuantityMeasurementDatabaseRepository;
import com.quantitymeasurement.repository.QuantityMeasurementJpaRepositoryAdapter;
import com.quantitymeasurement.service.IQuantityMeasurementService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class QuantitymeasurementappApplicationTests {

	@Autowired
	private IQuantityMeasurementRepository repository;

	@Autowired
	private IQuantityMeasurementService service;

	@Autowired
	private QuantityMeasurementController controller;

	@Test
	void contextLoads() {
		assertNotNull(repository);
		assertNotNull(service);
		assertNotNull(controller);
	}

	@Test
	void testJpaRepositoryIsInjectedByDefault() {
		assertInstanceOf(
				QuantityMeasurementJpaRepositoryAdapter.class,
				repository
		);
	}
}
