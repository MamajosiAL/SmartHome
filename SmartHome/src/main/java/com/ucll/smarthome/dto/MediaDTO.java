package com.ucll.smarthome.dto;

public class MediaDTO extends DeviceDTO {
    private int volume;
    private int zender;

    public MediaDTO() {
    }

    private MediaDTO(Builder builder) {
        setId(builder.id);
        setName(builder.name);
        setStatus(builder.status);
        setCategoryid(builder.categoryid);
        setRoomid(builder.roomid);
        setVolume(builder.volume);
        setZender(builder.zender);
    }


    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getZender() {
        return zender;
    }

    public void setZender(int zender) {
        this.zender = zender;
    }

    public static final class Builder {
        private long id;
        private String name;
        private boolean status;
        private int categoryid;
        private long roomid;
        private int volume;
        private int zender;

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

        public Builder roomid(long val) {
            roomid = val;
            return this;
        }

        public Builder volume(int val) {
            volume = val;
            return this;
        }

        public Builder zender(int val) {
            zender = val;
            return this;
        }

        public MediaDTO build() {
            return new MediaDTO(this);
        }
    }
}
