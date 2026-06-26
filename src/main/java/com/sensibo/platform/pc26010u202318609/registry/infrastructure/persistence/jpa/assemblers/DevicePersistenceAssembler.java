package com.sensibo.platform.pc26010u202318609.registry.infrastructure.persistence.jpa.assemblers;

import com.sensibo.platform.pc26010u202318609.registry.domain.model.aggregates.Device;
import com.sensibo.platform.pc26010u202318609.registry.infrastructure.persistence.jpa.entities.DevicePersistenceEntity;

public final class DevicePersistenceAssembler {
    private DevicePersistenceAssembler() {
    }

    public static DevicePersistenceEntity toPersistenceFromDomain(Device device) {
        DevicePersistenceEntity devicePersistenceEntity = new DevicePersistenceEntity();
        devicePersistenceEntity.setId(device.getId());
        devicePersistenceEntity.setUserId(device.getUserId());
        devicePersistenceEntity.setDeviceType(device.getDeviceType());
        devicePersistenceEntity.setModelName(device.getModelName());
        devicePersistenceEntity.setSerialNumber(device.getSerialNumber());
        devicePersistenceEntity.setMacAddress(device.getMacAddress());
        devicePersistenceEntity.setFirmwareVersion(device.getFirmwareVersion());
        devicePersistenceEntity.setInstallationDate(device.getInstallationDate());
        devicePersistenceEntity.setRoomLocation(device.getRoomLocation());
        devicePersistenceEntity.setDeviceType(device.getDeviceType());
        return devicePersistenceEntity;
    }

    public static Device toDomainFromPersistance(DevicePersistenceEntity devicePersistenceEntity) {
        Device device = new Device();
        device.setId(devicePersistenceEntity.getId());
        device.setUserId(devicePersistenceEntity.getUserId());
        device.setDeviceType(devicePersistenceEntity.getDeviceType());
        device.setModelName(devicePersistenceEntity.getModelName());
        device.setSerialNumber(devicePersistenceEntity.getSerialNumber());
        device.setMacAddress(devicePersistenceEntity.getMacAddress());
        device.setFirmwareVersion(devicePersistenceEntity.getFirmwareVersion());
        device.setInstallationDate(devicePersistenceEntity.getInstallationDate());
        device.setRoomLocation(devicePersistenceEntity.getRoomLocation());
        return device;
    }
}
