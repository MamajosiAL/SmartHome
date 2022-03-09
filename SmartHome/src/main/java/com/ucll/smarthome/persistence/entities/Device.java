package com.ucll.smarthome.persistence.entities;

import javax.persistence.*;

@Entity
@Table(name = "device", schema = "smarthome")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "category_id", discriminatorType = DiscriminatorType.INTEGER)
@DiscriminatorValue("4")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id" , updatable = false,nullable = false)
    private Long id;
    @Column(name ="category_id",insertable = false,updatable = false)
    private int categoryid;
    @Column(name = "name")
    private String name;
    @Column(name = "status")
    private boolean status;



    @ManyToOne
    @JoinColumn(name= "room_id")
    private Room room;

    public Device() {
    }

    private Device(Builder builder) {
        setId(builder.id);
        setCategoryid(builder.categoryid);
        setName(builder.name);
        setStatus(builder.status);
        setRoom(builder.room);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }


    public static final class Builder {
        private Long id;
        private int categoryid;
        private String name;
        private boolean status;
        private Room room;

        public Builder() {
        }

        public Builder id(Long val) {
            id = val;
            return this;
        }

        public Builder categoryid(int val) {
            categoryid = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder status(boolean val) {
            status = val;
            return this;
        }

        public Builder room(Room val) {
            room = val;
            return this;
        }

        public Device build() {
            return new Device(this);
        }
    }
}