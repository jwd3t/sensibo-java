package com.sensibo.platform.pc26010u202318609.registry.application.internal.queryservices;

import com.sensibo.platform.pc26010u202318609.registry.application.queryservices.DeviceQueryService;
import com.sensibo.platform.pc26010u202318609.registry.domain.model.aggregates.Device;
import com.sensibo.platform.pc26010u202318609.registry.domain.model.queries.GetDeviceById;
import com.sensibo.platform.pc26010u202318609.registry.domain.repositories.DeviceRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeviceQueryServiceImpl  implements DeviceQueryService {

    private final DeviceRepository deviceRepository;
    public DeviceQueryServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public Optional<Device> handle(GetDeviceById query) {
        return this.deviceRepository.findById(query.deviceId());
    }
}
