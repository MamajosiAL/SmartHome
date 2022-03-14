package com.ucll.smarthome.dto;

import com.ucll.smarthome.persistence.entities.enums.SensorType;

public class SensorDTO extends DeviceDTO{

    private String sensorType;
    private double sensordata;

    public SensorDTO() {
    }

    private SensorDTO(Builder builder) {
        setId(builder.id);
        setName(builder.name);
        setStatus(builder.status);
        setCategoryid(builder.categoryid);
        setRoomid(builder.roomid);
        setSensorType(builder.sensorType);
        setSensordata(builder.sensordata);
    }


    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public double getSensordata() {
        return sensordata;
    }

    public void setSensordata(double sensordata) {
        this.sensordata = sensordata;
    }


    public static final class Builder {
        private long id;
        private String name;
        private boolean status;
        private int categoryid;
        private long roomid;
        private String sensorType;
        private double sensordata;

        public Builder() {
        }

        public Builder id(long val) {
            id = val;
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

        public Builder categoryid(int val) {
            categoryid = val;
            return this;
        }

        public Builder roomid(long val) {
            roomid = val;
            return this;
        }

        public Builder sensorType(String val) {
            sensorType = val;
            return this;
        }

        public Builder sensordata(double val) {
            sensordata = val;
            return this;
        }

        public SensorDTO build() {
            return new SensorDTO(this);
        }
    }
}
