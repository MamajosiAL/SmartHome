package com.ucll.smarthome.dto;

public class UserDTO {
    private long id;
    private String username;
    private String name;
    private String firstname;
    private String email;
    private String password;
    private boolean isadmin;


    public UserDTO() {
    }

    private UserDTO(Builder builder) {
        setId(builder.id);
        setUsername(builder.username);
        setName(builder.name);
        setFirstname(builder.firstname);
        setEmail(builder.email);
        setPassword(builder.password);
        setIsadmin(builder.isadmin);
    }


    public boolean isIsadmin() {
        return isadmin;
    }

    public void setIsadmin(boolean isadmin) {
        this.isadmin = isadmin;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public static final class Builder {
        private long id;
        private String username;
        private String name;
        private String firstname;
        private String email;
        private String password;
        private boolean isadmin;

        public Builder() {
        }

        public Builder id(long val) {
            id = val;
            return this;
        }

        public Builder username(String val) {
            username = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder firstname(String val) {
            firstname = val;
            return this;
        }

        public Builder email(String val) {
            email = val;
            return this;
        }

        public Builder password(String val) {
            password = val;
            return this;
        }

        public Builder isadmin(boolean val) {
            isadmin = val;
            return this;
        }

        public UserDTO build() {
            return new UserDTO(this);
        }
    }
}
