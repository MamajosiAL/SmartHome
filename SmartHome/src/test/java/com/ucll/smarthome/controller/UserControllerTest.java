package com.ucll.smarthome.controller;

import com.ucll.smarthome.AbstractIntegrationTest;
import com.ucll.smarthome.dto.UserDTO;
import com.ucll.smarthome.persistence.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;


class UserControllerTest extends AbstractIntegrationTest {
    
    @Autowired
    private UserController userController;

    private UserDTO user1 = new UserDTO.Builder()
            .firstname("Testing")
            .name("User")
            .username("TestingUser1")
            .email("TestingUser@glock.com")
            .password("TUROX")
            .build();

    private long testUserId;

    @BeforeEach
    void setUp() {
        userController.createUser(user1);
        userController.getAllUsers().forEach(p -> {
                System.out.println("Test user created: id= " + (p.getId()));
                testUserId = p.getId();
        });
    }

    @Test
    void createUser() {
        UserDTO user2 = new UserDTO.Builder()
                .firstname("Testing2")
                .name("User2")
                .username("TestingUser2")
                .email("TestingUser@glock.com2")
                .password("TUROX2")
                .build();

        userController.createUser(user2);

        UserDTO user2Check = userController.getUserById(testUserId + 1);
        assertEquals(user2.getFirstname(), user2Check.getFirstname());
        assertEquals(user2.getName(), user2Check.getName());
        assertEquals(user2.getEmail(), user2Check.getEmail());
    }

    @Test
    void updateUser() {
        String alteredName = "TestingAltered";
        UserDTO updatedUser = userController.getUserById(testUserId);
        updatedUser.setFirstname(alteredName);

        userController.updateUser(updatedUser);

        UserDTO userUpdateCheck = userController.getUserById(testUserId);
        assertEquals(user1.getEmail(), userUpdateCheck.getEmail());
        assertNotEquals(user1.getFirstname(), userUpdateCheck.getFirstname());
        assertEquals(userUpdateCheck.getFirstname(), alteredName);
        assertEquals(user1.getUsername(), userUpdateCheck.getUsername());
    }

    @Test
    void getAllUsers() {
        assertEquals(1, userController.getAllUsers().size());
    }

    @Test
    void getUserById() {
         UserDTO userGetById = user1;

         userController.getUserById(testUserId);

         assertEquals(user1.getFirstname(), userGetById.getFirstname());
         assertEquals(user1.getUsername(), userGetById.getUsername());
         assertEquals(user1.getName(), userGetById.getName());
    }

    @Test
    void getUsersByHouse() {

    }

    @Test
    void deleteUser() {
        userController.deleteUser(testUserId);

        assertEquals(0, userController.getAllUsers().size());
    }
}