package com.ucll.smarthome.controller;

import com.ucll.smarthome.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.ucll.smarthome.persistence.repository.*;
import com.ucll.smarthome.dto.*;
import com.ucll.smarthome.persistence.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.testcontainers.shaded.org.apache.commons.lang.NotImplementedException;

import static org.junit.jupiter.api.Assertions.*;

@WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, value = "TestUser", userDetailsServiceBeanName = "UserDetailService")
class BigElectronicControllerTest extends AbstractIntegrationTest {

    @Autowired
    private HouseController houseController;

    @Autowired
    private UserController userController;

    @Autowired
    private RoomController roomController;

    @Autowired
    private BigElectronicController bigElectronicController;

    @Autowired
    private HouseDAO houseDAO;

    @Autowired
    private RoomDAO roomDAO;

    @Autowired
    private TypeDAO typeDAO;

    @Autowired
    private BigElectronicDAO bigElectronicDAO;

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

    private BigElectronicDTO bigElectronicDTO = new BigElectronicDTO.Builder()
            .name("testVaatwasser")
            .status(false)
            .build();


    private House searchedHouse;
    private Room searchedRoom;
    private BigElectronicDevice searchedBigElectronic;

    @BeforeEach
    void setUp() {
        userController.createUser(user1);
    }

    private void addBeforeTest(){
        houseController.createHouse(testHouse1);

        searchedHouse = houseDAO.findAll().stream()
                .filter(p-> p.getName().equals(testHouse1.getName()))
                .findFirst().get();

        testRoom.setHouseid(searchedHouse.getHouseId());
        roomController.createRoom(testRoom);

        searchedRoom = roomDAO.findAll().stream()
                .filter(p-> p.getName().equals(testRoom.getName()))
                .findFirst().get();

        Type vaatwasserType = typeDAO.findAll().stream()
                .filter(p -> p.getName().equals("Dishwasher"))
                .findFirst()
                .get();

        bigElectronicDTO.setRoomid(searchedRoom.getRoomID());
        bigElectronicDTO.setType(vaatwasserType);

        bigElectronicController.createApplianceDevice(bigElectronicDTO);

        searchedBigElectronic = bigElectronicDAO.findAll().stream()
                .filter(p -> p.getName().equals(bigElectronicDTO.getName()))
                        .findFirst()
                                .get();

        System.out.println("House created name: " + searchedHouse.getName());
        System.out.println("Room created name: " + searchedRoom.getName());
        System.out.println("Big Electronic device created " + searchedBigElectronic.getName());
    }

    @Test
    void createApplianceDevice() {
        addBeforeTest();

        BigElectronicDTO bigElectronicDTO = new BigElectronicDTO.Builder()
                .name("testSmartOven")
                .roomid(searchedRoom.getRoomID())
                .build();

        Type smartovenType;

        smartovenType = typeDAO.findAll().stream()
                .filter(p -> p.getName().equals("oven"))
                .findFirst()
                .get();
        
        bigElectronicDTO.setType(smartovenType);

        bigElectronicController.createApplianceDevice(bigElectronicDTO);

        assertEquals(2, bigElectronicDAO.findAll().size());
    }

    @Test
    void createApplianceNameNull() {
        addBeforeTest();

        BigElectronicDTO bigElectronicDTO = new BigElectronicDTO.Builder()
                .name(null)
                .roomid(searchedRoom.getRoomID())
                .build();

        Type smartovenType;

        smartovenType = typeDAO.findAll().stream()
                .filter(p -> p.getName().equals("oven"))
                .findFirst()
                .get();
        
        bigElectronicDTO.setType(smartovenType);

        assertThrows(IllegalArgumentException.class, () -> bigElectronicController.createApplianceDevice(bigElectronicDTO));
    }

    @Test
    void createApplianceNameSpace() {
        addBeforeTest();

        BigElectronicDTO bigElectronicDTO = new BigElectronicDTO.Builder()
                .name(" ")
                .roomid(searchedRoom.getRoomID())
                .build();

        Type smartovenType;

        smartovenType = typeDAO.findAll().stream()
                .filter(p -> p.getName().equals("oven"))
                .findFirst()
                .get();
        
        bigElectronicDTO.setType(smartovenType);

        assertThrows(IllegalArgumentException.class, () -> bigElectronicController.createApplianceDevice(bigElectronicDTO));
    }

    @Test
    void createApplianceNameEmpty() {
        addBeforeTest();

        BigElectronicDTO bigElectronicDTO = new BigElectronicDTO.Builder()
                .name("")
                .roomid(searchedRoom.getRoomID())
                .build();

        Type smartovenType;

        smartovenType = typeDAO.findAll().stream()
                .filter(p -> p.getName().equals("oven"))
                .findFirst()
                .get();
        
        bigElectronicDTO.setType(smartovenType);

        assertThrows(IllegalArgumentException.class, () -> bigElectronicController.createApplianceDevice(bigElectronicDTO));
    }

    @Test
    void createApplianceRoomIDNonExisting() {
        addBeforeTest();

        BigElectronicDTO bigElectronicDTO = new BigElectronicDTO.Builder()
                .name("testSmartOven")
                .roomid(45567894)
                .build();

        Type smartovenType;

        smartovenType = typeDAO.findAll().stream()
                .filter(p -> p.getName().equals("oven"))
                .findFirst()
                .get();
        
        bigElectronicDTO.setType(smartovenType);

        assertThrows(IllegalArgumentException.class, () -> bigElectronicController.createApplianceDevice(bigElectronicDTO));
    }

    @Test
    void updateApplianceDevice() {
        addBeforeTest();
        String alteredName = "AlteredTestName";
        String originalName = searchedBigElectronic.getName();

        BigElectronicDTO updateBigElectronicDTO = bigElectronicDTO;
        updateBigElectronicDTO.setId(searchedBigElectronic.getId());
        updateBigElectronicDTO.setName(alteredName);

        bigElectronicController.updateApplianceDevice(updateBigElectronicDTO);

        BigElectronicDevice updatedsearchedBigElectronic = bigElectronicDAO.findAll().stream()
                .filter(p -> p.getName().equals(bigElectronicDTO.getName()))
                .findFirst()
                .get();

        assertEquals(alteredName, updatedsearchedBigElectronic.getName());
        assertEquals(updatedsearchedBigElectronic.getId() , searchedBigElectronic.getId());
        assertNotEquals(updatedsearchedBigElectronic.getName() , originalName);
    }

    @Test
    void updateApplianceDeviceNameNull() {
        addBeforeTest();
        String alteredName = null;

        BigElectronicDTO updateBigElectronicDTO = bigElectronicDTO;
        updateBigElectronicDTO.setId(searchedBigElectronic.getId());
        updateBigElectronicDTO.setName(alteredName);
        
        assertThrows(IllegalArgumentException.class, () ->  bigElectronicController.updateApplianceDevice(updateBigElectronicDTO));
    }

    @Test
    void updateApplianceDeviceSpace() {
        addBeforeTest();
        String alteredName = " ";

        BigElectronicDTO updateBigElectronicDTO = bigElectronicDTO;
        updateBigElectronicDTO.setId(searchedBigElectronic.getId());
        updateBigElectronicDTO.setName(alteredName);

        assertThrows(IllegalArgumentException.class, () ->  bigElectronicController.updateApplianceDevice(updateBigElectronicDTO));
    }
    
    @Test
    void updateApplianceDeviceNameEmpty() {
        addBeforeTest();
        String alteredName = "";

        BigElectronicDTO updateBigElectronicDTO = bigElectronicDTO;
        updateBigElectronicDTO.setId(searchedBigElectronic.getId());
        updateBigElectronicDTO.setName(alteredName);

        assertThrows(IllegalArgumentException.class, () ->  bigElectronicController.updateApplianceDevice(updateBigElectronicDTO));
    }

    @Test
    void updateApplianceDeviceRoomIDNonExisting() {
        addBeforeTest();
        String alteredName = "Altered Name";

        BigElectronicDTO updateBigElectronicDTO = bigElectronicDTO;
        updateBigElectronicDTO.setId(searchedBigElectronic.getId());
        updateBigElectronicDTO.setName(alteredName);
        updateBigElectronicDTO.setRoomid(456945621);

        assertThrows(IllegalArgumentException.class, () ->  bigElectronicController.updateApplianceDevice(updateBigElectronicDTO));
    }
    

    @Test
    void getBigElectroniceByID() {
        addBeforeTest();
        
        assertEquals(bigElectronicDTO.getName(),bigElectronicController.getDeviceByid(searchedBigElectronic.getId()).getName());
    }

    @Test
    void getBigElectroniceByID0() {
        addBeforeTest();
        assertThrows(IllegalArgumentException.class, () -> bigElectronicController.getDeviceByid(0));
    }

    @Test
    void getBigElectroniceByIDNegative() {
        addBeforeTest();
        assertThrows(IllegalArgumentException.class, () -> bigElectronicController.getDeviceByid(-12));
    }

    @Test
    void getApplianceDevicesByRoom() {
        addBeforeTest();

        assertEquals(1, bigElectronicController.getApplianceDevicesByRoom(searchedRoom.getRoomID()).size());
        assertEquals(bigElectronicController.getApplianceDevicesByRoom(searchedRoom.getRoomID()).get(0).getName(), bigElectronicDTO.getName());
        assertEquals(bigElectronicController.getApplianceDevicesByRoom(searchedRoom.getRoomID()).get(0).getId(), searchedBigElectronic.getId());
    }

    @Test
    void deleteApplianceDeviceById() {
        addBeforeTest();

        bigElectronicController.deleteApplianceDeviceById(searchedBigElectronic.getId());
        assertEquals(0, bigElectronicController.getApplianceDevicesByRoom(searchedRoom.getRoomID()).size());
    }

    @Test
    void changeStatus() {
        addBeforeTest();

        bigElectronicController.changeStatus(searchedBigElectronic.getId());

        searchedBigElectronic = bigElectronicDAO.findAll().stream()
                .filter(p -> p.getName().equals(bigElectronicDTO.getName()))
                .findFirst()
                .get();

        assertTrue(searchedBigElectronic.isStatus());

        bigElectronicController.changeStatus(searchedBigElectronic.getId());

        searchedBigElectronic = bigElectronicDAO.findAll().stream()
                .filter(p -> p.getName().equals(bigElectronicDTO.getName()))
                .findFirst()
                .get();

        assertFalse(searchedBigElectronic.isStatus());
    }

    @Test
    void updateDeviceWithProgramme(){
        //TODO: Implement
        throw new NotImplementedException("TODO");
    }
}