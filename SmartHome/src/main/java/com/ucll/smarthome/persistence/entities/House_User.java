package com.ucll.smarthome.persistence.entities;

import javax.persistence.*;
@Entity
@Table(name = "house_user",schema = "smarthome")
public class House_User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "userid")
    private User user;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "houseid")
    private House house;

    @Column(name = "isadmin")
    private boolean isAdmin;

    @Column(name = "isowner")
    private boolean isOwner;

    public House_User() {
    }

    private House_User(Builder builder) {
        setId(builder.id);
        setUser(builder.user);
        setHouse(builder.house);
        setAdmin(builder.isAdmin);
        setOwner(builder.isOwner);
    }


    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }


    public static final class Builder {
        private Long id;
        private User user;
        private House house;
        private boolean isAdmin;
        private boolean isOwner;

        public Builder() {
        }

        public Builder id(Long val) {
            id = val;
            return this;
        }

        public Builder user(User val) {
            user = val;
            return this;
        }

        public Builder house(House val) {
            house = val;
            return this;
        }

        public Builder isAdmin(boolean val) {
            isAdmin = val;
            return this;
        }

        public Builder isOwner(boolean val) {
            isOwner = val;
            return this;
        }

        public House_User build() {
            return new House_User(this);
        }
    }
}
