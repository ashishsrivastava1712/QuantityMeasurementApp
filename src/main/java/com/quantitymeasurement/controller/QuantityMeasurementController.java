
/*
 * Class Name  : QuantityMeasurementController
 * Description : Exposes REST endpoints for quantity measurement operations.
 * Author      : Ashish Srivastava
 * Date        : 19/11/2026
 */
package com.quantitymeasurement.controller;

import com.quantitymeasurement.dto.QuantityDTO;
import com.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.quantitymeasurement.exception.QuantityMeasurementException;
import com.quantitymeasurement.service.IQuantityMeasurementService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@RestController
@RequestMapping("/api/measurements")
public class QuantityMeasurementController {

    private final IQuantityMeasurementService service;

    public QuantityMeasurementController(IQuantityMeasurementService service) {
        this.service = Objects.requireNonNull(
                service, "Service cannot be null");
    }

    // REST endpoint: POST /api/measurements/convert
    @PostMapping("/convert")
    public ResponseEntity<QuantityMeasurementEntity> convertApi(
            @RequestBody @Valid ConversionRequest request) {

        if (request == null) {
            throw new QuantityMeasurementException(
                    "Conversion request cannot be null");
        }

        return ResponseEntity.ok(
                convert(request.source(), request.targetUnit()));
    }

    // REST endpoint: POST /api/measurements/compare
    @PostMapping("/compare")
    public ResponseEntity<QuantityMeasurementEntity> compareApi(
            @RequestBody @Valid ComparisonRequest request) {

        if (request == null) {
            throw new QuantityMeasurementException(
                    "Comparison request cannot be null");
        }

        return ResponseEntity.ok(
                compare(request.first(), request.second()));
    }

    // REST endpoint: POST /api/measurements/calculate
    @PostMapping("/calculate")
    public ResponseEntity<QuantityMeasurementEntity> calculateApi(
            @RequestBody @Valid CalculationRequest request) {

        if (request == null) {
            throw new QuantityMeasurementException(
                    "Calculation request cannot be null");
        }

        return ResponseEntity.ok(
                calculate(
                        request.first(),
                        request.second(),
                        request.operation()));
    }

    // REST endpoint: GET /api/measurements
    @GetMapping
    public ResponseEntity<List<QuantityMeasurementEntity>>
    getAllMeasurementsApi() {

        return ResponseEntity.ok(getAllMeasurements());
    }

    // REST endpoint: GET /api/measurements/{id}
    @GetMapping("/{id}")
    public ResponseEntity<QuantityMeasurementEntity> getMeasurementByIdApi(
            @PathVariable("id") String id) {

        QuantityMeasurementEntity measurement = getMeasurementById(id);

        if (measurement == null) {
            throw new NoSuchElementException(
                    "Measurement not found with ID: " + id);
        }

        return ResponseEntity.ok(measurement);
    }

    // Existing application methods retained for backward compatibility.

    public QuantityMeasurementEntity convert(
            QuantityDTO source, String targetUnit) {

        validateRequest(source);
        validateTargetUnit(targetUnit);

        return service.convert(source, targetUnit);
    }

    public QuantityMeasurementEntity compare(
            QuantityDTO first, QuantityDTO second) {

        validateRequest(first);
        validateRequest(second);

        return service.compare(first, second);
    }

    public QuantityMeasurementEntity calculate(
            QuantityDTO first, QuantityDTO second, String operation) {

        validateRequest(first);
        validateRequest(second);

        if (operation == null || operation.isBlank()) {
            throw new QuantityMeasurementException(
                    "Operation cannot be null or empty");
        }

        return service.calculate(first, second, operation);
    }

    public List<QuantityMeasurementEntity> getAllMeasurements() {
        return service.getAllMeasurements();
    }

    public QuantityMeasurementEntity getMeasurementById(String id) {
        if (id == null || id.isBlank()) {
            throw new QuantityMeasurementException(
                    "Measurement ID cannot be null or empty");
        }

        return service.getMeasurementById(id);
    }

    private void validateRequest(QuantityDTO dto) {
        if (dto == null) {
            throw new QuantityMeasurementException(
                    "Measurement request cannot be null");
        }

        if (dto.getUnit() == null || dto.getUnit().isBlank()) {
            throw new QuantityMeasurementException(
                    "Measurement unit cannot be empty");
        }

        if (!Double.isFinite(dto.getValue())) {
            throw new QuantityMeasurementException(
                    "Measurement value must be finite");
        }
    }

    private void validateTargetUnit(String targetUnit) {
        if (targetUnit == null || targetUnit.isBlank()) {
            throw new QuantityMeasurementException(
                    "Target unit cannot be empty");
        }
    }

    public record ConversionRequest(
            @Valid QuantityDTO source,
            String targetUnit) {
    }

    public record ComparisonRequest(
            @Valid QuantityDTO first,
            @Valid QuantityDTO second) {
    }

    public record CalculationRequest(
            @Valid QuantityDTO first,
            @Valid QuantityDTO second,
            String operation) {
    }
}
