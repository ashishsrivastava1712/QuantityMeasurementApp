
/*
 * Class Name  : RepositoryCacheProfileTest
 * Description : Verifies Spring cache repository configuration.
 * Author      : Ashish Srivastava
 * Date        : 19/11/2026
 */
package com.quantitymeasurement.repository;
import org.springframework.test.context.ActiveProfiles;
import com.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.quantitymeasurement.repository.QuantityMeasurementCacheRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "app.repository.type=CACHE")
@ActiveProfiles("test")
class RepositoryCacheProfileTest {

    @Autowired
    private IQuantityMeasurementRepository repository;

    @Test
    void testCacheRepositoryIsInjected() {
        assertNotNull(repository);

        assertInstanceOf(
                QuantityMeasurementCacheRepository.class,
                repository);
    }
}
