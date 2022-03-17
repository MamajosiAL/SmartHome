package be.ucll.java.mobile.smarthome_mobile.util;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import be.ucll.java.mobile.smarthome_mobile.exception.DataNotFoundException;

public enum DeviceCategory {
    BIG_ELECTRO("Big electronic","Groot elektro"),MEDIA("Mediaplayer","Mediaspeler"),SENSOR("Sensor","Sensor"),GENERIC("Generic","Generiek");
    private String name, nameBE;

    DeviceCategory(String name, String nameBE) {
        this.name = name;
        this.nameBE = nameBE;
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
        Arrays.stream(DeviceCategory.values()).forEach(deviceCategory -> names.add(deviceCategory.name));
        return names;
    }

    @NonNull
    public static List<String> getNamesBE(){
        List<String> names = new ArrayList<>();
        Arrays.stream(DeviceCategory.values()).forEach(deviceCategory -> names.add(deviceCategory.nameBE));
        return names;
    }

    public static boolean nameEqualToCategory(DeviceCategory expected, String name){
        return (expected.name.equals(name)||expected.nameBE.equals(name));
    }

    public static DeviceCategory getCategoryFromName(String name){
        DeviceCategory result = null;

        for (DeviceCategory category: values()) {
            if(nameEqualToCategory(category,name)){
                result = category;
                break;
            }
        }
        if(result == null){
            throw new DataNotFoundException("No devicecategory found with name: "+name);
        }
        return result;
    }

    public static boolean matchedAny(@NonNull String name){
        return((DeviceCategory.getNames().contains(name)||DeviceCategory.getNamesBE().contains(name))&&!name.isEmpty());
    }
}
