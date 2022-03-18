package com.ucll.smarthome.controllerREST;

import com.ucll.smarthome.AbstractIntegrationTest;
import com.ucll.smarthome.controller.UserController;
import com.ucll.smarthome.dto.HouseDTO;
import com.ucll.smarthome.dto.UserDTO;
import com.ucll.smarthome.dto.UserLoginDTO;
import com.ucll.smarthome.persistence.entities.House;
import com.ucll.smarthome.persistence.entities.User;
import com.ucll.smarthome.persistence.repository.HouseDAO;
import com.ucll.smarthome.persistence.repository.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserRestControllerTest extends AbstractIntegrationTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private HouseDAO houseDAO;

    @Autowired
    private UserController userController;

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

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/users")
                                .content(toJson(userDTO))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/houses")
                                .with(httpBasic(userDTO.getUsername(), userDTO.getPassword()))
                                .content(toJson(houseDTO))
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

        assertEquals(2, userController.getAllUsers().size());

        Optional<User> user = userDAO.findFirstByUsername(userDTO.getUsername());

        assertEquals(user.get().getUsername(), userDTO.getUsername());
        assertEquals(user.get().getName(), userDTO.getName());
        assertEquals(user.get().getFirstname(), userDTO.getFirstname());
        assertEquals(user.get().getEmail(), userDTO.getEmail());
    }

    @Test
    void updateUser() throws Exception {
        UserDTO userAltered = userDTO;
        userAltered.setFirstname("Updated");

        MvcResult mvcResult = this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/users")
                                .with(httpBasic(userDTO.getUsername(), userDTO.getPassword()))
                                .content(toJson(userAltered))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Optional<User> user = userDAO.findFirstByUsername(userDTO.getUsername());

        assertEquals(user.get().getFirstname(), userAltered.getFirstname());
    }

    @Test
    void getAllUsers() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/users")
                                .with(httpBasic(userDTO.getUsername(), userDTO.getPassword()))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<UserDTO> result = fromMvcResult(mvcResult, List.class);
        System.out.println(result.stream().findFirst().get());
        assertEquals(userDAO.findAll().size(), result.size());
    }

    @Test
    void getUsersByHouse() throws Exception {
        MvcResult mvcResultHouses = this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/houses")
                                .with(httpBasic(userDTO.getUsername(), userDTO.getPassword()))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<House> allHouses = houseDAO.findAll();
        House foundHouse = allHouses.stream().filter(p -> p.getName().equals(houseDTO.getName())).findFirst().get();

        MvcResult mvcResult = this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/house/" + foundHouse.getHouseId())
                                .with(httpBasic(userDTO.getUsername(), userDTO.getPassword()))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<UserDTO> result = fromMvcResult(mvcResult, List.class);

        assertTrue(result.contains(userDTO));
    }


    @Test
    @WithMockUser(value = "TestUser")
    void deleteUser() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
                        MockMvcRequestBuilders.delete("/user")
                                .with(httpBasic(userDTO.getUsername(), userDTO.getPassword()))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(0, userDAO.findAll().size());
    }
}