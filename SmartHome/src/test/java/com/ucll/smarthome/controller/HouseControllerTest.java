package com.ucll.smarthome.controller;

import com.ucll.smarthome.AbstractIntegrationTest;
import com.ucll.smarthome.dto.HouseDTO;
import com.ucll.smarthome.dto.UserDTO;
import com.ucll.smarthome.persistence.entities.House;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HouseControllerTest extends AbstractIntegrationTest {

    @Autowired
    HouseController houseController;

    @Autowired
    UserController userController;

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

    private long testUserId;
    private long testHouseId;

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
    }

    @Test
    void createHouse() {
        String houseName = "MyCrib";

        HouseDTO dto = new HouseDTO.Builder().name(houseName)
                .userid(testUserId)
                .build();

        houseController.createHouse(dto);

        HouseDTO checkHouse = houseController.getHouseById(testHouseId + 1);
        assertEquals(checkHouse.getName(), houseName);
        assertEquals(testUserId, checkHouse.getUserid());
    }

    @Test
    void createHouseNameNull() {
        String houseName = null;

        HouseDTO dto = new HouseDTO.Builder().name(houseName)
                .userid(testUserId)
                .build();

        assertThrows(IllegalArgumentException.class, () -> houseController.createHouse(dto));
    }

    @Test
    void createHouseNameSpace() {
        String houseName = " ";

        HouseDTO dto = new HouseDTO.Builder().name(houseName)
                .userid(testUserId)
                .build();

        assertThrows(IllegalArgumentException.class, () -> houseController.createHouse(dto));
    }

    @Test
    void createHouseNameEmpty() {
        String houseName = "";

        HouseDTO dto = new HouseDTO.Builder().name(houseName)
                .userid(testUserId)
                .build();

        assertThrows(IllegalArgumentException.class, () -> houseController.createHouse(dto));
    }

    @Test
    void createHouseuserIDEmtpy() {
        String houseName = "MyCrib";

        HouseDTO dto = new HouseDTO.Builder().name(houseName)
                .userid(0)
                .build();

        assertThrows(IllegalArgumentException.class, () -> houseController.createHouse(dto));
    }

    @Test
    void createHouseuserIDNegative() {
        String houseName = "MyCrib";

        HouseDTO dto = new HouseDTO.Builder().name(houseName)
                .userid(-99)
                .build();

        assertThrows(IllegalArgumentException.class, () -> houseController.createHouse(dto));
    }

    @Test
    void updateHouse() {
        String testHouseName = testHouse1.getName();
        String alteredHouseName = testHouseName + "Altered";

        HouseDTO dto = new HouseDTO.Builder()
                .id(testHouseId)
                .name(alteredHouseName)
                .build();

        houseController.updateHouse(dto);

        HouseDTO checkHouse = houseController.getHouseById(testHouseId);
        assertEquals(checkHouse.getId(), testHouseId);
        assertEquals(checkHouse.getName(), alteredHouseName);
        assertNotEquals(checkHouse.getName(), testHouseName);
        assertEquals(checkHouse.getUserid(), testUserId);
    }

    @Test
    void updateHouseNameNull() {
        String testHouseName = testHouse1.getName();
        String alteredHouseName = null;

        HouseDTO dto = new HouseDTO.Builder()
                .id(testHouseId)
                .name(alteredHouseName)
                .build();

        assertThrows(IllegalArgumentException.class,() -> houseController.updateHouse(dto));
    }

    @Test
    void updateHouseNameEmpty() {
        String testHouseName = testHouse1.getName();
        String alteredHouseName = "";

        HouseDTO dto = new HouseDTO.Builder()
                .id(testHouseId)
                .name(alteredHouseName)
                .build();

        assertThrows(IllegalArgumentException.class,() -> houseController.updateHouse(dto));
    }

    @Test
    void updateHouseNameSpace() {
        String testHouseName = testHouse1.getName();
        String alteredHouseName = " ";

        HouseDTO dto = new HouseDTO.Builder()
                .id(testHouseId)
                .name(alteredHouseName)
                .build();

        assertThrows(IllegalArgumentException.class,() -> houseController.updateHouse(dto));
    }

    @Test
    void updateHouseID0() {
        String testHouseName = testHouse1.getName();
        String alteredHouseName = "MyCrib";

        HouseDTO dto = new HouseDTO.Builder()
                .id(0)
                .name(alteredHouseName)
                .build();

        assertThrows(IllegalArgumentException.class,() -> houseController.updateHouse(dto));
    }

    @Test
    void updateHouseIDNegative() {
        String testHouseName = testHouse1.getName();
        String alteredHouseName = "MyCrib";

        HouseDTO dto = new HouseDTO.Builder()
                .id(-789)
                .name(alteredHouseName)
                .build();

        assertThrows(IllegalArgumentException.class,() -> houseController.updateHouse(dto));
    }

    @Test
    void getHouseById() {
        assertEquals(houseController.getHouseById(testHouseId).getId(), testHouseId);
        assertEquals(houseController.getHouseById(testHouseId).getName(), testHouse1.getName());
        assertEquals(houseController.getHouseById(testHouseId).getUserid(), testHouse1.getUserid());
    }


    @Test
    void getHouseBy0() {
        assertThrows(IllegalArgumentException.class, () -> houseController.getHouseById(0));
    }

    @Test
    void getHouseByNegative() {
        assertThrows(IllegalArgumentException.class, () -> houseController.getHouseById(-654));
    }

    //TODO vanaf hier

    @Test
    void getHousesByUser() {
        String houseName = "MyCrib";

        HouseDTO dto = new HouseDTO.Builder().name(houseName)
                .userid(testUserId)
                .build();

        houseController.createHouse(dto);

        HouseDTO checkHouse = houseController.getHouseById(testHouseId + 1);

        List<HouseDTO> checkHouses = houseController.getHousesByUser(testUserId);
        assertEquals(checkHouses.size(), 2);
        assertTrue(checkHouses.stream().anyMatch(p -> p.getName().equals(testHouse1.getName())));
        assertTrue(checkHouses.stream().anyMatch(p -> p.getName().equals(houseName)));

    }

    @Test
    void getAllHouses() {
        assertEquals(1, houseController.getAllHouses().size());
    }

    @Test
    void deleteHouse() {
        houseController.deleteHouse(testHouseId);

        assertEquals(0, houseController.getAllHouses().size());
    }
}