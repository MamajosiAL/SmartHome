package com.ucll.smarthome.controller;

import com.ucll.smarthome.AbstractIntegrationTest;
import com.ucll.smarthome.dto.*;
import com.ucll.smarthome.persistence.entities.Device;
import com.ucll.smarthome.persistence.entities.House;
import com.ucll.smarthome.persistence.entities.Room;
import com.ucll.smarthome.persistence.entities.SensorDevice;
import com.ucll.smarthome.persistence.repository.HouseDAO;
import com.ucll.smarthome.persistence.repository.RoomDAO;
import com.ucll.smarthome.persistence.repository.SensorDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;

import javax.persistence.DiscriminatorValue;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, value = "TestUser", userDetailsServiceBeanName = "UserDetailService")
class SensorControllerTest extends AbstractIntegrationTest {

    @Autowired
    private HouseController houseController;

    @Autowired
    private UserController userController;

    @Autowired
    private RoomController roomController;

    @Autowired
    private SensorController sensorController;

    @Autowired
    private HouseDAO houseDAO;

    @Autowired
    private RoomDAO roomDAO;

    @Autowired
    private SensorDAO sensorDAO;

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

    private SensorDTO sensorDTO = new SensorDTO.Builder()
            .name("TestingSensor")
            .sensorType("Smart Stekker")
            .status(false)
            .sensordata(24)
            .build();

    private House searchedHouse;
    private Room searchedRoom;
    private SensorDevice sensorDevice;

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

        sensorDTO.setRoomid(searchedRoom.getRoomID());
        sensorController.createSensorDevice(sensorDTO);

        sensorDevice = sensorDAO.findAll().stream()
                .filter(p -> p.getName().equals(sensorDTO.getName()))
                .findFirst().get();


        System.out.println("House created name: " + searchedHouse.getName());
        System.out.println("Room created name: " + searchedRoom.getName());
        System.out.println("Sensor created name: " + sensorDevice.getName());

    }

    @Test
    void createSensor() {
        addBeforeTest();

        SensorDTO createSensorDTO = new SensorDTO.Builder()
                .name("TestingSensor")
                .sensorType("Smart Stekker")
                .status(false)
                .sensordata(24)
                .roomid(searchedRoom.getRoomID())
                .build();

        sensorController.createSensorDevice(createSensorDTO);

        SensorDevice createsearcheddevice = sensorDAO.findAll().stream()
                .filter(p -> p.getName().equals(createSensorDTO.getName()))
                .findFirst().get();

        assertEquals(createsearcheddevice.getName(), createsearcheddevice.getName());
        assertEquals(2, sensorDAO.findAll().size());
    }

    @Test
    void createSensorNameNull() {
        addBeforeTest();

        SensorDTO createSensorDTO = new SensorDTO.Builder()
                .name(null)
                .sensorType("Smart Stekker")
                .status(false)
                .sensordata(24)
                .build();

        assertThrows(IllegalArgumentException.class, () -> sensorController.createSensorDevice(createSensorDTO));
    }

    @Test
    void createSensorNameSpace() {
        addBeforeTest();

        SensorDTO createSensorDTO = new SensorDTO.Builder()
                .name(" ")
                .sensorType("Smart Stekker")
                .status(false)
                .sensordata(24)
                .build();

        assertThrows(IllegalArgumentException.class, () -> sensorController.createSensorDevice(createSensorDTO));
    }

    @Test
    void createSensorNameEmpty() {
        addBeforeTest();

        SensorDTO createSensorDTO = new SensorDTO.Builder()
                .name("")
                .sensorType("Smart Stekker")
                .status(false)
                .sensordata(24)
                .build();

        assertThrows(IllegalArgumentException.class, () -> sensorController.createSensorDevice(createSensorDTO));
    }

    @Test
    void createSensorSensorDataNegative() {
        addBeforeTest();

        SensorDTO createSensorDTO = new SensorDTO.Builder()
                .name("TesingSenor")
                .sensorType("Smart Stekker")
                .status(false)
                .sensordata(-24)
                .build();

        assertThrows(IllegalArgumentException.class, () -> sensorController.createSensorDevice(createSensorDTO));
    }

    @Test
    void updateSensorDevice() {
        addBeforeTest();

        SensorDTO updateSensorDevice = new SensorDTO.Builder()
                .id(sensorDevice.getId())
                .name("Altered Tekst")
                .sensorType("Smart Stekker")
                .status(false)
                .sensordata(24)
                .roomid(searchedRoom.getRoomID())
                .build();

        sensorController.updateSensorDevice(updateSensorDevice);

        SensorDevice updateSensorDeviceDAO = sensorDAO.findAll().stream()
                .filter(p -> p.getName().equals(updateSensorDevice.getName()))
                .findFirst().get();

        assertEquals(updateSensorDevice.getName(), updateSensorDeviceDAO.getName());
    }

    @Test
    void updateSensorDeviceNameSpace() {
        addBeforeTest();

        SensorDTO updateSensorDevice = new SensorDTO.Builder()
                .id(sensorDevice.getId())
                .name(" ")
                .sensorType("Smart Stekker")
                .status(false)
                .sensordata(24)
                .roomid(searchedRoom.getRoomID())
                .build();

        assertThrows(IllegalArgumentException.class, () -> sensorController.updateSensorDevice(updateSensorDevice));
    }
    
    @Test
    void updateSensorDeviceNameNull() {
        addBeforeTest();

        SensorDTO updateSensorDevice = new SensorDTO.Builder()
                .id(sensorDevice.getId())
                .name(null)
                .sensorType("Smart Stekker")
                .status(false)
                .sensordata(24)
                .roomid(searchedRoom.getRoomID())
                .build();

        assertThrows(IllegalArgumentException.class, () -> sensorController.updateSensorDevice(updateSensorDevice));
    }
   
    @Test
    void updateSensorDeviceNameEmpty() {
        addBeforeTest();

        SensorDTO updateSensorDevice = new SensorDTO.Builder()
                .id(sensorDevice.getId())
                .name("")
                .sensorType("Smart Stekker")
                .status(false)
                .sensordata(24)
                .roomid(searchedRoom.getRoomID())
                .build();

        assertThrows(IllegalArgumentException.class, () -> sensorController.updateSensorDevice(updateSensorDevice));
    }

    @Test
    void getDeviceById() {
        addBeforeTest();
        assertEquals(sensorDevice.getName(), sensorController.getSensorDeviceById(sensorDevice.getId()).getName());
    }

    @Test
    void getDeviceById0() {
        addBeforeTest();
        assertEquals(sensorDevice.getName(), sensorController.getSensorDeviceById(sensorDevice.getId()).getName());

        assertThrows(IllegalArgumentException.class, () -> sensorController.getSensorDeviceById(0));
    }

    @Test
    void getDeviceByIdNegative() {
        addBeforeTest();

        assertThrows(IllegalArgumentException.class, () -> sensorController.getSensorDeviceById(-12));
    }

    @Test
    void getDevicdsByRoom() {
        addBeforeTest();

        assertEquals(1, sensorController.getSonsorDevicesByRoom(searchedRoom.getRoomID()).size());
        assertEquals(sensorController.getSonsorDevicesByRoom(searchedRoom.getRoomID()).get(0).getName(), sensorDTO.getName());
        assertEquals(sensorController.getSonsorDevicesByRoom(searchedRoom.getRoomID()).get(0).getId(), sensorDevice.getId());
    }

    @Test
    void deleteDevice() {
        addBeforeTest();

        sensorController.deleteSensorDeviceById(sensorDevice.getId());

        assertEquals(0, sensorController.getSonsorDevicesByRoom(searchedRoom.getRoomID()).size());
    }

    @Test
    void changeStatus() {
        addBeforeTest();

        assertFalse(sensorController.getSensorDeviceById(sensorDevice.getId()).isStatus());

        sensorController.changeStatus(sensorDevice.getId());

        assertTrue(sensorController.getSensorDeviceById(sensorDevice.getId()).isStatus());
    }

    @Test
    void addDeviceWithCategory(){
        addBeforeTest();

        int categoryid = Integer.parseInt(sensorDevice.getClass().getAnnotation(DiscriminatorValue.class).value());
        assertEquals(3, categoryid);
    }
}