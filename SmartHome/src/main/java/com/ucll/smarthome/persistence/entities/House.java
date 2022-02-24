package com.ucll.smarthome.persistence.entities;

import javax.persistence.*;

@Entity
@Table(name = "house" ,schema = "smarthome")
public class House {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "houseid",nullable = false,updatable = false)
    private Long houseId;
    @Column(name = "name")
    private String name;


    public House() {
    }

    private House(Builder builder) {
        setHouseId(builder.houseId);
        setName(builder.name);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getHouseId() {
        return houseId;
    }

    public void setHouseId(Long houseId) {
        this.houseId = houseId;
    }

    public static final class Builder {
        private Long houseId;
        private String name;

        public Builder() {
        }

        public Builder houseId(Long val) {
            houseId = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public House build() {
            return new House(this);
        }
    }
}
