package com.ucll.smarthome.dto;

public class House_UserDTO {

    private long houseid;
    private boolean isadmin;

    public House_UserDTO() {
    }

    private House_UserDTO(Builder builder) {
        setHouseid(builder.houseid);
        setIsadmin(builder.isadmin);
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
        private boolean isadmin;

        public Builder() {
        }

        public Builder id(long val) {
            houseid = val;
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
