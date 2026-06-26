package com.sensibo.platform.pc26010u202318609.registry.infrastructure.persistence.jpa.repositories;

import com.sensibo.platform.pc26010u202318609.registry.infrastructure.persistence.jpa.entities.DevicePersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DevicePersistenceRepository extends JpaRepository<DevicePersistenceEntity, Long> {
    boolean existsBySerialNumberAndMacAddress(String serialNumber, String macAddress);
}
