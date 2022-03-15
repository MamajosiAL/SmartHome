package com.ucll.smarthome.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.Date;

public class ConsumptionLogDTO {
    private Long consumptionLogId;
    private Long consumptionId;
    private Long deviceId;
    private int aantalMinuten;
    private String unit;
    private double consumptionPerHour;
    private Long houseId;
    private Long roomId;
    @JsonFormat(pattern = "dd/MM/YYYY")
    private LocalDate date;
    private int totalConsumption;
    private String roomName;
    private String houseName;

    public ConsumptionLogDTO() {
    }

    public ConsumptionLogDTO(Builder builder) {
        setConsumptionLogId(builder.consumptionLogId);
        setConsumptionId(builder.consumptionId);
        setDeviceId(builder.deviceId);
        setAantalMinuten(builder.aantalMinuten);
        setUnit(builder.unit);
        setConsumptionPerHour(builder.consumptionPerHour);
        setHouseId(builder.houseId);
        setRoomId(builder.roomId);
        setDate(builder.date);
        setTotalConsumption(builder.totalConsumption);
        setHouseName(builder.houseName);
        setRoomName(builder.roomName);
    }

    public Long getConsumptionId() {
        return consumptionId;
    }

    public void setConsumptionId(Long consumptionId) {
        this.consumptionId = consumptionId;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public int getAantalMinuten() {
        return aantalMinuten;
    }

    public void setAantalMinuten(int aantalMinuten) {
        this.aantalMinuten = aantalMinuten;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public double getConsumptionPerHour() {
        return consumptionPerHour;
    }

    public void setConsumptionPerHour(double consumptionPerHour) {
        this.consumptionPerHour = consumptionPerHour;
    }

    public Long getHouseId() {
        return houseId;
    }

    public void setHouseId(Long houseId) {
        this.houseId = houseId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getConsumptionLogId() {
        return consumptionLogId;
    }

    public void setConsumptionLogId(Long consumptionLogId) {
        this.consumptionLogId = consumptionLogId;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getTotalConsumption() {
        return totalConsumption;
    }

    public void setTotalConsumption(int totalConsumption) {
        this.totalConsumption = totalConsumption;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getHouseName() {
        return houseName;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }

    public static final class Builder {
        private Long consumptionLogId;
        private Long consumptionId;
        private Long deviceId;
        private int aantalMinuten;
        private String unit;
        private double consumptionPerHour;
        private Long houseId;
        private Long roomId;
        private LocalDate date;
        private int totalConsumption;
        private String roomName;
        private String houseName;

        public Builder(){}

        public Builder consumptionLogId(Long val){
            consumptionLogId = val;
            return this;
        }

        public Builder consumptionId(Long val){
            consumptionId = val;
            return this;
        }

        public Builder deviceId(Long val){
            deviceId = val;
            return this;
        }

        public Builder aantalMinuten(int val){
            aantalMinuten = val;
            return this;
        }

        public Builder unit(String val){
            unit = val;
            return this;
        }

        public Builder consumptionPerHour(double val){
            consumptionPerHour = val;
            return this;
        }

        public Builder houseId(Long val){
            houseId = val;
            return this;
        }

        public Builder roomId(Long val){
            roomId = val;
            return this;
        }

        public Builder date(LocalDate val){
            date = val;
            return this;
        }

        public Builder totalConsumption(int val){
            totalConsumption = val;
            return this;
        }

        public Builder roomName (String val){
            roomName = val;
            return this;
        }

        public Builder houseName (String val){
            houseName = val;
            return this;
        }

        public ConsumptionLogDTO build() { return new ConsumptionLogDTO(this); }
    }
}
