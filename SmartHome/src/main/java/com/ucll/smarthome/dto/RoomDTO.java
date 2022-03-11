package com.ucll.smarthome.dto;

import com.ucll.smarthome.persistence.entities.House;

public class RoomDTO {
    private long id;
    private String name;
    private long houseid;


    public RoomDTO() {
    }

    private RoomDTO(Builder builder) {
        setId(builder.id);
        setName(builder.name);
        setHouseid(builder.houseid);

    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getHouseid() {
        return houseid;
    }

    public void setHouseid(long houseid) {
        this.houseid = houseid;
    }



    public static final class Builder {
        private long id;
        private String name;
        private long houseid;

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

        public Builder houseid(long val) {
            houseid = val;
            return this;
        }



        public RoomDTO build() {
            return new RoomDTO(this);
        }


    }

}
