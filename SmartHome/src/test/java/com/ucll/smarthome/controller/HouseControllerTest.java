package com.ucll.smarthome.controller;

import com.ucll.smarthome.AbstractIntegrationTest;
import com.ucll.smarthome.dto.HouseDTO;
import com.ucll.smarthome.dto.UserDTO;
import com.ucll.smarthome.functions.UserSecurityFunc;
import com.ucll.smarthome.persistence.entities.House;
import com.ucll.smarthome.persistence.entities.House_User;
import com.ucll.smarthome.persistence.entities.Room;
import com.ucll.smarthome.persistence.entities.User;
import com.ucll.smarthome.persistence.repository.HouseDAO;
import com.ucll.smarthome.persistence.repository.RoomDAO;
import com.ucll.smarthome.persistence.repository.UserDAO;
import com.vaadin.flow.router.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, value = "TestUser", userDetailsServiceBeanName = "UserDetailService")
class HouseControllerTest extends AbstractIntegrationTest {

    @Autowired
    private HouseController houseController;

    @Autowired
    private UserController userController;

    @Autowired
    private HouseDAO houseDAO;

    @Autowired
    private House_UserController house_userController;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RoomDAO roomDAO;

    @Autowired
    private UserSecurityFunc userSecurityFunc;

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

    House searchedHouse;

    @BeforeEach
    void setUp() {
        userController.createUser(user1);
    }

    private void addHouse(){
        houseController.createHouse(testHouse1);

        searchedHouse = houseDAO.findAll().stream()
                .filter(p -> p.getName().equals(testHouse1.getName()))
                .findFirst().get();
    }

    //Happy path create house
    @Test
    void createHouse() {
        HouseDTO newHouse = new HouseDTO.Builder()
                .name("newTestHouse")
                .build();

        houseController.createHouse(newHouse);

        List<String> namesFromHouses = houseController.getHousesByUser().stream()
                .map(HouseDTO::getName)
                        .collect(Collectors.toList());

        assertTrue(namesFromHouses.contains(newHouse.getName()));
    }

    @Test
    void createHouseNameNull() {
        addHouse();

        String houseName = null;
        HouseDTO searchedHouse = houseController.getAllHouses().stream().filter(p -> p.getName().equals(testHouse1.getName())).findFirst().get();

        HouseDTO dto = new HouseDTO.Builder().name(houseName)
                .id(searchedHouse.getId())
                .build();

        assertThrows(IllegalArgumentException.class, () -> houseController.createHouse(dto));
    }

    @Test
    void createHouseNameSpace() {
        addHouse();
        String houseName = " ";

        HouseDTO searchedHouse = houseController.getAllHouses().stream().filter(p -> p.getName().equals(testHouse1.getName())).findFirst().get();

        HouseDTO dto = new HouseDTO.Builder().name(houseName)
                .id(searchedHouse.getId())
                .build();

        assertThrows(IllegalArgumentException.class, () -> houseController.createHouse(dto));
    }

    @Test
    void createHouseNameEmpty() {
        addHouse();
        String houseName = "";

        HouseDTO searchedHouse = houseController.getAllHouses().stream().filter(p -> p.getName().equals(testHouse1.getName())).findFirst().get();

        HouseDTO dto = new HouseDTO.Builder().name(houseName)
                .id(searchedHouse.getId())
                .build();

        assertThrows(IllegalArgumentException.class, () -> houseController.createHouse(dto));
    }

    @Test
    void createHouseuserIDNegative() {
        addHouse();
        String houseName = "MyCrib";

        HouseDTO dto = new HouseDTO.Builder().name(houseName)
                .id(-99)
                .build();

        assertThrows(IllegalArgumentException.class, () -> houseController.createHouse(dto));
    }

    //Happy path update house
    @Test
    void updateHouse() {
        addHouse();

        HouseDTO searchedHouse = houseController.getAllHouses().stream().filter(p -> p.getName().equals(testHouse1.getName())).findFirst().get();
        System.out.println(searchedHouse.getName());

        HouseDTO alteredHouse = new HouseDTO.Builder()
                .id(searchedHouse.getId())
                .name("newTestHouse")
                .build();

        houseController.updateHouse(alteredHouse);

        assertEquals(alteredHouse.getName(), houseController.getHouseById(searchedHouse.getId()).getName());
        assertNotEquals(alteredHouse.getName(), testHouse1.getName());
    }

    @Test
    void updateHouseNameNull() {
        addHouse();

        String testHouseName = testHouse1.getName();
        String alteredHouseName = null;

        HouseDTO searchedHouse = houseController.getAllHouses().stream().filter(p -> p.getName().equals(testHouse1.getName())).findFirst().get();

        HouseDTO dto = new HouseDTO.Builder()
                .id(searchedHouse.getId())
                .name(alteredHouseName)
                .build();

        assertThrows(IllegalArgumentException.class,() -> houseController.updateHouse(dto));
    }

    @Test
    void updateHouseNameEmpty() {
        addHouse();

        String testHouseName = testHouse1.getName();
        String alteredHouseName = "";

        HouseDTO searchedHouse = houseController.getAllHouses().stream().filter(p -> p.getName().equals(testHouse1.getName())).findFirst().get();

        HouseDTO dto = new HouseDTO.Builder()
                .id(searchedHouse.getId())
                .name(alteredHouseName)
                .build();

        assertThrows(IllegalArgumentException.class,() -> houseController.updateHouse(dto));
    }

    @Test
    void updateHouseNameSpace() {
        addHouse();

        String testHouseName = testHouse1.getName();
        String alteredHouseName = " ";

        HouseDTO searchedHouse = houseController.getAllHouses().stream().filter(p -> p.getName().equals(testHouse1.getName())).findFirst().get();

        HouseDTO dto = new HouseDTO.Builder()
                .id(searchedHouse.getId())
                .name(alteredHouseName)
                .build();

        assertThrows(IllegalArgumentException.class,() -> houseController.updateHouse(dto));
    }

    @Test
    void updateHouseID0() {
        addHouse();

        String testHouseName = testHouse1.getName();
        String alteredHouseName = "MyCrib";

        HouseDTO dto = new HouseDTO.Builder()
                .id(0)
                .name(alteredHouseName)
                .build();

        assertThrows(IllegalArgumentException.class,() -> houseController.updateHouse(dto));
    }

    @Test
    void updateHouseIDNegative() {
        addHouse();

        String testHouseName = testHouse1.getName();
        String alteredHouseName = "MyCrib";

        HouseDTO dto = new HouseDTO.Builder()
                .id(-789)
                .name(alteredHouseName)
                .build();

        assertThrows(IllegalArgumentException.class,() -> houseController.updateHouse(dto));
    }



    @Test
    void getHouseById() {
        addHouse();

        House searechHouse = houseDAO.findAll().stream()
                .filter(p-> p.getName().equals(testHouse1.getName()))
                .findFirst().get();

        HouseDTO dto = houseController.getHouseById(searechHouse.getHouseId());

        assertEquals(dto.getName(), searechHouse.getName());
    }

    @Test
    void getHousesByUser() {
        addHouse();

        testHouse1.setId(searchedHouse.getHouseId());

        List<String> houseNames = houseController.getHousesByUser().stream()
                .map(HouseDTO::getName)
                .collect(Collectors.toList());

        assertTrue(houseNames.contains(testHouse1.getName()));
    }

    @Test
    void getAllHouses() {
        addHouse();
        addHouse();

        assertEquals(2, houseController.getAllHouses().size());
    }

    @Test
    void getUserByHouseNonExistingHouse(){
        assertThrows(IllegalArgumentException.class, () -> userController.getUsersByHouse(459789451));
    }

    @Test
    void deleteHouse() {
        addHouse();

        House searechHouse = houseDAO.findAll().stream()
                .filter(p-> p.getName().equals(testHouse1.getName()))
                .findFirst().get();

        houseController.deleteHouse(searechHouse.getHouseId());

        assertEquals(0, houseController.getAllHouses().size());
    }

    @Test
    void deleteUserFromHouse() {
        addHouse();

        UserDTO testUser2 = new UserDTO.Builder()
                .firstname("Testing2")
                .name("User2")
                .username("TestingUser2")
                .email("TestingUser@glock.com2")
                .password("TUROX2")
                .build();

        userController.createUser(testUser2);

        User searchedUser = userDAO.findFirstByUsername(testUser2.getUsername()).get();
        house_userController.createRegistratoinHouseUser(searchedHouse, searchedUser);

        assertEquals(2, userController.getUsersByHouse(searchedHouse.getHouseId()).size());

        House_User house_user = house_userController.getHouseUserByHouseAndUser(searchedHouse, searchedUser);
        house_userController.deleteSingleHouseUser(house_user);

        assertEquals(1, userController.getUsersByHouse(searchedHouse.getHouseId()).size());
    }

    @Test
    void deleteUserFromHouseAsOwner() {
        addHouse();

        UserDTO testUser2 = new UserDTO.Builder()
                .firstname("Testing2")
                .name("User2")
                .username("TestingUser2")
                .email("TestingUser@glock.com2")
                .password("TUROX2")
                .build();

        userController.createUser(testUser2);

        User searchedUser = userDAO.findFirstByUsername(testUser2.getUsername()).get();
        house_userController.createRegistratoinHouseUser(searchedHouse, searchedUser);

        assertEquals(2, userController.getUsersByHouse(searchedHouse.getHouseId()).size());

        houseController.deleteUserFromHouse(searchedHouse.getHouseId(), searchedUser.getId());

        assertEquals(1, userController.getUsersByHouse(searchedHouse.getHouseId()).size());
    }

}