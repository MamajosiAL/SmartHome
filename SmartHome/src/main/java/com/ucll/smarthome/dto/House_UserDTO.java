package com.ucll.smarthome.dto;

public class House_UserDTO {

    private long houseid;
    private long userid;
    private boolean isadmin;

    public House_UserDTO() {
    }

    private House_UserDTO(Builder builder) {
        setHouseid(builder.houseid);
        setUserid(builder.userid);
        setIsadmin(builder.isadmin);
    }


    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public long getHouseid() {
        return houseid;
    }

    public void setHouseid(long houseid) {
        this.houseid = houseid;
    }

    public boolean getIsadmin() {
        return isadmin;
    }

    public void setIsadmin(boolean isadmin) {
        this.isadmin = isadmin;
    }


    public static final class Builder {
        private long houseid;
        private long userid;
        private boolean isadmin;

        public Builder() {
        }

        public Builder houseid(long val) {
            houseid = val;
            return this;
        }

        public Builder userid(long val) {
            userid = val;
            return this;
        }

        public Builder isadmin(boolean val) {
            isadmin = val;
            return this;
        }

        public House_UserDTO build() {
            return new House_UserDTO(this);
        }
    }
}
