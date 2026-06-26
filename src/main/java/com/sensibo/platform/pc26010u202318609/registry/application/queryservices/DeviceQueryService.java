package com.sensibo.platform.pc26010u202318609.registry.application.queryservices;

import com.sensibo.platform.pc26010u202318609.registry.domain.model.aggregates.Device;
import com.sensibo.platform.pc26010u202318609.registry.domain.model.queries.GetDeviceById;

import java.util.Optional;

public interface DeviceQueryService {

    Optional<Device> handle(GetDeviceById query);

}
