package com.sensibo.platform.pc26010u202318609.registry.domain.model.valueobjects;

import java.util.Arrays;

public enum DeviceTypes {
    SMART_AC_CONTROLLER(1),
    ROOM_SENSOR(2),
    AIR_QUALITY_MONITOR(3),
    DOOR_WINDOW_SENSOR(4);

    private final int id;

    DeviceTypes(int id) { this.id = id; }

    public int getId() { return id; }

    public static DeviceTypes fromValue(int id) {
        return Arrays.stream(DeviceTypes.values())
                .filter(dt -> dt.id == id)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("[DeviceTypes] Invalid value for DeviceTypes: " + id));
    }
    public static DeviceTypes fromString(String name) {
        return  Arrays.stream(DeviceTypes.values())
                .filter(dt -> dt.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("[DeviceTypes] Invalid string for DeviceTypes: " + name));
    }
}