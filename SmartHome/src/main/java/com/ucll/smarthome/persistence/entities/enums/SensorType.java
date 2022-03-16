package com.ucll.smarthome.persistence.entities.enums;

public enum SensorType {

    THERMOSTAT("Thermostat"), MOTION_SENSOR("Motion Sensor"), WATER_LEAK_FREEZE_SENSOR("Water Leak/Freeze Sensor"),
    WINDOW_DOOR_SENSOR("Window/Door Sensor"), SMART_SMOKE_SENSOR("Smart Smoke Sensor");

    private String title;

    private SensorType(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }
}

