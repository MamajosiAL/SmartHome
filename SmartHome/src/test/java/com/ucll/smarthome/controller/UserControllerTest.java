package com.ucll.smarthome.controller;

import com.ucll.smarthome.AbstractIntegrationTest;
import com.ucll.smarthome.dto.UserDTO;
import com.ucll.smarthome.functions.UserSecurityFunc;
import com.ucll.smarthome.persistence.entities.User;
import com.ucll.smarthome.persistence.repository.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;

import static org.junit.jupiter.api.Assertions.*;

@WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, value = "TestUser", userDetailsServiceBeanName = "UserDetailService")
class UserControllerTest extends AbstractIntegrationTest {

    @Autowired
    UserController userController;

    @Autowired
    UserDAO userDAO;

    @Autowired
    UserSecurityFunc userSecurityFunc;


    private UserDTO userDTO = new UserDTO.Builder()
            .firstname("Testing")
            .name("User")
            .username("TestUser")
            .email("TestingUser@lock.com")
            .password("TUROX")
            .build();
    
    private UserDTO user1 = new UserDTO.Builder()
            .firstname("Testing")
            .name("User")
            .username("TestUser")
            .email("TestingUser@lock.com")
            .password("TUROX")
            .build();

    @BeforeEach
    void setUp() {
        userController.createUser(userDTO);
    }

    @Test
    @WithAnonymousUser
    void createUser() {
        UserDTO testUser2 = new UserDTO.Builder()
                .firstname("Testing2")
                .name("User2")
                .username("TestingUser2")
                .email("TestingUser@glock.com2")
                .password("TUROX2")
                .build();

        userController.createUser(testUser2);

        assertEquals(userDAO.findFirstByUsername("TestingUser2").get().getUsername(), testUser2.getUsername());
        assertEquals(userDAO.findFirstByUsername("TestingUser2").get().getName(), testUser2.getName());
        assertEquals(userDAO.findFirstByUsername("TestingUser2").get().getFirstname(), testUser2.getFirstname());
        assertEquals(userDAO.findFirstByUsername("TestingUser2").get().getEmail(), testUser2.getEmail());
    }

    @Test
    @WithAnonymousUser
    void createUserWithoutName(){
        UserDTO userNameNull = user1;
        userNameNull.setName(null);

        assertThrows(IllegalArgumentException.class, () -> userController.createUser(userNameNull));
    }


    @Test
    @WithAnonymousUser
    void createUserWithoutFirstname(){
        UserDTO userFirstnameNull = user1;
        userFirstnameNull.setFirstname(null);

        assertThrows(IllegalArgumentException.class, () -> userController.createUser(userFirstnameNull));
    }

    @Test
    @WithAnonymousUser
    void createUserWithoutUsername(){
        UserDTO userUsernameNull = user1;
        userUsernameNull.setUsername(null);

        assertThrows(IllegalArgumentException.class, () -> userController.createUser(userUsernameNull));
    }

    @Test
    @WithAnonymousUser
    void createUserWithoutEmail(){
        UserDTO userEmailNull = user1;
        userEmailNull.setEmail(null);

        assertThrows(IllegalArgumentException.class, () -> userController.createUser(userEmailNull));
    }

    @Test
    @WithAnonymousUser
    void createUserWithoutPassword() {
        UserDTO userUsernameNull = user1;
        userUsernameNull.setPassword(null);

        assertThrows(IllegalArgumentException.class, () -> userController.createUser(userUsernameNull));
    }


    @Test
    @WithAnonymousUser
    void createUserWithNullValues() {
        assertThrows(IllegalArgumentException.class, () -> userController.createUser(null));
    }

    //create Users with spaces in values

    @Test
    @WithAnonymousUser
    void createUserWithSpacesName() {
        UserDTO userNameNull = user1;
        userNameNull.setName(" ");

        assertThrows(IllegalArgumentException.class, () -> userController.createUser(userNameNull));
    }


    @Test
    @WithAnonymousUser
    void createUserWithSpacesFirstname() {
        UserDTO userFirstnameNull = user1;
        userFirstnameNull.setFirstname(" ");

        assertThrows(IllegalArgumentException.class, () -> userController.createUser(userFirstnameNull));
    }

    @Test
    @WithAnonymousUser
    void createUserWithSpacesUsername() {
        UserDTO userUsernameNull = user1;
        userUsernameNull.setUsername(" ");

        assertThrows(IllegalArgumentException.class, () -> userController.createUser(userUsernameNull));
    }

    @Test
    @WithAnonymousUser
    void createUserWithSpacesEmail() {
        UserDTO userEmailNull = user1;
        userEmailNull.setEmail(" ");

        assertThrows(IllegalArgumentException.class, () -> userController.createUser(userEmailNull));
    }

    @Test
    @WithAnonymousUser
    void createUserWithSpacesPassword() {
        UserDTO userUsernameNull = user1;
        userUsernameNull.setPassword(" ");

        assertThrows(IllegalArgumentException.class, () -> userController.createUser(userUsernameNull));
    }

    //Create user with empty values
    @Test
    @WithAnonymousUser
    void createUserWithEmptyName() {
        UserDTO userNameEmpty = user1;
        userNameEmpty.setName("");

        assertThrows(IllegalArgumentException.class, () -> userController.createUser(userNameEmpty));
    }


    @Test
    @WithAnonymousUser
    void createUserWithEmptyFirstname() {
        UserDTO userFirstnameEmpty = user1;
        userFirstnameEmpty.setFirstname("");

        assertThrows(IllegalArgumentException.class, () -> userController.createUser(userFirstnameEmpty));
    }

    @Test
    @WithAnonymousUser
    void createUserWithEmptyUsername() {
        UserDTO userUsernameEmpty = user1;
        userUsernameEmpty.setUsername("");

        assertThrows(IllegalArgumentException.class, () -> userController.createUser(userUsernameEmpty));
    }

    @Test
    @WithAnonymousUser
    void createUserWithEmptyEmail() {
        UserDTO userEmailEmpty = user1;
        userEmailEmpty.setEmail("");

        assertThrows(IllegalArgumentException.class, () -> userController.createUser(userEmailEmpty));
    }

    @Test
    @WithAnonymousUser
    void createUserWithEmptyPassword() {
        UserDTO userUsernameEmpty = user1;
        userUsernameEmpty.setPassword("");

        assertThrows(IllegalArgumentException.class, () -> userController.createUser(userUsernameEmpty));
    }
    
    @Test
    void updateUser() {
        UserDTO dto = userDTO;
        dto.setId(userSecurityFunc.getLoggedInUserId());
        dto.setFirstname("Updated");

        userController.updateUser(dto);
        UserDTO loggedInUser = userController.getUserById(userSecurityFunc.getLoggedInUserId());
        assertEquals(loggedInUser.getFirstname(), userDTO.getFirstname());
        assertNotEquals(loggedInUser.getFirstname(), user1.getFirstname());
    }

    @Test
    void updateUserWithDifferentID(){
        UserDTO dto = userDTO;
        dto.setId(2);
        dto.setFirstname("Updated");


        userController.updateUser(dto);
        UserDTO loggedInUser = userController.getUserById(userSecurityFunc.getLoggedInUserId());
        assertEquals(loggedInUser.getFirstname(), userDTO.getFirstname());
        assertNotEquals(loggedInUser.getFirstname(), user1.getFirstname());
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

    //Get Methodes

    @Test
    void getAllUsers() {
        assertEquals(1, userController.getAllUsers().size());
    }

    @Test
    void getUserById() {
        UserDTO userGetById = userController.getUserById(userSecurityFunc.getLoggedInUserId());

        assertEquals(user1.getFirstname(), userGetById.getFirstname());
        assertEquals(user1.getUsername(), userGetById.getUsername());
        assertEquals(user1.getName(), userGetById.getName());
        assertEquals(user1.getEmail(), userGetById.getEmail());
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
    void getUserTest(){
        UserDTO dto = userController.getUser();

        assertEquals(dto.getName(), user1.getName());
        assertEquals(dto.getUsername(), user1.getUsername());
        assertEquals(dto.getFirstname(), user1.getFirstname());
        assertEquals(dto.getEmail(), user1.getEmail());
    }

    @Test
    void getUserByIdNonExistingUser(){
        assertThrows(IllegalArgumentException.class, () -> userController.getUserById(45649849));
    }

    //Delete Mehodes
    @Test
    void deleteUser() {
        userController.deleteUser();

        assertEquals(0, userDAO.findAll().size());
    }
}