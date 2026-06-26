package com.sensibo.platform.pc26010u202318609.registry.domain.model.aggregates;

import com.sensibo.platform.pc26010u202318609.registry.domain.model.commands.CreateDeviceCommand;
import com.sensibo.platform.pc26010u202318609.registry.domain.model.valueobjects.*;
import com.sensibo.platform.pc26010u202318609.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Device extends AbstractDomainAggregateRoot {

    private Long id;

    private SensiboUserId userId;

    private DeviceTypes deviceType;

    private String modelName;

    private SerialNumber serialNumber;

    private MacAddress macAddress;

    private Version firmwareVersion;

    private LocalDate installationDate;

    private String roomLocation;




    //se crea al hacer el CommandServiceImpl

    public Device() { }
    public Device(CreateDeviceCommand command) {
        this.userId = new SensiboUserId(command.userId());
        this.deviceType = DeviceTypes.valueOf(command.deviceType());
        this.modelName = command.modelName();
        this.serialNumber = new SerialNumber(command.serialNumber());
        this.macAddress = new MacAddress(command.macAddress());
        this.firmwareVersion = new Version(command.firmwareVersion());
        this.installationDate = command.installationDate();
        this.roomLocation = command.roomLocation();
    }

}
