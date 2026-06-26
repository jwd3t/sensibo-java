package com.sensibo.platform.pc26010u202318609.registry.domain.model.queries;

public record GetDeviceById(Long deviceId) {
    public GetDeviceById {
        if (deviceId == null) {
            throw new IllegalArgumentException("[GetDeviceById] deviceId cannot be null");
        }
        if (deviceId <= 0) {
            throw new IllegalArgumentException("[GetDeviceById] deviceId must be greater than zero");
        }
    }
}
