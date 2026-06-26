package com.sensibo.platform.pc26010u202318609.registry.domain.model.valueobjects;

import java.util.Objects;
import java.util.UUID;

public record SerialNumber(String value) {

    public SerialNumber {
        if (Objects.isNull(value) || value.isBlank()) {
            throw new IllegalArgumentException("[SerialNumber] value cannot be null or blank");
        }
        if (value.length() != 36) {
            throw new IllegalArgumentException("[SerialNumber] must be 36 characters long");
        }
        if (!value.matches("[a-f0-9]{8}-([a-f0-9]{4}-){3}[a-f0-9]{12}")) {
            throw new IllegalArgumentException("[SerialNumber] must be a valid UUID");
        }
    }

    public SerialNumber() {
        this(UUID.randomUUID().toString());
    }

}
