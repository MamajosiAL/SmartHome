package com.ucll.smarthome.persistence.entities;

import liquibase.pro.packaged.B;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
@Table(name = "consumptionlog", schema = "smarthome")
public class ConsumptionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "consumptionid")
    private Consumption consumption;

    @Column(name = "minutesPerDay")
    private int minutesPerDay;

    @Column(name = "date")
    private LocalDate date;

    public ConsumptionLog(){

    }

    public ConsumptionLog(Builder builder){
        setId(builder.id);
        setConsumption(builder.consumption);
        setMinutesPerDay(builder.minutesPerDay);
        setDate(builder.date);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Consumption getConsumption() {
        return consumption;
    }

    public void setConsumption(Consumption consumption) {
        this.consumption = consumption;
    }

    public int getMinutesPerDay() {
        return minutesPerDay;
    }

    public void setMinutesPerDay(int minutesPerDay) {
        this.minutesPerDay = minutesPerDay;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public static final class Builder{
        private Long id;
        private Consumption consumption;
        private int minutesPerDay;
        private LocalDate date;

        public Builder(){

        }

        public Builder id(Long val){
            id =val;
            return this;
        }

        public Builder consumption(Consumption val){
            consumption = val;
            return this;
        }

        public Builder minutesPerDay(int val){
            minutesPerDay = val;
            return this;
        }

        public Builder date(LocalDate val){
            date = val;
            return this;
        }

        public ConsumptionLog build() {return new ConsumptionLog(this);}
    }
}
