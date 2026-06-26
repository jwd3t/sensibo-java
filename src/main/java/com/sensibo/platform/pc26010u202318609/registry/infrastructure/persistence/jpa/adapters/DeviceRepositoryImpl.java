package com.sensibo.platform.pc26010u202318609.registry.infrastructure.persistence.jpa.adapters;

import com.sensibo.platform.pc26010u202318609.registry.domain.model.aggregates.Device;
import com.sensibo.platform.pc26010u202318609.registry.domain.model.valueobjects.MacAddress;
import com.sensibo.platform.pc26010u202318609.registry.domain.model.valueobjects.SerialNumber;
import com.sensibo.platform.pc26010u202318609.registry.domain.repositories.DeviceRepository;
import com.sensibo.platform.pc26010u202318609.registry.infrastructure.persistence.jpa.assemblers.DevicePersistenceAssembler;
import com.sensibo.platform.pc26010u202318609.registry.infrastructure.persistence.jpa.repositories.DevicePersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class DeviceRepositoryImpl implements DeviceRepository {

    private final DevicePersistenceRepository devicePersistenceRepository;
    public DeviceRepositoryImpl(DevicePersistenceRepository devicePersistenceRepository) {
        this.devicePersistenceRepository = devicePersistenceRepository;
    }

    @Override
    public Device save(Device device) {
        var persistenceSaved = this.devicePersistenceRepository
                .save(DevicePersistenceAssembler.toPersistenceFromDomain(device));
        return DevicePersistenceAssembler.toDomainFromPersistance(persistenceSaved);
    }

    @Override
    public Optional<Device> findById(Long deviceId) {
        // opcion 1
        //var persistence = this.devicePersistenceRepository.findById(deviceId);
        //return Optional.of(DevicePersistenceAssembler.toDomainFromPersistence(persistence.get()));

        //opcion 2
        return this.devicePersistenceRepository.findById(deviceId)
                .map(DevicePersistenceAssembler::toDomainFromPersistance);
    }

    @Override
    public boolean existsBySerialNumberAndMacAddress(String serialNumber, String macAddress) {
        return this.devicePersistenceRepository.existsBySerialNumberAndMacAddress(serialNumber, macAddress);
    }


}
