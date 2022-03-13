package be.ucll.java.mobile.smarthome_mobile.util;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import be.ucll.java.mobile.smarthome_mobile.exception.DataNotFoundException;

public enum SensorType {
        MOTION("Motionsensor","Bewegingssensor", ""),TEMPERATURE("Temperaturesensor","Temperatuursensor", "°C")
        ,DAYLIGHT("Lightsensor","Lichtsensor", ""),PRESSURE("Pressuresensor","Druksensor", "psi")
        ,INFRARED("Infraredsensor","Infraroodsensor", ""),MIC("Microphone","Microfoon", "")
        ,GENERIC("Sensor","Sensor", "") ,WEIGHT("Loadsensor","Gewichtsenseor","kg")
        ,MOISTURE("Humiditysensor","Vochtigheidsensor", "%"), TOUCH("Touchsensor","Aanraaksensor","");

    private String name, nameBE, unit;

    SensorType(String name, String nameBE, String unit) {
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
