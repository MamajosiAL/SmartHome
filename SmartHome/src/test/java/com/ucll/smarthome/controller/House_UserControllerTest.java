package com.ucll.smarthome.controller;

import com.ucll.smarthome.AbstractIntegrationTest;
import com.ucll.smarthome.dto.HouseDTO;
import com.ucll.smarthome.dto.House_UserDTO;
import com.ucll.smarthome.dto.UserDTO;
import com.ucll.smarthome.functions.UserSecurityFunc;
import com.ucll.smarthome.persistence.entities.House;
import com.ucll.smarthome.persistence.entities.House_User;
import com.ucll.smarthome.persistence.entities.User;
import com.ucll.smarthome.persistence.repository.HouseDAO;
import com.ucll.smarthome.persistence.repository.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.testcontainers.shaded.org.apache.commons.lang.NotImplementedException;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, value = "TestUser", userDetailsServiceBeanName = "UserDetailService")
class House_UserControllerTest extends AbstractIntegrationTest {

    @Autowired
    private HouseController houseController;

    @Autowired
    private UserController userController;

    @Autowired
    private House_UserController house_userController;

    @Autowired
    private HouseDAO houseDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserSecurityFunc userSecurityFunc;

    private UserDTO user1 = new UserDTO.Builder()
            .firstname("Testing")
            .name("User")
            .username("TestUser")
            .email("TestingUser@glock.com")
            .password("TUROX")
            .build();

    private UserDTO user2 = new UserDTO.Builder()
            .firstname("Testing")
            .name("User")
            .username("TestUser2")
            .email("TestingUser@glock.com")
            .password("TUROX")
            .build();

    private HouseDTO testHouse1 = new HouseDTO.Builder()
            .name("testHouse")
            .build();

    private House searchedHouse;
    private User user2info;

    @BeforeEach
    void setUp() {
        userController.createUser(user1);
        userController.createUser(user2);
    }

    private void addBeforeTest(){
        houseController.createHouse(testHouse1);
        searchedHouse = houseDAO.findAll().stream()
                .filter(p -> p.getName().equals(testHouse1.getName()))
                .findFirst()
                .get();

        user2info = userDAO.findAll().stream()
                .filter(p -> p.getUsername().equals(user2.getUsername()))
                .findFirst()
                .get();

        System.out.println("User 2 id: " + user2info.getId());
    }

    @Test
    void registerUserToHouseNotOwner() {
        addBeforeTest();

        HouseDTO registerUser = new HouseDTO.Builder()
                .id(searchedHouse.getHouseId())
                .username(user2info.getUsername())
                .build();

        house_userController.registerUserToHouseNotOwner(registerUser);

        assertEquals(2, userController.getUsersByHouse(searchedHouse.getHouseId()).size());
    }

    @Test
    void updateRegistrationHouseUsser() {
        addBeforeTest();

        HouseDTO registerUser = new HouseDTO.Builder()
                .id(searchedHouse.getHouseId())
                .username(user2info.getUsername())
                .build();

        house_userController.registerUserToHouseNotOwner(registerUser);

        House_User hs = house_userController.getHouseUserByHouseAndUser(searchedHouse, user2info);

        House_UserDTO house_userDTO = new House_UserDTO.Builder()
                .houseid(hs.getHouse().getHouseId())
                .isadmin(true)
                .build();

        assertDoesNotThrow(() -> house_userController.updateUserSetAdmin(house_userDTO));
        throw new NotImplementedException("Method updates logged in user");
    }

    @Test
    void getHouseUserByHouseIdAndUserId() {
        addBeforeTest();

        HouseDTO registerUser = new HouseDTO.Builder()
                .id(searchedHouse.getHouseId())
                .username(user2info.getUsername())
                .build();

        house_userController.registerUserToHouseNotOwner(registerUser);

        assertDoesNotThrow(() -> house_userController.getHouseUserByHouseIdAndUserId(searchedHouse.getHouseId(), user2info.getId()));
    }
}