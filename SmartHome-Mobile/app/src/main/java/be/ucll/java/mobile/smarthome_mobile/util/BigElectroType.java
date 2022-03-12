package be.ucll.java.mobile.smarthome_mobile.util;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import be.ucll.java.mobile.smarthome_mobile.exception.DataNotFoundException;

public enum BigElectroType {
    OVEN("Oven","Oven"),COOLING_DEVICE("Cooling Device","Cooling apparaat"),DISHWASHER("Dishwasher","Vaatwasser"),DRYER("Dryer","Droogkast"),COOLING("Cooling Device","Koeling");
    private String name, nameBE;

    BigElectroType(String name, String nameBE) {
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
        Arrays.stream(BigElectroType.values()).forEach(bigElectroType -> names.add(bigElectroType.name));
        return names;
    }

    @NonNull
    public static List<String> getNamesBE(){
        List<String> names = new ArrayList<>();
        Arrays.stream(BigElectroType.values()).forEach(bigElectroType -> names.add(bigElectroType.nameBE));
        return names;
    }

    public static boolean nameEqualToType(BigElectroType expected, String name){
        return (expected.name.equals(name)||expected.nameBE.equals(name));
    }

    public static BigElectroType getTypeFromName(String name){
        BigElectroType result = null;

        for (BigElectroType type: values()) {
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
