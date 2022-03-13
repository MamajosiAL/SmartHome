package com.ucll.smarthome.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ucll.smarthome.persistence.entities.Type;

import java.time.LocalTime;

public class BigElectronicDTO extends DeviceDTO {


    private Type type;
    private long programid;
    private double tempature;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime timer;


    public BigElectronicDTO() {
    }

    private BigElectronicDTO(Builder builder) {
        setType(builder.type);
        setProgramid(builder.programid);
        setTempature(builder.tempature);
        setTimer(builder.timer);
        setId(builder.id);
        setName(builder.name);
        setStatus(builder.status);
        setCategoryid(builder.categoryid);
        setRoomid(builder.roomid);
    }


    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public long getProgramid() {
        return programid;
    }

    public void setProgramid(long programid) {
        this.programid = programid;
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
        private long programid;
        private double tempature;
        private LocalTime timer;
        private long id;
        private String name;
        private boolean status;
        private int categoryid;
        private long roomid;

        public Builder() {
        }

        public Builder type(Type val) {
            type = val;
            return this;
        }

        public Builder programid(long val) {
            programid = val;
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

        public BigElectronicDTO build() {
            return new BigElectronicDTO(this);
        }
    }
}