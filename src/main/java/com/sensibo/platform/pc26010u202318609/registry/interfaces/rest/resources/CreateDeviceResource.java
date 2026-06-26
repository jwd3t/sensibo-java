package com.sensibo.platform.pc26010u202318609.registry.interfaces.rest.resources;

import com.sensibo.platform.pc26010u202318609.registry.domain.model.valueobjects.DeviceTypes;

import java.time.LocalDate;

public record CreateDeviceResource(Long userId, String deviceType, String modelName,
                                   String macAddress, String firmwareVersion, LocalDate installationDate,
                                   String roomLocation) {
  public CreateDeviceResource {
    if (userId == null || userId.intValue() < 0) {
      throw new IllegalArgumentException("userId cannot be null or negative");
    }
    if (deviceType == null || deviceType.isBlank() || DeviceTypes.valueOf(deviceType) == null) {
      throw new IllegalArgumentException("deviceType cannot be null or blank");
    }
    if (modelName == null || modelName.isBlank()) {
      throw new IllegalArgumentException("modelName cannot be null or blank");
    }
    if (macAddress == null || macAddress.isBlank()) {
      throw new IllegalArgumentException("macAddress cannot be null or blank");
    }
    if (firmwareVersion == null || firmwareVersion.isBlank()) {
      throw new IllegalArgumentException("firmwareVersion cannot be null or blank");
    }
    if (installationDate == null) {
      throw new IllegalArgumentException("installationDate cannot be null");
    }
    if (roomLocation == null || roomLocation.isBlank()) {
      throw new IllegalArgumentException("roomLocation cannot be null or blank");
    }

  }
}
