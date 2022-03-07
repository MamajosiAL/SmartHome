package com.ucll.smarthome.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;

public class ProgrammeDTO {

    private long id;
    private Double temp;
    private String name;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime timer;
    private long typeId;

    public ProgrammeDTO() {
    }

    private ProgrammeDTO(Builder builder) {
        setId(builder.id);
        setTemp(builder.temp);
        setName(builder.name);
        setTimer(builder.timer);
        setTypeId(builder.typeId);
    }


    public LocalTime getTimer() {
        return timer;
    }

    public void setTimer(LocalTime timer) {
        this.timer = timer;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTypeId() {
        return typeId;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }


    public static final class Builder {
        private long id;
        private Double temp;
        private String name;
        private LocalTime timer;
        private long typeId;

        public Builder() {
        }

        public Builder id(long val) {
            id = val;
            return this;
        }

        public Builder temp(Double val) {
            temp = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder timer(LocalTime val) {
            timer = val;
            return this;
        }

        public Builder typeId(long val) {
            typeId = val;
            return this;
        }

        public ProgrammeDTO build() {
            return new ProgrammeDTO(this);
        }
    }
}
