package com.ucll.smarthome.dto;

public class MediaDTO extends DeviceDTO {
    private int volume;

    public MediaDTO() {
    }

    private MediaDTO(Builder builder) {
        setVolume(builder.volume);
        setId(builder.id);
        setName(builder.name);
        setStatus(builder.status);
        setCategoryid(builder.categoryid);
        setRoomid(builder.roomid);
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public static final class Builder {
        private int volume;
        private long id;
        private String name;
        private boolean status;
        private int categoryid;
        private long roomid;

        public Builder() {
        }

        public Builder volume(int val) {
            volume = val;
            return this;
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

        public MediaDTO build() {
            return new MediaDTO(this);
        }
    }
}
