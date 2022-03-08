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

@WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, value = "TestUser", userDetailsServiceBeanName = "UserDetailService")
class AppliancesControllerTest extends AbstractIntegrationTest {

    @Autowired
    private HouseController houseController;

    @Autowired
    private UserController userController;

    @Autowired
    private RoomController roomController;

    @Autowired
    private AppliancesController appliancesController;

    @Autowired
    private HouseDAO houseDAO;

    @Autowired
    private RoomDAO roomDAO;

    @Autowired
    private TypeDAO typeDAO;

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

    private House searchedHouse;
    private Room searchedRoom;

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

        System.out.println("House created name: " + searchedHouse.getName());
        System.out.println("House created name: " + searchedRoom.getName());
    }

    @Test
    void createApplianceDevice() {
        addBeforeTest();

        AppliancesDTO appliancesDTO = new AppliancesDTO.Builder()
                .name("TestVaatwasser")
                .roomid(searchedRoom.getRoomID())
                .build();

        appliancesController.createApplianceDevice(appliancesDTO);
    }

    @Test
    void updateApplianceDevice() {
        throw new NotImplementedException("Still needs implementation");
    }

    @Test
    void getDeviceByid() {
        throw new NotImplementedException("Still needs implementation");
    }

    @Test
    void getApplianceDevicesByRoom() {
        throw new NotImplementedException("Still needs implementation");
    }

    @Test
    void deleteApplianceDeviceById() {
        throw new NotImplementedException("Still needs implementation");
    }

    @Test
    void changeStatus() {
        throw new NotImplementedException("Still needs implementation");
    }
}