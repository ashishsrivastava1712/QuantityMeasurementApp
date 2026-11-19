package com.quantitymeasurement.repository;

import com.quantitymeasurement.entity.QuantityMeasurementEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class JpaQuantityMeasurementStore {

    private final QuantityMeasurementJpaRepository jpaRepository;

    public JpaQuantityMeasurementStore(
            QuantityMeasurementJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    public QuantityMeasurementEntity save(QuantityMeasurementEntity entity) {
        return jpaRepository.save(entity);
    }

    public List<QuantityMeasurementEntity> findAll() {
        return jpaRepository.findAll();
    }

    public Optional<QuantityMeasurementEntity> findById(String id) {
        return jpaRepository.findById(id);
    }

    public void deleteAll() {
        jpaRepository.deleteAll();
    }
}