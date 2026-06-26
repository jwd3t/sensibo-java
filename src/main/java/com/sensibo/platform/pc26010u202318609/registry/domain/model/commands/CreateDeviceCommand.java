package com.sensibo.platform.pc26010u202318609.registry.domain.model.commands;

import java.time.LocalDate;

public record CreateDeviceCommand(Long userId, String deviceType, String modelName, String serialNumber, String macAddress, String firmwareVersion, LocalDate installationDate, String roomLocation) {

    public CreateDeviceCommand {
        if (userId == null) {
            throw new IllegalArgumentException("[CreateDeviceCommand] userId cannot be null");
        }
        if (deviceType == null || deviceType.isBlank()) {
            throw new IllegalArgumentException("[CreateDeviceCommand] deviceType cannot be null or blank");
        }
        if (modelName == null || modelName.isBlank()) {
            throw new IllegalArgumentException("[CreateDeviceCommand] modelName cannot be null or blank");
        }
        if (serialNumber == null || serialNumber.isBlank()) {
            throw new IllegalArgumentException("[CreateDeviceCommand] serialNumber cannot be null or blank");
        }
        if (macAddress == null || macAddress.isBlank()) {
            throw new IllegalArgumentException("[CreateDeviceCommand] macAddress cannot be null or blank");
        }
        if (firmwareVersion == null || firmwareVersion.isBlank()) {
            throw new IllegalArgumentException("[CreateDeviceCommand] firmwareVersion cannot be null or blank");
        }
        if (installationDate == null) {
            throw new IllegalArgumentException("[CreateDeviceCommand] installationDate cannot be null");
        }
        if (roomLocation == null || roomLocation.isBlank()) {
            throw new IllegalArgumentException("[CreateDeviceCommand] roomLocation cannot be null or blank");
        }
    }
}
