
/*
 * Interface Name : IQuantityMeasurementService
 * Description    : Defines business operations for quantity measurements.
 * Author         : Ashish Srivastava
 * Date           : 19/11/2026
 */
package com.quantitymeasurement.service;

import com.quantitymeasurement.dto.QuantityDTO;
import com.quantitymeasurement.entity.QuantityMeasurementEntity;

import java.util.List;

public interface IQuantityMeasurementService {

    // Converts a measurement into the requested unit.
    QuantityMeasurementEntity convert(QuantityDTO source, String targetUnit);

    // Compares two measurements.
    QuantityMeasurementEntity compare(QuantityDTO first, QuantityDTO second);

    // Performs addition, subtraction, or division.
    QuantityMeasurementEntity calculate(
            QuantityDTO first, QuantityDTO second, String operation);

    // Returns all saved measurement operations.
    List<QuantityMeasurementEntity> getAllMeasurements();

    // Returns a saved measurement operation by its ID.
    QuantityMeasurementEntity getMeasurementById(String id);
}
