package be.ucll.java.mobile.smarthome_mobile.util;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import be.ucll.java.mobile.smarthome_mobile.exception.DataNotFoundException;

public enum SensorType {
    THERMOSTAT("Thermostat"), MOTION_SENSOR("Motion Sensor"), WATER_LEAK_FREEZE_SENSOR("Water Leak/Freeze Sensor"),
    WINDOW_DOOR_SENSOR("Window/Door Sensor"), SMART_SMOKE_SENSOR("Smart Smoke Sensor");

    private String name, nameBE, unit;

    SensorType(String name) {
        this.name = name;
        this.nameBE = nameBE;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameBE() {
        return nameBE;
    }

    public void setNameBE(String nameBE) {
        this.nameBE = nameBE;
    }

    @NonNull
    public static List<String> getNames(){
        List<String> names = new ArrayList<>();
        Arrays.stream(SensorType.values()).forEach(bigElectroType -> names.add(bigElectroType.name));
        return names;
    }

    @NonNull
    public static List<String> getNamesBE(){
        List<String> names = new ArrayList<>();
        Arrays.stream(SensorType.values()).forEach(bigElectroType -> names.add(bigElectroType.nameBE));
        return names;
    }

    public static boolean nameEqualToType(SensorType expected, String name){
        return (expected.name.equals(name)||expected.nameBE.equals(name));
    }

    public static SensorType getTypeFromName(String name){
        SensorType result = null;

        for (SensorType type: values()) {
            if(nameEqualToType(type,name)){
                result = type;
                break;
            }
        }
        if(result == null){
            throw new DataNotFoundException("No devicecategory found with name: "+name);
        }
        return result;
    }
}
