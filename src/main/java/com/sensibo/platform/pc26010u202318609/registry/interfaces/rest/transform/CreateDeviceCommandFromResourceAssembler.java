package com.sensibo.platform.pc26010u202318609.registry.interfaces.rest.transform;

import com.sensibo.platform.pc26010u202318609.registry.domain.model.commands.CreateDeviceCommand;
import com.sensibo.platform.pc26010u202318609.registry.domain.model.valueobjects.SerialNumber;
import com.sensibo.platform.pc26010u202318609.registry.interfaces.rest.resources.CreateDeviceResource;

public class CreateDeviceCommandFromResourceAssembler {

  public static CreateDeviceCommand toCommandFromResource(CreateDeviceResource resource) {
    return new CreateDeviceCommand(
        resource.userId(), resource.deviceType(), resource.modelName(), new SerialNumber().value(), resource.macAddress(),
        resource.firmwareVersion(), resource.installationDate(), resource.roomLocation()
    );
  }
}
