package com.ucll.smarthome.controller;

import com.ucll.smarthome.AbstractIntegrationTest;
import com.ucll.smarthome.dto.HouseDTO;
import com.ucll.smarthome.dto.UserDTO;
import com.ucll.smarthome.persistence.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class UserControllerTest extends AbstractIntegrationTest {
    
    @Autowired
    private UserController userController;

    @Autowired
    HouseController houseController;

    private UserDTO user1 = new UserDTO.Builder()
            .firstname("Testing")
            .name("User")
            .username("TestingUser1")
            .email("TestingUser@glock.com")
            .password("TUROX")
            .build();

    private HouseDTO testHouse1 = new HouseDTO.Builder()
            .name("testHouse")
            .build();

    private long testUserId;
    private long testHouseId;

    @BeforeEach
    void setUp() {
        userController.createUser(user1);
        userController.getAllUsers().forEach(p -> {
                System.out.println("Test user created: id= " + (p.getId()));
                testUserId = p.getId();
        });

        testHouse1.setUserid(testUserId);
        houseController.createHouse(testHouse1);
        houseController.getAllHouses().forEach(p -> {
            System.out.println("Test house created: id = " + (p.getId()));
            testHouseId = p.getId();
        });
    }

    //Happy path create user 
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
    
    //Create Users without values
    @Test
    void createUserWithoutName(){
        UserDTO userNameNull = user1;
        userNameNull.setName(null);

        assertThrows(IllegalArgumentException.class, () -> userController.createUser(userNameNull));
    }


    @Test
    void createUserWithoutFirstname(){
        UserDTO userFirstnameNull = user1;
        userFirstnameNull.setFirstname(null);

        assertThrows(IllegalArgumentException.class, () -> userController.createUser(userFirstnameNull));
    }

    @Test
    void createUserWithoutUsername(){
        UserDTO userUsernameNull = user1;
        userUsernameNull.setUsername(null);

        assertThrows(IllegalArgumentException.class, () -> userController.createUser(userUsernameNull));
    }

    @Test
    void createUserWithoutEmail(){
        UserDTO userEmailNull = user1;
        userEmailNull.setEmail(null);

        assertThrows(IllegalArgumentException.class, () -> userController.createUser(userEmailNull));
    }

    @Test
    void createUserWithoutPassword(){
        UserDTO userUsernameNull = user1;
        userUsernameNull.setPassword(null);

        assertThrows(IllegalArgumentException.class, () -> userController.createUser(userUsernameNull));
    }
    

    @Test
    void createUserWithNullValues(){
        assertThrows(IllegalArgumentException.class, () -> userController.createUser(null));
    }

    //create Users with spaces in values

    @Test
    void createUserWithSpacesName(){
        UserDTO userNameNull = user1;
        userNameNull.setName(" ");

        assertThrows(IllegalArgumentException.class, () -> userController.createUser(userNameNull));
    }


    @Test
    void createUserWithSpacesFirstname(){
        UserDTO userFirstnameNull = user1;
        userFirstnameNull.setFirstname(" ");

        assertThrows(IllegalArgumentException.class, () -> userController.createUser(userFirstnameNull));
    }

    @Test
    void createUserWithSpacesUsername(){
        UserDTO userUsernameNull = user1;
        userUsernameNull.setUsername(" ");

        assertThrows(IllegalArgumentException.class, () -> userController.createUser(userUsernameNull));
    }

    @Test
    void createUserWithSpacesEmail(){
        UserDTO userEmailNull = user1;
        userEmailNull.setEmail(" ");

        assertThrows(IllegalArgumentException.class, () -> userController.createUser(userEmailNull));
    }

    @Test
    void createUserWithSpacesPassword(){
        UserDTO userUsernameNull = user1;
        userUsernameNull.setPassword(" ");

        assertThrows(IllegalArgumentException.class, () -> userController.createUser(userUsernameNull));
    }
    
    //Create user with empty values
    @Test
    void createUserWithEmptyName(){
        UserDTO userNameEmpty = user1;
        userNameEmpty.setName("");

        assertThrows(IllegalArgumentException.class, () -> userController.createUser(userNameEmpty));
    }


    @Test
    void createUserWithEmptyFirstname(){
        UserDTO userFirstnameEmpty = user1;
        userFirstnameEmpty.setFirstname("");

        assertThrows(IllegalArgumentException.class, () -> userController.createUser(userFirstnameEmpty));
    }

    @Test
    void createUserWithEmptyUsername(){
        UserDTO userUsernameEmpty = user1;
        userUsernameEmpty.setUsername("");

        assertThrows(IllegalArgumentException.class, () -> userController.createUser(userUsernameEmpty));
    }

    @Test
    void createUserWithEmptyEmail(){
        UserDTO userEmailEmpty = user1;
        userEmailEmpty.setEmail("");

        assertThrows(IllegalArgumentException.class, () -> userController.createUser(userEmailEmpty));
    }

    @Test
    void createUserWithEmptyPassword(){
        UserDTO userUsernameEmpty = user1;
        userUsernameEmpty.setPassword("");

        assertThrows(IllegalArgumentException.class, () -> userController.createUser(userUsernameEmpty));
    }
    
    //Happy path update User
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
    
    //Update user with null values
    
    @Test
    void updateUserWithoutName(){
        UserDTO userNameNull = user1;
        userNameNull.setName(null);

        assertThrows(IllegalArgumentException.class, () -> userController.updateUser(userNameNull));
    }


    @Test
    void updateUserWithoutFirstname(){
        UserDTO userFirstnameNull = user1;
        userFirstnameNull.setFirstname(null);

        assertThrows(IllegalArgumentException.class, () -> userController.updateUser(userFirstnameNull));
    }

    @Test
    void updateUserWithoutUsername(){
        UserDTO userUsernameNull = user1;
        userUsernameNull.setUsername(null);

        assertThrows(IllegalArgumentException.class, () -> userController.updateUser(userUsernameNull));
    }

    @Test
    void updateUserWithoutEmail(){
        UserDTO userEmailNull = user1;
        userEmailNull.setEmail(null);

        assertThrows(IllegalArgumentException.class, () -> userController.updateUser(userEmailNull));
    }

    @Test
    void updateUserWithoutPassword(){
        UserDTO userUsernameNull = user1;
        userUsernameNull.setPassword(null);

        assertThrows(IllegalArgumentException.class, () -> userController.updateUser(userUsernameNull));
    }


    @Test
    void updateUserWithNullValues(){
        assertThrows(IllegalArgumentException.class, () -> userController.updateUser(null));
    }
    
    //Update User with spaces in values

    @Test
    void updateUserWithSpacesName(){
        UserDTO userNameSpaces = user1;
        userNameSpaces.setName(" ");

        assertThrows(IllegalArgumentException.class, () -> userController.updateUser(userNameSpaces));
    }


    @Test
    void updateUserWithSpacesFirstname(){
        UserDTO userFirstnameSpaces = user1;
        userFirstnameSpaces.setFirstname(" ");

        assertThrows(IllegalArgumentException.class, () -> userController.updateUser(userFirstnameSpaces));
    }

    @Test
    void updateUserWithSpacesUsername(){
        UserDTO userUsernameSpaces = user1;
        userUsernameSpaces.setUsername(" ");

        assertThrows(IllegalArgumentException.class, () -> userController.updateUser(userUsernameSpaces));
    }

    @Test
    void updateUserWithSpacesEmail(){
        UserDTO userEmailSpaces = user1;
        userEmailSpaces.setEmail(" ");

        assertThrows(IllegalArgumentException.class, () -> userController.updateUser(userEmailSpaces));
    }

    @Test
    void updateUserWithSpacesPassword(){
        UserDTO userUsernameSpaces = user1;
        userUsernameSpaces.setPassword(" ");

        assertThrows(IllegalArgumentException.class, () -> userController.updateUser(userUsernameSpaces));
    }
    
    //Update user with empty values
    @Test
    void updateUserEmptyName(){
        UserDTO userNameEmpty = user1;
        userNameEmpty.setName("");

        assertThrows(IllegalArgumentException.class, () -> userController.updateUser(userNameEmpty));
    }


    @Test
    void updateUserEmptyFirstname(){
        UserDTO userFirstnameEmpty = user1;
        userFirstnameEmpty.setFirstname("");

        assertThrows(IllegalArgumentException.class, () -> userController.updateUser(userFirstnameEmpty));
    }

    @Test
    void updateUserEmptyUsername(){
        UserDTO userUsernameEmpty = user1;
        userUsernameEmpty.setUsername("");

        assertThrows(IllegalArgumentException.class, () -> userController.updateUser(userUsernameEmpty));
    }

    @Test
    void updateUserEmptyEmail(){
        UserDTO userEmailEmpty = user1;
        userEmailEmpty.setEmail("");

        assertThrows(IllegalArgumentException.class, () -> userController.updateUser(userEmailEmpty));
    }

    @Test
    void updateUserEmptyPassword(){
        UserDTO userUsernameEmpty = user1;
        userUsernameEmpty.setPassword("");

        assertThrows(IllegalArgumentException.class, () -> userController.updateUser(userUsernameEmpty));
    }
    
    // Get methodes
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
    void getUserByIdZero(){
        assertThrows(IllegalArgumentException.class, () -> userController.getUserById(0));
    }

    @Test
    void getUserByIdNegative(){
        assertThrows(IllegalArgumentException.class, () -> userController.getUserById(-42));
    }

    @Test
    void getUsersByHouse() {
        //TODO indien users aan houses kunnen worden toegevoegd een tweede user toevoegen aan house

        UserDTO testuser2 = new UserDTO.Builder()
                .firstname("Testing2")
                .name("User2")
                .username("TestingUser2")
                .email("TestingUser2@glock.com")
                .password("TU2ROXMORE")
                .build();

        userController.createUser(testuser2);

        List<UserDTO> users = userController.getUsersByHouse(testHouseId);
        assertTrue(users.contains(user1));
    }
    
    // Delete methode 
    @Test
    void deleteUser() {
        userController.deleteUser(testUserId);
        assertEquals(0, userController.getAllUsers().size());
    }
}