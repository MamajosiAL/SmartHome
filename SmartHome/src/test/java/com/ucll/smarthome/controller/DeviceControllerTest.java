package com.ucll.smarthome.controller;

import com.ucll.smarthome.AbstractIntegrationTest;
import com.ucll.smarthome.dto.*;
import com.ucll.smarthome.persistence.entities.*;
import com.ucll.smarthome.persistence.repository.CategoryDAO;
import com.ucll.smarthome.persistence.repository.DeviceDAO;
import com.ucll.smarthome.persistence.repository.HouseDAO;
import com.ucll.smarthome.persistence.repository.RoomDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.testcontainers.shaded.org.apache.commons.lang.NotImplementedException;

import javax.persistence.DiscriminatorValue;
import java.util.List;

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
    void createDeviceNameEmpty() {
        addBeforeTest();

        DeviceDTO createDeviceDTO = new DeviceDTO.Builder()
                .name("")
                .status(false)
                .roomid(searchedRoom.getRoomID())
                //.categoryid()
                .build();

        assertThrows(IllegalArgumentException.class, () -> deviceController.createDevice(createDeviceDTO));
    }

    @Test
    void createDeviceNameNull() {
        addBeforeTest();

        DeviceDTO createDeviceDTO = new DeviceDTO.Builder()
                .name("")
                .status(false)
                .roomid(searchedRoom.getRoomID())
                //.categoryid()
                .build();

        assertThrows(IllegalArgumentException.class, () -> deviceController.createDevice(createDeviceDTO));
    }
    
    @Test
    void createDeviceNameSpace() {
        addBeforeTest();

        DeviceDTO createDeviceDTO = new DeviceDTO.Builder()
                .name(" ")
                .status(false)
                .roomid(searchedRoom.getRoomID())
                //.categoryid()
                .build();

        assertThrows(IllegalArgumentException.class, () -> deviceController.createDevice(createDeviceDTO));
    }


    @Test
    void updateDevice() {
        addBeforeTest();

        DeviceDTO updatedDevice = new DeviceDTO.Builder()
                .id(searchedDevice.getId())
                .name("UpdatedDevice")
                .status(false)
                .roomid(searchedRoom.getRoomID())
                .build();

        deviceController.updateDevice(updatedDevice);

        Device updatedDeviceDao = deviceDAO.findAll().stream()
                .filter(p -> p.getName().equals(updatedDevice.getName()))
                .findFirst().get();

        assertEquals(updatedDevice.getName(), updatedDeviceDao.getName());
    }

    @Test
    void updateDeviceEmptyName() {
        addBeforeTest();

        DeviceDTO updatedDevice = new DeviceDTO.Builder()
                .id(searchedDevice.getId())
                .name("")
                .status(false)
                .roomid(searchedRoom.getRoomID())
                .build();
        
        assertThrows(IllegalArgumentException.class, () -> deviceController.updateDevice(updatedDevice));
    }

    @Test
    void updateDeviceNameSpace() {
        addBeforeTest();

        DeviceDTO updatedDevice = new DeviceDTO.Builder()
                .id(searchedDevice.getId())
                .name(" ")
                .status(false)
                .roomid(searchedRoom.getRoomID())
                .build();

        assertThrows(IllegalArgumentException.class, () -> deviceController.updateDevice(updatedDevice));
    }

    @Test
    void updateDeviceNameNull() {
        addBeforeTest();

        DeviceDTO updatedDevice = new DeviceDTO.Builder()
                .id(searchedDevice.getId())
                .name(null)
                .status(false)
                .roomid(searchedRoom.getRoomID())
                .build();

        assertThrows(IllegalArgumentException.class, () -> deviceController.updateDevice(updatedDevice));
    }

    @Test
    void getDeviceById() {
        addBeforeTest();
        assertEquals(searchedDevice.getName(), deviceController.getDeviceById(searchedDevice.getId()).getName());
    }

    @Test
    void getDeviceById0() {
        addBeforeTest();
        assertEquals(searchedDevice.getName(), deviceController.getDeviceById(searchedDevice.getId()).getName());

        assertThrows(IllegalArgumentException.class, () -> deviceController.getDeviceById(0));
    }

    @Test
    void getDeviceByIdNegative() {
        addBeforeTest();

        assertThrows(IllegalArgumentException.class, () -> deviceController.getDeviceById(-12));
    }

    @Test
    void getDevicdsByRoom() {
        addBeforeTest();

        assertEquals(1, deviceController.getDevicdsByRoom(searchedRoom.getRoomID()).size());
        assertEquals(deviceController.getDevicdsByRoom(searchedRoom.getRoomID()).get(0).getName(), deviceDTO.getName());
        assertEquals(deviceController.getDevicdsByRoom(searchedRoom.getRoomID()).get(0).getId(), searchedDevice.getId());
    }

    @Test
    void deleteDevice() {
        addBeforeTest();

        deviceController.deleteDeviceById(searchedDevice.getId());

        assertEquals(0, deviceController.getDevicdsByRoom(searchedRoom.getRoomID()).size());
    }

    @Test
    void deviceExists() {
        //TODO: Private?
        throw new NotImplementedException("Still needs implementation - Private?");
    }

    @Test
    void changeStatus() {
        addBeforeTest();

        assertFalse(deviceController.getDeviceById(searchedDevice.getId()).isStatus());

        deviceController.changeStatus(searchedDevice.getId());

        assertTrue(deviceController.getDeviceById(searchedDevice.getId()).isStatus());
    }

    @Test
    void addDeviceWithCategory(){
        addBeforeTest();
        int categoryid = Integer.parseInt(searchedDevice.getClass().getAnnotation(DiscriminatorValue.class).value());
        assertEquals(4, categoryid);
    }
}