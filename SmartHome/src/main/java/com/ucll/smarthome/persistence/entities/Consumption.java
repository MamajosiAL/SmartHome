package com.ucll.smarthome.persistence.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "consumption", schema = "smarthome")
public class Consumption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private long id;

    @Column(name = "startDatumEnTijd")
    private LocalDateTime startDatumEnTijd;

    @Column(name = "aantalUur")
    private int aantalUur;

    @Column(name = "unit")
    private String unit;

    @Column(name = "consumption")
    private int consumption;

    public Consumption() {
    }

    private Consumption(Builder builder){
        setId(builder.id);
        setStartDatumEnTijd(builder.startDatumEnTijd);
        setAantalUur(builder.aantalUur);
        setUnit(builder.unit);
        setConsumption(builder.consumption);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getStartDatumEnTijd() {
        return startDatumEnTijd;
    }

    public void setStartDatumEnTijd(LocalDateTime startDatumEnTijd) {
        this.startDatumEnTijd = startDatumEnTijd;
    }

    public int getAantalUur() {
        return aantalUur;
    }

    public void setAantalUur(int aantalUur) {
        this.aantalUur = aantalUur;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getConsumption() {
        return consumption;
    }

    public void setConsumption(int consumption) {
        this.consumption = consumption;
    }

    public static final class Builder{
        private long id;
        private LocalDateTime startDatumEnTijd;
        private int aantalUur;
        private String unit;
        private int consumption;

        public Builder(){

        }

        public Builder id(Long val) {
            id = val;
            return this;
        }

        public Builder startDatumEnTijd(LocalDateTime val) {
            startDatumEnTijd = val;
            return this;
        }

        public Builder aantalUur(int val) {
            aantalUur = val;
            return this;
        }

        public Builder unit(String val) {
            unit = val;
            return this;
        }

        public Builder consumption(int val) {
            consumption = val;
            return this;
        }

        public Consumption build() {
            return new Consumption(this);
        }

    }

}
