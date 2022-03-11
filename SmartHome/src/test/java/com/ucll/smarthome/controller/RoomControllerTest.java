package com.ucll.smarthome.controller;

import com.ucll.smarthome.AbstractIntegrationTest;
import com.ucll.smarthome.dto.HouseDTO;
import com.ucll.smarthome.dto.RoomDTO;
import com.ucll.smarthome.dto.UserDTO;
import com.ucll.smarthome.persistence.entities.House;
import com.ucll.smarthome.persistence.entities.Room;
import com.ucll.smarthome.persistence.repository.HouseDAO;
import com.ucll.smarthome.persistence.repository.RoomDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, value = "TestUser", userDetailsServiceBeanName = "UserDetailService")
class RoomControllerTest extends AbstractIntegrationTest {

    @Autowired
    private HouseController houseController;

    @Autowired
    private UserController userController;

    @Autowired
    private RoomController roomController;

    @Autowired
    private HouseDAO houseDAO;

    @Autowired
    private RoomDAO roomDAO;

    private UserDTO user1 = new UserDTO.Builder()
            .firstname("Testing")
            .name("User")
            .username("TestUser")
            .email("TestingUser@glock.com")
            .password("TUROX")
            .build();

    private HouseDTO testHouse1 = new HouseDTO.Builder()
            .name("testHouse")
            .build();

    private RoomDTO testRoom = new RoomDTO.Builder()
            .name("testRoom")
            .build();

    private House searechHouse;

    @BeforeEach
    void setUp() {
        userController.createUser(user1);
    }

    private void addBeforeTest(){
        houseController.createHouse(testHouse1);

        searechHouse = houseDAO.findAll().stream()
                .filter(p-> p.getName().equals(testHouse1.getName()))
                .findFirst().get();
    }

    //Happy path create Room
    @Test
    void createRoom() {
        addBeforeTest();

        houseController.getHousesByUser();

        RoomDTO testRoom = new RoomDTO.Builder()
                .name("testRoom")
                .houseid(searechHouse.getHouseId())
                .build();

        roomController.createRoom(testRoom);

        assertEquals(roomController.getRoomsByHouse(searechHouse.getHouseId()).size(), 1);
    }

    @Test
    void createRoomNullName(){
        RoomDTO roomNullName = testRoom;
        roomNullName.setName(null);

        assertThrows(IllegalArgumentException.class, () -> roomController.createRoom(roomNullName));
    }

    @Test
    void createRoomEmptyName(){
        RoomDTO roomEmptyName = testRoom;
        roomEmptyName.setName("");

        assertThrows(IllegalArgumentException.class, () -> roomController.createRoom(roomEmptyName));
    }

    @Test
    void createRoomHouseID0(){
        RoomDTO roomID0 = testRoom;
        roomID0.setHouseid(0);

        assertThrows(IllegalArgumentException.class, () -> roomController.createRoom(roomID0));
    }

    @Test
    void createRoomHouseIDNegative(){
        RoomDTO roomIDNegative = testRoom;
        roomIDNegative.setHouseid(-666);

        assertThrows(IllegalArgumentException.class, () -> roomController.createRoom(roomIDNegative));
    }

    @Test
    void createRoomSpaceAsName(){
        RoomDTO roomNullSpace = testRoom;
        roomNullSpace.setName(" ");

        assertThrows(IllegalArgumentException.class, () -> roomController.createRoom(roomNullSpace));
    }

    //Happy Path Update Room
    @Test
    void updateRoom() {
        addBeforeTest();

        testRoom.setHouseid(searechHouse.getHouseId());
        roomController.createRoom(testRoom);

        Room room = roomDAO.findAll().stream()
                .filter(p -> p.getName().equals(testRoom.getName()))
                .findFirst()
                .get();

        RoomDTO testRoomUpdate = new RoomDTO.Builder()
                .id(room.getRoomID())
                .name("RoomUpdated")
                .build();

        roomController.updateRoom(testRoomUpdate);

        RoomDTO checkRoom = roomController.getRoomById(room.getRoomID());
        assertEquals(checkRoom.getName(), testRoomUpdate.getName());
        assertNotEquals(checkRoom.getName(), testRoom.getName());
    }

    @Test
    void updateRoomNullName(){
        RoomDTO roomNullName = testRoom;
        roomNullName.setName(null);

        assertThrows(IllegalArgumentException.class, () -> roomController.updateRoom(roomNullName));
    }

    @Test
    void updateRoomEmptyName(){
        RoomDTO roomEmptyName = testRoom;
        roomEmptyName.setName("");

        assertThrows(IllegalArgumentException.class, () -> roomController.updateRoom(roomEmptyName));
    }

    @Test
    void updateRoomHouseID0(){
        RoomDTO roomID0 = testRoom;
        roomID0.setHouseid(0);

        assertThrows(IllegalArgumentException.class, () -> roomController.updateRoom(roomID0));
    }

    @Test
    void updateRoomHouseIDNegative(){
        RoomDTO roomIDNegative = testRoom;
        roomIDNegative.setHouseid(-666);

        assertThrows(IllegalArgumentException.class, () -> roomController.updateRoom(roomIDNegative));
    }

    @Test
    void updateRoomSpaceAsName(){
        RoomDTO roomNullSpace = testRoom;
        roomNullSpace.setName(" ");

        assertThrows(IllegalArgumentException.class, () -> roomController.updateRoom(roomNullSpace));
    }

    @Test
    void getRoomById() {
        addBeforeTest();

        testRoom.setHouseid(searechHouse.getHouseId());

        roomController.createRoom(testRoom);

        Room room = roomDAO.findAll().stream()
                .filter(p -> p.getName().equals(testRoom.getName()))
                .findFirst()
                .get();

        assertEquals(testRoom.getName(), roomController.getRoomById(room.getRoomID()).getName());
        assertEquals(room.getRoomID(), roomController.getRoomById(room.getRoomID()).getId());
    }

    @Test
    void getRoomsByHouse() {
        addBeforeTest();

        testRoom.setHouseid(searechHouse.getHouseId());
        roomController.createRoom(testRoom);

        Room daoRoom = roomDAO.findAll().stream()
                .filter(p -> p.getName().equals(testRoom.getName()))
                .findFirst().get();

        testRoom.setId(daoRoom.getRoomID());
        List<String> nameFromRooms = roomController.getRoomsByHouse(searechHouse.getHouseId()).stream()
                .map(RoomDTO::getName)
                .collect(Collectors.toList());

        assertTrue(nameFromRooms.contains(testRoom.getName()));
    }

    @Test
    void deleteRoom() {
        addBeforeTest();

        testRoom.setHouseid(searechHouse.getHouseId());

        roomController.createRoom(testRoom);

        Room room = roomDAO.findAll().stream()
                .filter(p -> p.getName().equals(testRoom.getName()))
                .findFirst()
                .get();

        roomController.deleteRoom(room.getRoomID());

        assertEquals(0, roomController.getRoomsByHouse(searechHouse.getHouseId()).size());
    }
}