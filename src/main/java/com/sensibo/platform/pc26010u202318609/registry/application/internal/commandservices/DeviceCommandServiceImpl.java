package com.sensibo.platform.pc26010u202318609.registry.application.internal.commandservices;

import com.sensibo.platform.pc26010u202318609.registry.application.commandservices.DeviceCommandService;
import com.sensibo.platform.pc26010u202318609.registry.domain.model.aggregates.Device;
import com.sensibo.platform.pc26010u202318609.registry.domain.model.commands.CreateDeviceCommand;
import com.sensibo.platform.pc26010u202318609.registry.domain.repositories.DeviceRepository;
import com.sensibo.platform.pc26010u202318609.shared.application.result.ApplicationError;
import com.sensibo.platform.pc26010u202318609.shared.application.result.Result;
import org.springframework.stereotype.Service;

@Service
public class DeviceCommandServiceImpl implements DeviceCommandService {

    private final DeviceRepository deviceRepository;

    public DeviceCommandServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public Result<Long, ApplicationError> handle(CreateDeviceCommand command) {
        if (this.deviceRepository.existsBySerialNumberAndMacAddress(command.serialNumber(), command.macAddress())) {
            return Result.failure(
                    ApplicationError.conflict("Device", "No se permite que se registre dos device con el mismo serialNumber y en el mismo macAddress")
            );
        }
        var device = new Device(command);
        try{
            device = deviceRepository.save(device);
        }catch (Exception e){
            return Result.failure(ApplicationError.unexpected("create-device", e.getMessage()));
        }
        return Result.success(device.getId());
    }
}
