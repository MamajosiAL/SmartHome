package com.ucll.smarthome.controller;

import com.ucll.smarthome.AbstractIntegrationTest;
import com.ucll.smarthome.dto.UserDTO;
import com.ucll.smarthome.persistence.repository.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;

import static org.junit.jupiter.api.Assertions.*;

@WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, value = "TestUser", userDetailsServiceBeanName = "UserDetailService")
class UserControllerTest extends AbstractIntegrationTest {

    @Autowired
    UserController userController;

    @Autowired
    UserDAO userDAO;


    private UserDTO userDTO = new UserDTO.Builder()
            .firstname("Testing")
            .name("User")
            .username("TestUser")
            .email("TestingUser@lock.com")
            .password("TUROX")
            .build();

    @BeforeEach
    void setUp() {
        userController.createUser(userDTO);
        userDAO.findAll().forEach(p -> System.out.println(p.getUsername()));
    }

    @Test
    void createUser() {
        userDAO.findAll().forEach(p -> System.out.println(p.getUsername()));
    }

    @Test
    void updateUser() {
        UserDTO dto = userDTO;
        dto.setId(1);
        dto.setFirstname("Updated");

        userController.updateUser(dto);

        userDAO.findAll().forEach(p -> System.out.println(p.getUsername()));
    }

    @Test
    void getAllUsers() {
    }

    @Test
    void getUserById() {
    }

    @Test
    void getUsersByHouse() {
    }

    @Test
    void deleteUser() {
    }
}