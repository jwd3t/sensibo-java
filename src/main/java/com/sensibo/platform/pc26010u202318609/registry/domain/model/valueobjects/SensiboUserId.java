package com.sensibo.platform.pc26010u202318609.registry.domain.model.valueobjects;

import java.util.Objects;

public record SensiboUserId(Long userId) {

    public SensiboUserId {
        if (Objects.isNull(userId)) {
            throw new IllegalArgumentException("[SensiboUserId] value cannot be null");
        }
        if (userId <= 0) {
            throw new IllegalArgumentException("[SensiboUserId] value must be greater than zero");
        }
    }

    public SensiboUserId() { this(null); }
}
