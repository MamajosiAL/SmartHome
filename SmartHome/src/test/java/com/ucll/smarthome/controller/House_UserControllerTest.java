package com.ucll.smarthome.controller;

import com.ucll.smarthome.AbstractIntegrationTest;
import com.ucll.smarthome.dto.HouseDTO;
import com.ucll.smarthome.dto.RoomDTO;
import com.ucll.smarthome.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class House_UserControllerTest extends AbstractIntegrationTest {

    @Autowired
    HouseController houseController;

    @Autowired
    RoomController roomController;

    @Autowired
    UserController userController;

    @Autowired
    House_UserController houseUserController;

    private UserDTO user1 = new UserDTO.Builder()
            .firstname("Testing")
            .name("User")
            .username("TestingUser1")
            .email("TestingUser@glock.com")
            .password("TUROX")
            .build();

    private HouseDTO testHouse1 = new HouseDTO.Builder()
            .name("testHouse")
            .build();

    private RoomDTO testRoom1 = new RoomDTO.Builder()
            .name("testRoom")
            .build();

    private long testUserId;
    private long testHouseId;
    private long testRoomId;

    @BeforeEach
    void setUp() {
        userController.createUser(user1);
        userController.getAllUsers().forEach(p -> {
            System.out.println("Test user created: id = " + (p.getId()));
            testUserId = p.getId();
        });

        testHouse1.setUserid(testUserId);
        houseController.createHouse(testHouse1);
        houseController.getAllHouses().forEach(p -> {
            System.out.println("Test house created: id = " + (p.getId()));
            testHouseId = p.getId();
        });

        testRoom1.setHouseid(testHouseId);
        roomController.createRoom(testRoom1);
        roomController.getRoomsByHouse(testHouseId).forEach(p -> {
            System.out.println("Test room created: id = " + (p.getId()));
            testRoomId = p.getId();
        });
    }

    @Test
    void createRegistratoinHouseUser() {
    }

    @Test
    void updateRegistrationHouseUsser() {
    }

    @Test
    void getByUser() {
    }

    @Test
    void getByHouse() {
    }

    @Test
    void deleteRegistratieHouseUser() {
    }
}