package com.ucll.smarthome.dto;

public class HouseDTO {
    private long id;
    private String name;

    public HouseDTO() {
    }

    private HouseDTO(Builder builder) {
        setId(builder.id);
        setName(builder.name);
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

    public static final class Builder {
        private long id;
        private String name;

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

        public HouseDTO build() {
            return new HouseDTO(this);
        }
    }
}
