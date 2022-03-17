package com.ucll.smarthome.controller;

import com.ucll.smarthome.AbstractIntegrationTest;
import com.ucll.smarthome.dto.HouseDTO;
import com.ucll.smarthome.dto.MediaDTO;
import com.ucll.smarthome.dto.RoomDTO;
import com.ucll.smarthome.dto.UserDTO;
import com.ucll.smarthome.persistence.entities.House;
import com.ucll.smarthome.persistence.entities.MediaDevice;
import com.ucll.smarthome.persistence.entities.Room;
import com.ucll.smarthome.persistence.repository.HouseDAO;
import com.ucll.smarthome.persistence.repository.MediaDAO;
import com.ucll.smarthome.persistence.repository.RoomDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;

import javax.persistence.DiscriminatorValue;

import static org.junit.jupiter.api.Assertions.*;

@WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, value = "TestUser", userDetailsServiceBeanName = "UserDetailService")
class MediaControllerTest extends AbstractIntegrationTest {
    @Autowired
    private HouseController houseController;

    @Autowired
    private UserController userController;

    @Autowired
    private RoomController roomController;

    @Autowired
    private MediaController mediaController;

    @Autowired
    private HouseDAO houseDAO;

    @Autowired
    private RoomDAO roomDAO;

    @Autowired
    private MediaDAO mediaDAO;

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

    private MediaDTO mediaDTO = new MediaDTO.Builder()
            .name("SpeakersWoonkamer")
            .status(false)
            .volume(10)
            .build();

    private House searchedHouse;
    private Room searchedRoom;
    private MediaDevice mediaDevice;

    @BeforeEach
    void setUp() {
        userController.createUser(user1);
    }

    private void addBeforeTest() {
        houseController.createHouse(testHouse1);

        searchedHouse = houseDAO.findAll().stream()
                .filter(p -> p.getName().equals(testHouse1.getName()))
                .findFirst().get();

        testRoom.setHouseid(searchedHouse.getHouseId());
        roomController.createRoom(testRoom);

        searchedRoom = roomDAO.findAll().stream()
                .filter(p -> p.getName().equals(testRoom.getName()))
                .findFirst().get();

        mediaDTO.setRoomid(searchedRoom.getRoomID());
        mediaController.createAudioDevice(mediaDTO);

        mediaDevice = mediaDAO.findAllByRoom(searchedRoom).stream().findFirst().get();

        System.out.println("House created name: " + searchedHouse.getName());
        System.out.println("Room created name: " + searchedRoom.getName());
        System.out.println("Audio device created name: " + mediaDevice.getName());
    }

    @Test
    void createAudioDevice() {
        addBeforeTest();

        MediaDTO createMediaDTO = new MediaDTO.Builder()
                .name("SpeakersWoonkamer")
                .status(false)
                .volume(10)
                .roomid(searchedRoom.getRoomID())
                .build();

        mediaController.createAudioDevice(createMediaDTO);

        assertEquals(mediaDAO.findAll().size(), 2);
    }

    @Test
    void createAudioDeviceNull() {
        addBeforeTest();

        MediaDTO createMediaDTO = null;
        assertThrows(IllegalArgumentException.class, () -> mediaController.createAudioDevice(createMediaDTO));
    }

    @Test
    void createAudioDeviceNameSpace() {
        addBeforeTest();

        MediaDTO createMediaDTO = new MediaDTO.Builder()
                .name("")
                .status(false)
                .volume(10)
                .roomid(searchedRoom.getRoomID())
                .build();

        assertThrows(IllegalArgumentException.class, () -> mediaController.createAudioDevice(createMediaDTO));
    }

    @Test
    void createAudioDeviceNameEmpty() {
        addBeforeTest();

        MediaDTO createMediaDTO = new MediaDTO.Builder()
                .name("")
                .status(false)
                .volume(10)
                .roomid(searchedRoom.getRoomID())
                .build();

        assertThrows(IllegalArgumentException.class, () -> mediaController.createAudioDevice(createMediaDTO));
    }

    @Test
    void createAudioDeviceNameNull() {
        addBeforeTest();

        MediaDTO createMediaDTO = new MediaDTO.Builder()
                .name(null)
                .status(false)
                .volume(10)
                .roomid(searchedRoom.getRoomID())
                .build();

        assertThrows(IllegalArgumentException.class, () -> mediaController.createAudioDevice(createMediaDTO));
    }

    @Test
    void createAudioDeviceStatusEmpty() {
        addBeforeTest();

        MediaDTO createMediaDTO = new MediaDTO.Builder()
                .name("SpeakerWoonKamer")
                .volume(10)
                .roomid(searchedRoom.getRoomID())
                .build();

        assertThrows(IllegalArgumentException.class, () -> mediaController.createAudioDevice(createMediaDTO));
    }

    @Test
    void createAudioDeviceVolumeNegative() {
        addBeforeTest();

        MediaDTO createMediaDTO = new MediaDTO.Builder()
                .name("SpeakerWoonKamer")
                .status(false)
                .volume(-10)
                .roomid(searchedRoom.getRoomID())
                .build();

        assertThrows(IllegalArgumentException.class, () -> mediaController.createAudioDevice(createMediaDTO));
    }

    @Test
    void createAudioDeviceVolumeNull() {
        addBeforeTest();

        MediaDTO createMediaDTO = new MediaDTO.Builder()
                .name("SpeakerWoonKamer")
                .status(false)
                .roomid(searchedRoom.getRoomID())
                .build();

        assertThrows(IllegalArgumentException.class, () -> mediaController.createAudioDevice(createMediaDTO));
    }

    @Test
    void createAudioDeviceRoomIdNull() {
        addBeforeTest();

        MediaDTO createMediaDTO = new MediaDTO.Builder()
                .name("SpeakerWoonKamer")
                .status(false)
                .volume(10)
                .build();

        assertThrows(IllegalArgumentException.class, () -> mediaController.createAudioDevice(createMediaDTO));
    }

    @Test
    void updateAudioDevice() {
        addBeforeTest();

        String alteredName = "AlteredSpeakerWoonKamer";

        MediaDTO updateaudioDTO = new MediaDTO.Builder()
                .id(mediaDevice.getId())
                .name(alteredName)
                .status(false)
                .volume(10)
                .roomid(searchedRoom.getRoomID())
                .build();

        mediaController.updateAudioDevice(updateaudioDTO);

        mediaDevice = mediaDAO.findAllByRoom(searchedRoom).stream().findFirst().get();

        assertEquals(mediaDevice.getName(), updateaudioDTO.getName());
        assertNotEquals(mediaDevice.getName(), mediaDTO.getName());
    }

    @Test
    void updateAudioDeviceNameNull() {
        addBeforeTest();

        MediaDTO updateaudioDTO = new MediaDTO.Builder()
                .id(mediaDevice.getId())
                .name(null)
                .status(false)
                .volume(10)
                .roomid(searchedRoom.getRoomID())
                .build();

        assertThrows(IllegalArgumentException.class, () -> mediaController.updateAudioDevice(updateaudioDTO));
    }

    @Test
    void updateAudioDeviceNameSpace() {
        addBeforeTest();

        MediaDTO updateaudioDTO = new MediaDTO.Builder()
                .id(mediaDevice.getId())
                .name(" ")
                .status(false)
                .volume(10)
                .roomid(searchedRoom.getRoomID())
                .build();

        assertThrows(IllegalArgumentException.class, () -> mediaController.updateAudioDevice(updateaudioDTO));
    }

    @Test
    void updateAudioDeviceNameEmpty() {
        addBeforeTest();

        MediaDTO updateaudioDTO = new MediaDTO.Builder()
                .id(mediaDevice.getId())
                .name("")
                .status(false)
                .volume(10)
                .roomid(searchedRoom.getRoomID())
                .build();

        assertThrows(IllegalArgumentException.class, () -> mediaController.updateAudioDevice(updateaudioDTO));
    }

    @Test
    void updateAudioDeviceStatusEmpty() {
        addBeforeTest();

        String alteredName = "AlteredSpeakerWoonKamer";

        MediaDTO updateaudioDTO = new MediaDTO.Builder()
                .id(mediaDevice.getId())
                .name(alteredName)
                .volume(10)
                .roomid(searchedRoom.getRoomID())
                .build();

        assertThrows(IllegalArgumentException.class, () -> mediaController.updateAudioDevice(updateaudioDTO));
    }

    @Test
    void updateAudioDeviceNegativeVolume() {
        addBeforeTest();

        String alteredName = "AlteredSpeakerWoonKamer";

        MediaDTO updateaudioDTO = new MediaDTO.Builder()
                .id(mediaDevice.getId())
                .name(alteredName)
                .volume(-10)
                .roomid(searchedRoom.getRoomID())
                .build();

        assertThrows(IllegalArgumentException.class, () -> mediaController.updateAudioDevice(updateaudioDTO));
    }

    @Test
    void updateAudioDeviceId0() {
        addBeforeTest();

        String alteredName = "AlteredSpeakerWoonKamer";

        MediaDTO updateaudioDTO = new MediaDTO.Builder()
                .id(0)
                .name(alteredName)
                .volume(-10)
                .roomid(searchedRoom.getRoomID())
                .build();

        assertThrows(IllegalArgumentException.class, () -> mediaController.updateAudioDevice(updateaudioDTO));
    }

    @Test
    void updateAudioDeviceIdRoomID0() {
        addBeforeTest();

        String alteredName = "AlteredSpeakerWoonKamer";

        MediaDTO updateaudioDTO = new MediaDTO.Builder()
                .id(mediaDevice.getId())
                .name(alteredName)
                .volume(-10)
                .roomid(0)
                .build();

        assertThrows(IllegalArgumentException.class, () -> mediaController.updateAudioDevice(updateaudioDTO));
    }

    @Test
    void getAudioDeviceById() {
        addBeforeTest();
        assertEquals(mediaDevice.getName(), mediaController.getAudioDeviceById(mediaDevice.getId()).getName());
    }

    @Test
    void getAudioDeviceById0() {
        addBeforeTest();
        assertEquals(mediaDevice.getName(), mediaController.getAudioDeviceById(mediaDevice.getId()).getName());

        assertThrows(IllegalArgumentException.class, () -> mediaController.getAudioDeviceById(0));
    }

    @Test
    void getAudioDeviceByIdNegative() {
        addBeforeTest();

        assertThrows(IllegalArgumentException.class, () -> mediaController.getAudioDeviceById(-12));
    }

    @Test
    void getAdioDevicesByRoom() {
        addBeforeTest();

        assertEquals(1, mediaController.getAdioDevicesByRoom(searchedRoom.getRoomID()).size());
        assertEquals(mediaController.getAdioDevicesByRoom(mediaDevice.getRoom().getRoomID()).get(0).getName(), mediaDTO.getName());
        assertEquals(mediaController.getAdioDevicesByRoom(mediaDevice.getRoom().getRoomID()).get(0).getId(), mediaDevice.getId());
    }


    @Test
    void addDeviceWithCategory(){
        addBeforeTest();

        int categoryid = Integer.parseInt(mediaDevice.getClass().getAnnotation(DiscriminatorValue.class).value());
        assertEquals(2, categoryid);
    }
}