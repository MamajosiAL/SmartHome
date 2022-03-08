package com.ucll.smarthome.dto;

public class House_UserDTO {

    private long id;
    private long houseid;
    private long userid;
    private boolean isadmin;

    public House_UserDTO() {
    }

    private House_UserDTO(Builder builder) {
        setId(builder.id);
        setIsadmin(builder.isadmin);
        setUserid(builder.userid);
        setHouseid(builder.houseid);
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setIsadmin(boolean isadmin) {
        this.isadmin = isadmin;
    }

    public long getHouseid() {
        return houseid;
    }

    public void setHouseid(long houseid) {
        this.houseid = houseid;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public boolean isIsadmin() {
        return isadmin;
    }

    public static final class Builder {
        private long id;
        private boolean isadmin;
        private long userid;
        private long houseid;

        public Builder() {
        }

        public Builder id(long val) {
            id = val;
            return this;
        }

        public Builder isadmin(boolean val) {
            isadmin = val;
            return this;
        }

        public Builder userid(long val){
            userid = val;
            return this;
        }

        public Builder houseid(long val){
            houseid = val;
            return this;
        }

        public House_UserDTO build() {
            return new House_UserDTO(this);
        }
    }
}
