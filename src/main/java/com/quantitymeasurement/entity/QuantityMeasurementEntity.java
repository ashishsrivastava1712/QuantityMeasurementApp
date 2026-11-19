
package com.quantitymeasurement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "quantity_measurements")
public class QuantityMeasurementEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "`first_value`", nullable = false)
    private double firstValue;

    @Column(name = "first_unit", nullable = false)
    private String firstUnit;

    @Column(name = "second_value")
    private Double secondValue;

    @Column(name = "second_unit")
    private String secondUnit;

    @Column(name = "operation")
    private String operation;

    @Column(name = "`result`")
    private Double result;

    @Column(name = "result_unit")
    private String resultUnit;

    @Column(name = "error_message", length = 2000)
    private String errorMessage;

    protected QuantityMeasurementEntity() {
    }
    public QuantityMeasurementEntity(double value, String unit) {
        this(UUID.randomUUID().toString(), value, unit,
                null, null, "CONVERSION", null, null, null);
    }

    public QuantityMeasurementEntity(
            double firstValue,
            String firstUnit,
            double secondValue,
            String secondUnit,
            String operation,
            double result,
            String resultUnit) {

        this(UUID.randomUUID().toString(), firstValue, firstUnit,
                secondValue, secondUnit, operation, result, resultUnit, null);
    }

    public QuantityMeasurementEntity(
            double firstValue,
            String firstUnit,
            Double secondValue,
            String secondUnit,
            String operation,
            String errorMessage) {

        this(UUID.randomUUID().toString(), firstValue, firstUnit,
                secondValue, secondUnit, operation, null, null, errorMessage);
    }

    public QuantityMeasurementEntity(
            double value,
            String unit,
            String operation,
            double result,
            String resultUnit) {

        this(UUID.randomUUID().toString(), value, unit,
                null, null, operation, result, resultUnit, null);
    }

    private QuantityMeasurementEntity(
            String id,
            double firstValue,
            String firstUnit,
            Double secondValue,
            String secondUnit,
            String operation,
            Double result,
            String resultUnit,
            String errorMessage) {

        this.id = id;
        this.firstValue = firstValue;
        this.firstUnit = firstUnit;
        this.secondValue = secondValue;
        this.secondUnit = secondUnit;
        this.operation = operation;
        this.result = result;
        this.resultUnit = resultUnit;
        this.errorMessage = errorMessage;
    }

    public static QuantityMeasurementEntity fromDatabase(
            String id,
            double firstValue,
            String firstUnit,
            Double secondValue,
            String secondUnit,
            String operation,
            Double result,
            String resultUnit,
            String errorMessage) {

        return new QuantityMeasurementEntity(
                id, firstValue, firstUnit, secondValue,
                secondUnit, operation, result, resultUnit, errorMessage);
    }

    public String getId() {
        return id;
    }

    public double getFirstValue() {
        return firstValue;
    }

    public String getFirstUnit() {
        return firstUnit;
    }

    public Double getSecondValue() {
        return secondValue;
    }

    public String getSecondUnit() {
        return secondUnit;
    }

    public String getOperation() {
        return operation;
    }

    public Double getResult() {
        return result;
    }

    public String getResultUnit() {
        return resultUnit;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isSuccessful() {
        return errorMessage == null;
    }

    @Override
    public String toString() {
        return "QuantityMeasurementEntity{" +
                "id='" + id + '\'' +
                ", firstValue=" + firstValue +
                ", firstUnit='" + firstUnit + '\'' +
                ", secondValue=" + secondValue +
                ", secondUnit='" + secondUnit + '\'' +
                ", operation='" + operation + '\'' +
                ", result=" + result +
                ", resultUnit='" + resultUnit + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
