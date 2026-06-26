package com.sensibo.platform.pc26010u202318609.registry.interfaces.rest.transform;

import com.sensibo.platform.pc26010u202318609.registry.domain.model.aggregates.Device;
import com.sensibo.platform.pc26010u202318609.registry.interfaces.rest.resources.DeviceResource;

public class DeviceResourceFromEntityAssembler {

  public static DeviceResource toResourceFromEntity(Device entity) {
    return new DeviceResource(
            entity.getId(), entity.getSerialNumber().value(), entity.getDeviceType().name(),
            entity.getModelName(), entity.getMacAddress().value(), entity.getFirmwareVersion().value(),
            entity.getInstallationDate(), entity.getRoomLocation()
    );
  }
}
