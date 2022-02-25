package com.ucll.smarthome.dto;

public class House_UserDTO {

    private long id;
    private boolean isadmin;

    public House_UserDTO() {
    }

    private House_UserDTO(Builder builder) {
        setId(builder.id);
        setIsadmin(builder.isadmin);
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean getIsadmin() {
        return isadmin;
    }

    public void setIsadmin(boolean isadmin) {
        this.isadmin = isadmin;
    }


    public static final class Builder {
        private long id;
        private boolean isadmin;

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

        public House_UserDTO build() {
            return new House_UserDTO(this);
        }
    }
}
