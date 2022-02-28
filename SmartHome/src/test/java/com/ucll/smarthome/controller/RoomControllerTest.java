package com.ucll.smarthome.controller;

import com.ucll.smarthome.AbstractIntegrationTest;
import com.ucll.smarthome.dto.HouseDTO;
import com.ucll.smarthome.dto.RoomDTO;
import com.ucll.smarthome.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoomControllerTest extends AbstractIntegrationTest {

    @Autowired
    HouseController houseController;

    @Autowired
    RoomController roomController;

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
    
    //Happy path create room 
    @Test
    void createRoom() {
        RoomDTO testRoomCreate = new RoomDTO.Builder()
                .name("testRoomCreate")
                .houseid(testHouseId)
                .build();

        roomController.createRoom(testRoomCreate);

        RoomDTO checkRoom = roomController.getRoomById(testRoomId + 1);
        assertEquals(checkRoom.getName(), testRoomCreate.getName());
        assertEquals(checkRoom.getId(), testRoomId + 1);
        assertEquals(checkRoom.getHouseid(), testHouseId);
    }
    
    //Create room tests
    @Test 
    void createRoomNullName(){
        RoomDTO roomNullName = testRoom1; 
        roomNullName.setName(null);
        
        assertThrows(IllegalArgumentException.class, () -> roomController.createRoom(roomNullName));
    }

    @Test
    void createRoomEmptyName(){
        RoomDTO roomEmptyName = testRoom1;
        roomEmptyName.setName("");

        assertThrows(IllegalArgumentException.class, () -> roomController.createRoom(roomEmptyName));
    }

    @Test
    void createRoomHouseID0(){
        RoomDTO roomID0 = testRoom1;
        roomID0.setHouseid(0);

        assertThrows(IllegalArgumentException.class, () -> roomController.createRoom(roomID0));
    }
    
    @Test
    void createRoomHouseIDNegative(){
        RoomDTO roomIDNegative = testRoom1;
        roomIDNegative.setHouseid(-666);

        assertThrows(IllegalArgumentException.class, () -> roomController.createRoom(roomIDNegative));
    }
    
    @Test
    void createRoomSpaceAsName(){
        RoomDTO roomNullSpace = testRoom1;
        roomNullSpace.setName(" ");

        assertThrows(IllegalArgumentException.class, () -> roomController.createRoom(roomNullSpace));
    }
    
    //Happy path update room
    @Test
    void updateRoom() {
        RoomDTO testRoomUpdate = new RoomDTO.Builder()
                .id(testRoomId)
                .name("testRoomCreate")
                .build();

        roomController.updateRoom(testRoomUpdate);

        RoomDTO checkRoom = roomController.getRoomById(testRoomId);
        assertEquals(checkRoom.getName(), testRoomUpdate.getName());
        assertNotEquals(checkRoom.getName(), testRoom1.getName());

    }

    //Update room tests
    
    @Test
    void updateRoomNullName(){
        RoomDTO roomNullName = testRoom1;
        roomNullName.setName(null);

        assertThrows(IllegalArgumentException.class, () -> roomController.updateRoom(roomNullName));
    }

    @Test
    void updateRoomEmptyName(){
        RoomDTO roomEmptyName = testRoom1;
        roomEmptyName.setName("");

        assertThrows(IllegalArgumentException.class, () -> roomController.updateRoom(roomEmptyName));
    }

    @Test
    void updateRoomHouseID0(){
        RoomDTO roomID0 = testRoom1;
        roomID0.setHouseid(0);

        assertThrows(IllegalArgumentException.class, () -> roomController.updateRoom(roomID0));
    }

    @Test
    void updateRoomHouseIDNegative(){
        RoomDTO roomIDNegative = testRoom1;
        roomIDNegative.setHouseid(-666);

        assertThrows(IllegalArgumentException.class, () -> roomController.updateRoom(roomIDNegative));
    }

    @Test
    void updateRoomSpaceAsName(){
        RoomDTO roomNullSpace = testRoom1;
        roomNullSpace.setName(" ");

        assertThrows(IllegalArgumentException.class, () -> roomController.updateRoom(roomNullSpace));
    }

    //Get methodes
    @Test
    void getRoomById() {
        assertEquals(testRoom1.getName(), roomController.getRoomById(testRoomId).getName());
        assertEquals(testRoomId, roomController.getRoomById(testRoomId).getId());
    }

    @Test
    void getRoomById0(){
        assertThrows(IllegalArgumentException.class, () -> roomController.getRoomById(0));
    }

    @Test
    void getRoomByIdNegative(){
        assertThrows(IllegalArgumentException.class, () -> roomController.getRoomById(-3));
    }

    @Test
    void getRoomsByHouse() {
        RoomDTO testRoomGetRooms = new RoomDTO.Builder()
                .name("testRoomGetRooms")
                .houseid(testHouseId)
                .build();

        roomController.createRoom(testRoomGetRooms);

        List<RoomDTO> checkRooms = roomController.getRoomsByHouse(testHouseId);
        assertEquals(2, roomController.getRoomsByHouse(testHouseId).size());
        assertTrue(checkRooms.stream().anyMatch(p -> p.getName().equals(testRoom1.getName())));
        assertTrue(checkRooms.stream().anyMatch(p -> p.getName().equals(testRoomGetRooms.getName())));
    }

    @Test
    void deleteRoom() {
        roomController.deleteRoom(testRoomId);
        assertEquals(0, roomController.getRoomsByHouse(testHouseId).size());
    }
}