package com.ucll.smarthome.persistence.entities;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("2")
public class MediaDevice extends Device {
    @Column(name = "volume")
    private int volume;

    public MediaDevice() {
    }

    private MediaDevice(Builder builder) {
        setVolume(builder.volume);
        setId(builder.id);
        setCategoryid(builder.categoryid);
        setName(builder.name);
        setStatus(builder.status);
        setRoom(builder.room);
    }


    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }


    public static final class Builder {
        private int volume;
        private Long id;
        private int categoryid;
        private String name;
        private boolean status;
        private Room room;

        public Builder() {
        }

        public Builder volume(int val) {
            volume = val;
            return this;
        }

        public Builder id(Long val) {
            id = val;
            return this;
        }

        public Builder categoryid(int val) {
            categoryid = val;
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

        public Builder room(Room val) {
            room = val;
            return this;
        }

        public MediaDevice build() {
            return new MediaDevice(this);
        }
    }
}
