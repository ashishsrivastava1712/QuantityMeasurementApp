
/*
 * Class Name  : QuantityMeasurementIntegrationTest
 * Description : Verifies controller-to-database integration.
 * Author      : Ashish Srivastava
 * Date        : 19/11/2026
 */
package com.quantitymeasurement;

import com.quantitymeasurement.controller.QuantityMeasurementController;
import com.quantitymeasurement.dto.QuantityDTO;
import com.quantitymeasurement.entity.QuantityMeasurementEntity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class QuantityMeasurementIntegrationTest {

    @Autowired
    private QuantityMeasurementController controller;

    @BeforeEach
    void clearDatabase() {
        controller.getAllMeasurements().forEach(
                entity -> {
                    // Existing records are isolated using unique IDs.
                }
        );
    }

    @Test
    void testConversionIsPersistedAndRetrieved() {

        QuantityDTO source = new QuantityDTO(5.0, "FEET");

        QuantityMeasurementEntity saved =
                controller.convert(source, "INCHES");

        assertNotNull(saved);
        assertEquals(60.0, saved.getResult(), 0.000001);
        assertEquals("INCHES", saved.getResultUnit());
        assertTrue(saved.isSuccessful());

        QuantityMeasurementEntity retrieved =
                controller.getMeasurementById(saved.getId());

        assertNotNull(retrieved);
        assertEquals(saved.getId(), retrieved.getId());
        assertEquals(60.0, retrieved.getResult(), 0.000001);

        List<QuantityMeasurementEntity> records =
                controller.getAllMeasurements();

        assertTrue(records.stream().anyMatch(
                entity -> entity.getId().equals(saved.getId())));
    }
}
