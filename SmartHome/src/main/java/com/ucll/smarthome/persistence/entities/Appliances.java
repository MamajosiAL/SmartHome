package com.ucll.smarthome.persistence.entities;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@DiscriminatorValue("1")
public class Appliances extends Device {


    @ManyToOne
    @JoinColumn(name= "type_id")
    private Type type;
    @Column(name = "tempature")
    private double tempature;
    @Column(name = "timer")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime timer;

    public Appliances() {
    }

    private Appliances(Builder builder) {
        setType(builder.type);
        setTempature(builder.tempature);
        setTimer(builder.timer);
        setId(builder.id);
        setCategoryid(builder.categoryid);
        setName(builder.name);
        setStatus(builder.status);
        setRoom(builder.room);
    }


    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public double getTempature() {
        return tempature;
    }

    public void setTempature(double tempature) {
        this.tempature = tempature;
    }



    public LocalTime getTimer() {
        return timer;
    }

    public void setTimer(LocalTime timer) {
        this.timer = timer;
    }


    public static final class Builder {
        private Type type;
        private double tempature;
        private LocalTime timer;
        private Long id;
        private int categoryid;
        private String name;
        private boolean status;
        private Room room;

        public Builder() {
        }

        public Builder type(Type val) {
            type = val;
            return this;
        }

        public Builder tempature(double val) {
            tempature = val;
            return this;
        }

        public Builder timer(LocalTime val) {
            timer = val;
            return this;
        }

        public Builder id(Long val) {
            id = val;
            return this;
        }

        public Builder categoryid(int val) {
            categoryid = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder status(boolean val) {
            status = val;
            return this;
        }

        public Builder room(Room val) {
            room = val;
            return this;
        }

        public Appliances build() {
            return new Appliances(this);
        }
    }
}
