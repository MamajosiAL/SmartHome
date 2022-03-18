package com.ucll.smarthome.controller;

import com.ucll.smarthome.AbstractIntegrationTest;
import com.ucll.smarthome.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;

class LoginControllerTest extends AbstractIntegrationTest {

    @Autowired
    LoginController loginController;

    @Autowired
    UserController userController;

    private String username = "TestingUser1";
    private String password = "TUROX";

    private UserDTO user1 = new UserDTO.Builder()
            .firstname("Testing")
            .name("User")
            .username(username)
            .email("TestingUser@glock.com")
            .password(password)
            .build();

    @BeforeEach
    void setup() {
        userController.createUser(user1);
    }

    @Test
    void authentication() throws Exception {
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setUsername(username);
        userLoginDTO.setPassword(password);

        UserDetails userDetails = loginController.Authentication(userLoginDTO);

        assertDoesNotThrow(() -> loginController.Authentication(userLoginDTO));
        assertEquals(userDetails.getUsername(), username);
    }
}