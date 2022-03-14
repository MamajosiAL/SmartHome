package com.ucll.smarthome.dto;

import java.time.LocalDateTime;

public class ConsumptionDTO {
    private Long consumptionId;
    private Long deviceId;
    private int aantalMinuten;
    private String unit;
    private double consumptionPerHour;
    private Long houseId;
    private Long roomId;

    public ConsumptionDTO() {
    }

    public ConsumptionDTO(Builder builder) {
        setConsumptionId(builder.consumptionId);
        setDeviceId(builder.deviceId);
        setAantalMinuten(builder.aantalMinuten);
        setUnit(builder.unit);
        setConsumptionPerHour(builder.consumptionPerHour);
        setHouseId(builder.houseId);
        setRoomId(builder.roomId);
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

    public static final class Builder {
        private Long consumptionId;
        private Long deviceId;
        private int aantalMinuten;
        private String unit;
        private double consumptionPerHour;
        private Long houseId;
        private Long roomId;

        public Builder(){}

        public Builder consumptionId(Long val){
            consumptionId = val;
            return this;
        }

        public Builder device(Long val){
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

        public ConsumptionDTO build() { return new ConsumptionDTO(this); }
    }
}
