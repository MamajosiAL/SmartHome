package com.ucll.smarthome.controllerREST;

import com.ucll.smarthome.AbstractIntegrationTest;
import com.ucll.smarthome.controller.UserController;
import com.ucll.smarthome.dto.HouseDTO;
import com.ucll.smarthome.dto.UserDTO;
import com.ucll.smarthome.dto.UserLoginDTO;
import com.ucll.smarthome.persistence.entities.House;
import com.ucll.smarthome.persistence.repository.HouseDAO;
import com.ucll.smarthome.persistence.repository.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.processor.core.AbstractInputValueSwitch;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class HouseRestControllerTest extends AbstractIntegrationTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private HouseDAO houseDAO;

    @Autowired
    private UserDAO userDAO;

    private MockMvc mockMvc;

    private UserDTO userDTO = new UserDTO.Builder()
            .firstname("Testing")
            .name("User")
            .username("TestUser")
            .email("TestingUser@lock.com")
            .password("TUROX")
            .build();

    private HouseDTO houseDTO = new HouseDTO.Builder()
            .name("Testhousel")
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

    //Test Create house OK
    @Test
    void createHouse() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/houses")
                                .with(httpBasic(userDTO.getUsername(), userDTO.getPassword()))
                                .content(toJson(houseDTO))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<House> allhouse =  houseDAO.findAll();
        assertTrue(allhouse.stream().anyMatch(p -> p.getName().equals(houseDTO.getName())));
    }

    @Test
    void updateUseIsAdmin() {
    }

    @Test
    void updateHouse() {
    }

    @Test
    void getAllHouses() {
    }

    @Test
    void getHousesByUser() {
    }

    @Test
    void getHouseById() {
    }

    @Test
    void deleteHouse() {
    }

    @Test
    void deleteUserFromHouse() {
    }
}