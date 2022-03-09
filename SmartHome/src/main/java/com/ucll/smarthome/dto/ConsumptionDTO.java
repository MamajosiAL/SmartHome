package com.ucll.smarthome.dto;

import java.time.LocalDateTime;

public class ConsumptionDTO {
    private Long consumptionId;
    private Long deviceId;
    private LocalDateTime startDatumEnTijd;
    private int aantalMinuten;
    private String unit;
    private double consumptionPerHour;

    public ConsumptionDTO() {
    }

    public ConsumptionDTO(Builder builder) {
        setConsumptionId(builder.consumptionId);
        setDeviceId(builder.deviceId);
        setStartDatumEnTijd(builder.startDatumEnTijd);
        setAantalMinuten(builder.aantalMinuten);
        setUnit(builder.unit);
        setConsumptionPerHour(builder.consumptionPerHour);
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

    public LocalDateTime getStartDatumEnTijd() {
        return startDatumEnTijd;
    }

    public void setStartDatumEnTijd(LocalDateTime startDatumEnTijd) {
        this.startDatumEnTijd = startDatumEnTijd;
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

    public static final class Builder {
        private Long consumptionId;
        private Long deviceId;
        private LocalDateTime startDatumEnTijd;
        private int aantalMinuten;
        private String unit;
        private double consumptionPerHour;

        public Builder(){}

        public Builder consumptionId(Long val){
            consumptionId = val;
            return this;
        }

        public Builder device(Long val){
            deviceId = val;
            return this;
        }

        public Builder startDatumEnTijd(LocalDateTime val){
            startDatumEnTijd = val;
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

        private Builder consumptionPerHour(double val){
            consumptionPerHour = val;
            return this;
        }

        public ConsumptionDTO build() { return new ConsumptionDTO(this); }

    }

    @Override
    public String toString() {
        return "ConsumptionDTO{" +
                "consumptionId=" + consumptionId +
                ", deviceId=" + deviceId +
                ", startDatumEnTijd=" + startDatumEnTijd +
                ", aantalMinuten=" + aantalMinuten +
                ", unit='" + unit + '\'' +
                ", consumptionPerHour=" + consumptionPerHour +
                '}';
    }
}
