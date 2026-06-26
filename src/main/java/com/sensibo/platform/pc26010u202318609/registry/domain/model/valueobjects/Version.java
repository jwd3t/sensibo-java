package com.sensibo.platform.pc26010u202318609.registry.domain.model.valueobjects;

import java.util.Objects;

public record Version(String value) {

    private static final String VERSION_REGEX = "^\\d+\\.\\d+\\.\\d+$";

    public Version {
        if (Objects.isNull(value) || value.isBlank()) {
            throw new IllegalArgumentException("[Version] value cannot be null or blank");
        }
        if (!value.matches(VERSION_REGEX)) {
            throw new IllegalArgumentException("[Version] Invalid version format. Expected X.Y.Z");
        }
    }

    public Version() { this(null); }
}
