package com.sensibo.platform.pc26010u202318609.registry.infrastructure.persistence.jpa.entities;

import com.sensibo.platform.pc26010u202318609.registry.domain.model.valueobjects.DeviceTypes;
import com.sensibo.platform.pc26010u202318609.registry.domain.model.valueobjects.MacAddress;
import com.sensibo.platform.pc26010u202318609.registry.domain.model.valueobjects.SensiboUserId;
import com.sensibo.platform.pc26010u202318609.registry.domain.model.valueobjects.SerialNumber;
import com.sensibo.platform.pc26010u202318609.registry.domain.model.valueobjects.Version;
import com.sensibo.platform.pc26010u202318609.registry.infrastructure.persistence.jpa.converters.MacAddressPersistenceConverter;
import com.sensibo.platform.pc26010u202318609.registry.infrastructure.persistence.jpa.converters.SensiboUserIdPersistenceConverter;
import com.sensibo.platform.pc26010u202318609.registry.infrastructure.persistence.jpa.converters.SerialNumberPersistenceConverter;
import com.sensibo.platform.pc26010u202318609.registry.infrastructure.persistence.jpa.converters.VersionPersistenceConverter;
import com.sensibo.platform.pc26010u202318609.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "devices")
@Getter
@Setter
public class DevicePersistenceEntity extends AuditableAbstractPersistenceEntity {
    @Convert(converter = SensiboUserIdPersistenceConverter.class)
    @Column(name = "user_id", nullable = false)
    private SensiboUserId userId;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "device_type", nullable = false)
    private DeviceTypes deviceType;

    @Column(name = "model_name", length = 50, nullable = false)
    private String modelName;

    @Convert(converter = SerialNumberPersistenceConverter.class)
    @Column(name = "serial_number", nullable = false)
    private SerialNumber serialNumber;

    @Convert(converter = MacAddressPersistenceConverter.class)
    @Column(name = "mac_address", nullable = false)
    private MacAddress macAddress;

    @Convert(converter = VersionPersistenceConverter.class)
    @Column(name = "firmware_version", nullable = false)
    private Version firmwareVersion;

    @Column(name = "installation_date", nullable = false)
    private LocalDate installationDate;

    @Column(name = "room_location", nullable = false)
    private String roomLocation;
}
