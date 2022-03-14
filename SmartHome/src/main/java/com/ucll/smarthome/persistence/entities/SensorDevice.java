package com.ucll.smarthome.persistence.entities;

import com.ucll.smarthome.persistence.entities.enums.SensorType;

import javax.persistence.*;

@Entity
@DiscriminatorValue("3")
public class SensorDevice extends Device {


    @Column(name = "sensortype")
    private SensorType sensorType;

    @Column(name = "sensordata")
    private double sensordata;

    public SensorDevice() {
    }

    private SensorDevice(Builder builder) {
        setId(builder.id);
        setCategoryid(builder.categoryid);
        setName(builder.name);
        setStatus(builder.status);
        setRoom(builder.room);
        setSensorType(builder.sensorType);
        setSensordata(builder.sensordata);
    }


    public SensorType getSensorType() {
        return sensorType;
    }

    public void setSensorType(SensorType sensorType) {
        this.sensorType = sensorType;
    }

    public double getSensordata() {
        return sensordata;
    }

    public void setSensordata(double sensordata) {
        this.sensordata = sensordata;
    }


    public static final class Builder {
        private Long id;
        private int categoryid;
        private String name;
        private boolean status;
        private Room room;
        private SensorType sensorType;
        private double sensordata;

        public Builder() {
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

        public Builder sensorType(SensorType val) {
            sensorType = val;
            return this;
        }

        public Builder sensordata(double val) {
            sensordata = val;
            return this;
        }

        public SensorDevice build() {
            return new SensorDevice(this);
        }
    }
}
