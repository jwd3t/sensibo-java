package com.sensibo.platform.pc26010u202318609.registry.domain.model.valueobjects;

import java.util.Objects;

public record MacAddress(String value) {

    private static final String MAC_REGEX = "^([0-9A-Fa-f]{2}:){5}[0-9A-Fa-f]{2}$";

    public MacAddress {
        if (Objects.isNull(value) || value.isBlank()) {
            throw new IllegalArgumentException("[MacAddress] value cannot be null or blank");
        }
        if (!value.matches(MAC_REGEX)) {
            throw new IllegalArgumentException("[MacAddress] Invalid MAC address format. Expected AA:BB:CC:DD:EE:FF");
        }
    }

    public MacAddress() { this(null); }
}
