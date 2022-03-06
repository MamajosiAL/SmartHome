package com.ucll.smarthome.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ucll.smarthome.persistence.entities.Type;


import java.time.LocalTime;

public class DeviceDTO {

    private long id;
    private String name;
    private boolean status;
    private int categoryid;
    private long roomid;



    public DeviceDTO() {
    }

    private DeviceDTO(Builder builder) {
        setId(builder.id);
        setName(builder.name);
        setStatus(builder.status);
        setCategoryid(builder.categoryid);
        setRoomid(builder.roomid);
    }

    public long getRoomid() {
        return roomid;
    }

    public void setRoomid(long roomid) {
        this.roomid = roomid;
    }

    public int getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }



    public static final class Builder {
        private long id;
        private String name;
        private boolean status;
        private int categoryid;
        private long roomid;

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

        public Builder status(boolean val) {
            status = val;
            return this;
        }


        public Builder categoryid(int val) {
            categoryid = val;
            return this;
        }
        public Builder roomid(long val){
            roomid = val;
            return this;
        }

        public DeviceDTO build() {
            return new DeviceDTO(this);
        }
    }
}
