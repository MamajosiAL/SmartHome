package be.ucll.java.mobile.smarthome_mobile.util;

import java.util.List;

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

    //TODO
//    public List<String> getNames(){
//        for (DeviceCategory dc :this.) {
//
//        }
//        return new Strea
//    }
}
