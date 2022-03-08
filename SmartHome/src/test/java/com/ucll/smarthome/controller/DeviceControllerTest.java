package com.ucll.smarthome.controller;

import com.ucll.smarthome.AbstractIntegrationTest;
import com.ucll.smarthome.dto.*;
import com.ucll.smarthome.persistence.entities.Audio;
import com.ucll.smarthome.persistence.entities.Device;
import com.ucll.smarthome.persistence.entities.House;
import com.ucll.smarthome.persistence.entities.Room;
import com.ucll.smarthome.persistence.repository.AudioDAO;
import com.ucll.smarthome.persistence.repository.DeviceDAO;
import com.ucll.smarthome.persistence.repository.HouseDAO;
import com.ucll.smarthome.persistence.repository.RoomDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.testcontainers.shaded.org.apache.commons.lang.NotImplementedException;

import static org.junit.jupiter.api.Assertions.*;

@WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, value = "TestUser", userDetailsServiceBeanName = "UserDetailService")
class DeviceControllerTest extends AbstractIntegrationTest {
    @Autowired
    private HouseController houseController;

    @Autowired
    private UserController userController;

    @Autowired
    private RoomController roomController;

    @Autowired
    private DeviceController deviceController;

    @Autowired
    private HouseDAO houseDAO;

    @Autowired
    private RoomDAO roomDAO;

    @Autowired
    private DeviceDAO deviceDAO;

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

    private DeviceDTO deviceDTO = new DeviceDTO.Builder()
            .name("testDevice")
            .status(false)
            //.categoryid()
            .build();

    private House searchedHouse;
    private Room searchedRoom;
    private Device searchedDevice;

    @BeforeEach
    void setUp() {
        userController.createUser(user1);
    }

    private void addBeforeTest() {
        houseController.createHouse(testHouse1);

        searchedHouse = houseDAO.findAll().stream()
                .filter(p -> p.getName().equals(testHouse1.getName()))
                .findFirst().get();

        testRoom.setHouseid(searchedHouse.getHouseId());
        roomController.createRoom(testRoom);

        searchedRoom = roomDAO.findAll().stream()
                .filter(p -> p.getName().equals(testRoom.getName()))
                .findFirst().get();

        deviceDTO.setRoomid(searchedRoom.getRoomID());
        deviceController.createDevice(deviceDTO);

        searchedDevice = deviceDAO.findAll().stream()
                .filter(p -> p.getName().equals(deviceDTO.getName()))
                .findFirst().get();


        System.out.println("House created name: " + searchedHouse.getName());
        System.out.println("Room created name: " + searchedRoom.getName());
        System.out.println("Device created name: " + searchedDevice.getName());

    }

    @Test
    void createDevice() {
        addBeforeTest();

        DeviceDTO createDeviceDTO = new DeviceDTO.Builder()
                .name("createTestDevice")
                .status(false)
                .roomid(searchedRoom.getRoomID())
                //.categoryid()
                .build();

        deviceController.createDevice(createDeviceDTO);

        Device createSearchedDevice = deviceDAO.findAll().stream()
                .filter(p -> p.getName().equals(createDeviceDTO.getName()))
                .findFirst().get();

        assertEquals(createDeviceDTO.getName(), createSearchedDevice.getName());
        assertEquals(2, deviceDAO.findAll().size());
    }

    @Test
    void updateDevice() {
        throw new NotImplementedException("Still needs implementation");
    }

    @Test
    void getDeviceById() {
        throw new NotImplementedException("Still needs implementation");
    }

    @Test
    void getDevicdsByRoom() {
        throw new NotImplementedException("Still needs implementation");
    }

    @Test
    void deleteDeviceById() {
        throw new NotImplementedException("Still needs implementation");
    }

    @Test
    void deviceExists() {
        throw new NotImplementedException("Still needs implementation");
    }

    @Test
    void changeStatus() {
        throw new NotImplementedException("Still needs implementation");
    }
}