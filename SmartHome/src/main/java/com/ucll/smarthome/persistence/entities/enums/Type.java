package com.ucll.smarthome.persistence.entities.enums;

public enum Type {
    OVEN("oven"),WASHING_MACHINE("washing machine"),DISH_WASHER("dish washer");

    private String title;

    private Type(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }
}
