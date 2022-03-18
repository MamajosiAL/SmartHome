package com.ucll.smarthome.controller;

import com.ucll.smarthome.AbstractIntegrationTest;
import com.ucll.smarthome.dto.*;
import com.ucll.smarthome.persistence.entities.*;
import com.ucll.smarthome.persistence.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.testcontainers.shaded.org.apache.commons.lang.NotImplementedException;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, value = "TestUser", userDetailsServiceBeanName = "UserDetailService")
class ConsumptionControllerTest extends AbstractIntegrationTest {
    @Autowired
    private HouseController houseController;

    @Autowired
    private UserController userController;

    @Autowired
    private RoomController roomController;

    @Autowired
    private BigElectronicController bigElectronicController;

    @Autowired
    private ConsumptionController consumptionController;

    @Autowired
    private DeviceController deviceController;

    @Autowired
    private HouseDAO houseDAO;

    @Autowired
    private RoomDAO roomDAO;

    @Autowired
    private TypeDAO typeDAO;

    @Autowired
    private BigElectronicDAO bigElectronicDAO;

    @Autowired
    private ConsumptionDAO consumptionDAO;


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

    private RoomDTO testRoom = new RoomDTO.Builder()
            .name("testRoom")
            .build();

    private BigElectronicDTO bigElectronicDTO = new BigElectronicDTO.Builder()
            .name("testVaatwasser")
            .status(false)
            .build();


    private House searchedHouse;
    private Room searchedRoom;
    private BigElectronicDevice searchedBigElectronic;

    @BeforeEach
    void setUp() {
        userController.createUser(user1);
    }

    private void addBeforeTest(){
        houseController.createHouse(testHouse1);

        searchedHouse = houseDAO.findAll().stream()
                .filter(p-> p.getName().equals(testHouse1.getName()))
                .findFirst().get();

        testRoom.setHouseid(searchedHouse.getHouseId());
        roomController.createRoom(testRoom);

        searchedRoom = roomDAO.findAll().stream()
                .filter(p-> p.getName().equals(testRoom.getName()))
                .findFirst().get();

        Type vaatwasserType = typeDAO.findAll().stream()
                .filter(p -> p.getName().equals("Dishwasher"))
                .findFirst()
                .get();

        TypeDTO dto = new TypeDTO.Builder()
                .typeid(vaatwasserType.getTypeid())
                        .typeName(vaatwasserType.getName())
                                .build();


        bigElectronicDTO.setRoomid(searchedRoom.getRoomID());
        bigElectronicDTO.setType(dto);

        bigElectronicController.createApplianceDevice(bigElectronicDTO);

        searchedBigElectronic = bigElectronicDAO.findAll().stream()
                .filter(p -> p.getName().equals(bigElectronicDTO.getName()))
                .findFirst()
                .get();

        ConsumptionDTO createConsumptionDTO = new ConsumptionDTO.Builder()
                .device(searchedBigElectronic.getId())
                .unit("KwH")
                .build();

        consumptionController.createConsumption(createConsumptionDTO);

        System.out.println("House created name: " + searchedHouse.getName());
        System.out.println("Room created name: " + searchedRoom.getName());
        System.out.println("Big Electronic device created " + searchedBigElectronic.getName());
    }

    @Test
    void createConsumption() {
        addBeforeTest();

        ConsumptionDTO createConsumptionDTO = new ConsumptionDTO.Builder()
                .device(searchedBigElectronic.getId())
                        .unit("Liter")
                                .build();

        consumptionController.createConsumption(createConsumptionDTO);

        assertEquals(3, consumptionDAO.findAll().size());
    }

    @Test
    void deviceChangeStatusOneConsumptionInDevice() {
        addBeforeTest();

        deviceController.changeStatus(searchedBigElectronic.getId());

        assertTrue(bigElectronicDAO.findById(searchedBigElectronic.getId()).get().isStatus());

        deviceController.changeStatus(searchedBigElectronic.getId());

        assertFalse(bigElectronicDAO.findById(searchedBigElectronic.getId()).get().isStatus());

        List<Integer> timeActive = consumptionDAO.findAllByDevice(searchedBigElectronic).get().stream()
                .map(Consumption::getAantalMinuten)
                .collect(Collectors.toList());

        assertNotNull(timeActive.stream().findFirst().get());
        System.out.println("Time device was active: " + timeActive.stream().findFirst().get());
    }

    @Test
    void deviceChangeStatusTwoConsumptionInDevice() {
        addBeforeTest();

        ConsumptionDTO createConsumptionDTO = new ConsumptionDTO.Builder()
                .device(searchedBigElectronic.getId())
                .unit("Liter")
                .build();

        consumptionController.createConsumption(createConsumptionDTO);

        //Turn device on and then off
        deviceController.changeStatus(searchedBigElectronic.getId());
        assertTrue(bigElectronicDAO.findById(searchedBigElectronic.getId()).get().isStatus());
       deviceController.changeStatus(searchedBigElectronic.getId());
        assertFalse(bigElectronicDAO.findById(searchedBigElectronic.getId()).get().isStatus());

        List<Integer> timeActive = consumptionDAO.findAllByDevice(searchedBigElectronic).get().stream()
                .map(Consumption::getAantalMinuten)
                .collect(Collectors.toList());

        assertNotNull(timeActive.get(0));
        assertNotNull(timeActive.get(1));

        System.out.println("Time device was active: " + timeActive.get(0));
        System.out.println("Time device was active: " + timeActive.get(1));
    }

    @Test
    void getConsumptionsByDeviceId() {
        addBeforeTest();

        ConsumptionDTO createConsumptionDTO = new ConsumptionDTO.Builder()
                .device(searchedBigElectronic.getId())
                .unit("Liter")
                .build();

        consumptionController.createConsumption(createConsumptionDTO);

        //Turn device on and then off
        deviceController.changeStatus(searchedBigElectronic.getId());
        assertTrue(bigElectronicDAO.findById(searchedBigElectronic.getId()).get().isStatus());
        deviceController.changeStatus(searchedBigElectronic.getId());
        assertFalse(bigElectronicDAO.findById(searchedBigElectronic.getId()).get().isStatus());

        consumptionDAO.findAllByDevice(searchedBigElectronic).get().forEach(consumption -> {
            consumption.setAantalMinuten(20);
        });

        List<Integer> timeActive = consumptionDAO.findAllByDevice(searchedBigElectronic).get().stream()
                .map(Consumption::getAantalMinuten)
                .collect(Collectors.toList());

        List<ConsumptionDTO> allConsumptionFromDevice = consumptionController.getConsumptionsByDeviceId(searchedBigElectronic.getId());


        assertTrue(timeActive.get(0) > 0);
        assertTrue(timeActive.get(1) > 0);

        allConsumptionFromDevice.forEach(consumptionDTO ->{
            System.out.println("Consumption per hour: " + consumptionDTO.getConsumptionPerHour());
            System.out.println("Time in minutes: " + consumptionDTO.getAantalMinuten());
            System.out.println(consumptionDTO.getConsumptionPerHour() * consumptionDTO.getAantalMinuten());
        });

        allConsumptionFromDevice.forEach(consumptionDTO ->{
            System.out.println(consumptionDTO.toString());
        });

        System.out.println("Time device was active: " + timeActive.get(0));
        System.out.println("Time device was active: " + timeActive.get(1));
    }

    @Test
    void getConsumptionsByDevice() {
        addBeforeTest();

        ConsumptionDTO createConsumptionDTO = new ConsumptionDTO.Builder()
                .device(searchedBigElectronic.getId())
                .unit("Liter")
                .build();

        consumptionController.createConsumption(createConsumptionDTO);

        //Turn device on and then off
        deviceController.changeStatus(searchedBigElectronic.getId());
        assertTrue(bigElectronicDAO.findById(searchedBigElectronic.getId()).get().isStatus());
       deviceController.changeStatus(searchedBigElectronic.getId());
        assertFalse(bigElectronicDAO.findById(searchedBigElectronic.getId()).get().isStatus());

        consumptionDAO.findAllByDevice(searchedBigElectronic).get().forEach(consumption -> {
            consumption.setAantalMinuten(20);
        });

        List<Integer> timeActive = consumptionDAO.findAllByDevice(searchedBigElectronic).get().stream()
                .map(Consumption::getAantalMinuten)
                .collect(Collectors.toList());

        List<Consumption> allConsumptionFromDevice = consumptionController.getConsumptionsByDevice(searchedBigElectronic);


        assertTrue(timeActive.get(0) > 0);
        assertTrue(timeActive.get(1) > 0);

        allConsumptionFromDevice.forEach(consumptionDTO ->{
            System.out.println(consumptionDTO.getDevice().getId() + " " +  consumptionDTO.toString());
            System.out.println("Totale consumptie bedraagt: " + consumptionDTO.getConsumptionPerHour() * consumptionDTO.getAantalMinuten() + " " + consumptionDTO.getUnit());
        });

        System.out.println("Time device was active: " + timeActive.get(0));
        System.out.println("Time device was active: " + timeActive.get(1));
    }
}