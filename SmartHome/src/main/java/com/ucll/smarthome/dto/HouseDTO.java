package com.ucll.smarthome.dto;

public class HouseDTO {
    private long id;
    private String name;
    private String username;
    private boolean isowner;
    private boolean isAdmin;

    public HouseDTO() {
    }

    private HouseDTO(Builder builder) {
        setId(builder.id);
        setName(builder.name);
        setUsername(builder.username);
        setIsowner(builder.isowner);
        setAdmin(builder.isAdmin);
    }


    public boolean isIsowner() {
        return isowner;
    }

    public void setIsowner(boolean isowner) {
        this.isowner = isowner;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public static final class Builder {
        private long id;
        private String name;
        private String username;
        private boolean isowner;
        private boolean isAdmin;

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

        public Builder username(String val) {
            username = val;
            return this;
        }

        public Builder isowner(boolean val) {
            isowner = val;
            return this;
        }

        public Builder isAdmin(boolean val) {
            isAdmin = val;
            return this;
        }

        public HouseDTO build() {
            return new HouseDTO(this);
        }
    }
}
