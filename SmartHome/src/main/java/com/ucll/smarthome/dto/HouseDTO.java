package com.ucll.smarthome.dto;

public class HouseDTO {
    private long id;
    private String name;
    private long userid;

    public HouseDTO() {
    }

    private HouseDTO(Builder builder) {
        setId(builder.id);
        setName(builder.name);
        setUserid(builder.userid);
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

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public static final class Builder {
        private long id;
        private String name;
        private long userid;

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

        public Builder userid(long val){
            userid = val;
            return this;
        }

        public HouseDTO build() {
            return new HouseDTO(this);
        }
    }
}
