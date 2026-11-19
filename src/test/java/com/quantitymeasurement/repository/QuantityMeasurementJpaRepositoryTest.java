package com.quantitymeasurement.repository;

import com.quantitymeasurement.entity.QuantityMeasurementEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class QuantityMeasurementJpaRepositoryTest {

    @Autowired
    private QuantityMeasurementJpaRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void shouldSaveAndFindMeasurement() {
        QuantityMeasurementEntity entity =
                new QuantityMeasurementEntity(10.0, "FEET");

        QuantityMeasurementEntity saved = repository.save(entity);

        assertNotNull(saved.getId());
        assertTrue(repository.findById(saved.getId()).isPresent());
    }

    @Test
    void shouldFindAllMeasurements() {
        repository.save(new QuantityMeasurementEntity(10.0, "FEET"));
        repository.save(new QuantityMeasurementEntity(20.0, "INCHES"));

        assertEquals(2, repository.findAll().size());
    }

    @Test
    void shouldDeleteAllMeasurements() {
        repository.save(new QuantityMeasurementEntity(10.0, "FEET"));

        repository.deleteAll();

        assertTrue(repository.findAll().isEmpty());
    }
}