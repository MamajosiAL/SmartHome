package com.ucll.smarthome.controllerREST;

import com.ucll.smarthome.AbstractIntegrationTest;
import com.ucll.smarthome.controller.UserController;
import com.ucll.smarthome.dto.UserDTO;
import com.ucll.smarthome.dto.UserLoginDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserRestControllerTest extends AbstractIntegrationTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private UserController userController;

    @Autowired
    private UserDetailsService userDetailsService;

    private MockMvc mockMvc;

    private UserDTO userDTO = new UserDTO.Builder()
            .firstname("Testing")
            .name("User")
            .username("TestUser")
            .email("TestingUser@lock.com")
            .password("TUROX")
            .build();

    private UserLoginDTO login = new UserLoginDTO();

    @BeforeEach
    void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .apply(springSecurity())
                .build();

        MvcResult mvcResult = this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/users")
                                .content(toJson(userDTO))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        login.setUsername("TestUser");
        login.setPassword("TUROX");
    }

    @Test
    void createUser() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/users")
                                .content(toJson(userDTO))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(1, userController.getAllUsers().size());
        assertEquals(userDetailsService.loadUserByUsername(userDTO.getUsername()).getUsername(), userDTO.getUsername());
    }

    @Test
    void updateUser() {
    }

    @Test
    void getAllUsers() {
    }

    @Test
    void getUsersByHouse() {
    }

    @Test
    void getUserById() {
    }

    @Test
    void deleteUser() {
    }
}