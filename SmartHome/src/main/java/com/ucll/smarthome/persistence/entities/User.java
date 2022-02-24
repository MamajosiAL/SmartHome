package com.ucll.smarthome.persistence.entities;

import javax.persistence.*;

@Entity
@Table(name = "user",schema = "smarthome")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userid",updatable = false,nullable = false)
    private Long userId;
    @Column(name = "username")
    private String username;
    @Column(name = "name")
    private String name;
    @Column(name = "firstname")
    private String firstname;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;

    public User() {
    }

    private User(Builder builder) {
        setId(builder.userId);
        setUsername(builder.username);
        setName(builder.name);
        setFirstname(builder.firstname);
        setEmail(builder.email);
        setPassword(builder.password);
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

    public void setId(Long id) {
        this.userId = id;
    }


    public Long getId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static final class Builder {
        private Long userId;
        private String username;
        private String name;
        private String firstname;
        private String email;
        private String password;

        public Builder() {
        }

        public Builder userId(Long val) {
            userId = val;
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
        public Builder password(String val){
            password = val;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
