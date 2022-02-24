package com.ucll.smarthome.persistence.entities;



import javax.persistence.*;

@Entity
@Table(name = "room",schema = "smarthome")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roomid",nullable = false,updatable = false)
    private Long roomID;
    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name= "house_id")
    private House house;

    public Room() {
    }

    private Room(Builder builder) {
        setRoomID(builder.roomID);
        setName(builder.name);
        setHouse(builder.house);
    }


    public Long getRoomID() {
        return roomID;
    }

    public void setRoomID(Long roomID) {
        this.roomID = roomID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public static final class Builder {
        private Long roomID;
        private String name;
        private House house;

        public Builder() {
        }

        public Builder roomID(Long val) {
            roomID = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder house(House val) {
            house = val;
            return this;
        }

        public Room build() {
            return new Room(this);
        }
    }
}
