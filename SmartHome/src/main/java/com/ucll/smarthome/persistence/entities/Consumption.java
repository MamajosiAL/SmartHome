package com.ucll.smarthome.persistence.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "consumption", schema = "smarthome")
public class Consumption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consumptionid")
    private Long consumptionId;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "deviceid")
    private Device device;

    @Column(name = "startDatumEnTijd")
    private LocalDateTime startDatumEnTijd;

    @Column(name = "aantalMinuten")
    private int aantalMinuten;

    @Column(name = "unit")
    private String unit;

    @Column(name = "consumptionperhour")
    private double consumptionPerHour;

    public Consumption() {
    }

    private Consumption(Builder builder){
        setConsumptionId(builder.consumtionId);
        setDevice(builder.device);
        setStartDatumEnTijd(builder.startDatumEnTijd);
        setAantalMinuten(builder.aantalMinuten);
        setUnit(builder.unit);
        setConsumptionPerHour(builder.consumptionPerHour);
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
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

    public Long getConsumptionId() {
        return consumptionId;
    }

    public void setConsumptionId(Long consumptionId) {
        this.consumptionId = consumptionId;
    }

    public double getConsumptionPerHour() {
        return consumptionPerHour;
    }

    public void setConsumptionPerHour(double consumptionPerHour) {
        this.consumptionPerHour = consumptionPerHour;
    }

    public static final class Builder{
        private Long consumtionId;
        private Device device;
        private LocalDateTime startDatumEnTijd;
        private int aantalMinuten;
        private String unit;
        private double consumptionPerHour;

        public Builder(){

        }

        public Builder id(Long val) {
            consumtionId = val;
            return this;
        }

        public Builder startDatumEnTijd(LocalDateTime val) {
            startDatumEnTijd = val;
            return this;
        }

        public Builder aantalMinuten(int val) {
            aantalMinuten = val;
            return this;
        }

        public Builder unit(String val) {
            unit = val;
            return this;
        }

        public Builder device(Device val){
            device = val;
            return this;
        }

        public Builder consumptionPerHour(double val){
            consumptionPerHour = val;
            return this;
        }

        public Consumption build() {
            return new Consumption(this);
        }
    }
}
