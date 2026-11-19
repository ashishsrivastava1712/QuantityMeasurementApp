
/*
 * Class Name  : QuantityMeasurementJpaRepositoryAdapter
 * Description : Adapts Spring Data JPA to the existing repository contract.
 * Author      : Ashish Srivastava
 * Date        : 19/11/2026
 */
package com.quantitymeasurement.repository;

import com.quantitymeasurement.entity.QuantityMeasurementEntity;
import java.util.List;

public class QuantityMeasurementJpaRepositoryAdapter
        implements IQuantityMeasurementRepository {

    private final QuantityMeasurementJpaRepository jpaRepository;

    public QuantityMeasurementJpaRepositoryAdapter(
            QuantityMeasurementJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(QuantityMeasurementEntity entity) {
        jpaRepository.save(entity);
    }

    @Override
    public List<QuantityMeasurementEntity> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public QuantityMeasurementEntity findById(String id) {
        return jpaRepository.findById(id).orElse(null);
    }

    @Override
    public void clear() {
        jpaRepository.deleteAll();
    }
}
