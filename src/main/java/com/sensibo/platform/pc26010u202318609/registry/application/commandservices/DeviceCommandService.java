package com.sensibo.platform.pc26010u202318609.registry.application.commandservices;

import com.sensibo.platform.pc26010u202318609.registry.domain.model.commands.CreateDeviceCommand;
import com.sensibo.platform.pc26010u202318609.shared.application.result.ApplicationError;
import com.sensibo.platform.pc26010u202318609.shared.application.result.Result;

public interface DeviceCommandService {

    Result<Long, ApplicationError> handle(CreateDeviceCommand command);

}
