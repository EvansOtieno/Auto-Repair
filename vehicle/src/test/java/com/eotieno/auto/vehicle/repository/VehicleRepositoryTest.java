package com.eotieno.auto.vehicle.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.eotieno.auto.vehicle.entity.Vehicle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class VehicleRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Test
    void findByVin_ShouldReturnVehicle() {
        // Given
        Vehicle vehicle = Vehicle.builder()
                .vin("1HGCM82633A123456")
                .make("Toyota")
                .ownerId(1L)
                .build();
        entityManager.persist(vehicle);

        // When
        Vehicle found = vehicleRepository.findByVin("1HGCM82633A123456").orElse(null);

        // Then
        assertThat(found).isNotNull();
        assertThat(found.getVin()).isEqualTo("1HGCM82633A123456");
    }
}
