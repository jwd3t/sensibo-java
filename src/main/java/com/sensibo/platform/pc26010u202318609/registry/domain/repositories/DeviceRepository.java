package com.sensibo.platform.pc26010u202318609.registry.domain.repositories;

import com.sensibo.platform.pc26010u202318609.registry.domain.model.aggregates.Device;

import java.util.Optional;

public interface DeviceRepository {
    Device save(Device device);

    Optional<Device> findById(Long id);

    boolean existsBySerialNumberAndMacAddress(String serialNumber, String macAddress);
}
