package com.ucll.smarthome.controller;

import com.ucll.smarthome.AbstractIntegrationTest;
import com.ucll.smarthome.dto.AudioDTO;
import com.ucll.smarthome.dto.HouseDTO;
import com.ucll.smarthome.dto.RoomDTO;
import com.ucll.smarthome.dto.UserDTO;
import com.ucll.smarthome.persistence.entities.Audio;
import com.ucll.smarthome.persistence.entities.House;
import com.ucll.smarthome.persistence.entities.Room;
import com.ucll.smarthome.persistence.repository.AudioDAO;
import com.ucll.smarthome.persistence.repository.HouseDAO;
import com.ucll.smarthome.persistence.repository.RoomDAO;
import com.ucll.smarthome.persistence.repository.TypeDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;

import static org.junit.jupiter.api.Assertions.*;

@WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, value = "TestUser", userDetailsServiceBeanName = "UserDetailService")
class AudioControllerTest extends AbstractIntegrationTest {
    @Autowired
    private HouseController houseController;

    @Autowired
    private UserController userController;

    @Autowired
    private RoomController roomController;

    @Autowired
    private AudioController audioController;

    @Autowired
    private HouseDAO houseDAO;

    @Autowired
    private RoomDAO roomDAO;

    @Autowired
    private AudioDAO audioDAO;

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

    private AudioDTO audioDTO = new AudioDTO.Builder()
            .name("SpeakersWoonkamer")
            .status(false)
            .volume(10)
            .build();

    private House searchedHouse;
    private Room searchedRoom;
    private Audio searchedAudio;

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

        audioDTO.setRoomid(searchedRoom.getRoomID());
        audioController.createAudioDevice(audioDTO);

        searchedAudio = audioDAO.findAllByRoom(searchedRoom).stream().findFirst().get();

        System.out.println("House created name: " + searchedHouse.getName());
        System.out.println("Room created name: " + searchedRoom.getName());
        System.out.println("Audio device created name: " + searchedAudio.getName());
    }

    @Test
    void createAudioDevice() {
        addBeforeTest();

        AudioDTO createAudioDTO = new AudioDTO.Builder()
                .name("SpeakersWoonkamer")
                .status(false)
                .volume(10)
                .roomid(searchedRoom.getRoomID())
                .build();

        audioController.createAudioDevice(createAudioDTO);

        assertEquals(audioDAO.findAll().size(), 2);
    }

    @Test
    void createAudioDeviceNull() {
        addBeforeTest();

        AudioDTO createAudioDTO = null;
        assertThrows(IllegalArgumentException.class, () -> audioController.createAudioDevice(createAudioDTO));
    }

    @Test
    void createAudioDeviceNameSpace() {
        addBeforeTest();

        AudioDTO createAudioDTO = new AudioDTO.Builder()
                .name("")
                .status(false)
                .volume(10)
                .roomid(searchedRoom.getRoomID())
                .build();

        assertThrows(IllegalArgumentException.class, () -> audioController.createAudioDevice(createAudioDTO));
    }

    @Test
    void createAudioDeviceNameEmpty() {
        addBeforeTest();

        AudioDTO createAudioDTO = new AudioDTO.Builder()
                .name("")
                .status(false)
                .volume(10)
                .roomid(searchedRoom.getRoomID())
                .build();

        assertThrows(IllegalArgumentException.class, () -> audioController.createAudioDevice(createAudioDTO));
    }

    @Test
    void createAudioDeviceNameNull() {
        addBeforeTest();

        AudioDTO createAudioDTO = new AudioDTO.Builder()
                .name(null)
                .status(false)
                .volume(10)
                .roomid(searchedRoom.getRoomID())
                .build();

        assertThrows(IllegalArgumentException.class, () -> audioController.createAudioDevice(createAudioDTO));
    }

    @Test
    void createAudioDeviceStatusEmpty() {
        addBeforeTest();

        AudioDTO createAudioDTO = new AudioDTO.Builder()
                .name("SpeakerWoonKamer")
                .volume(10)
                .roomid(searchedRoom.getRoomID())
                .build();

        assertThrows(IllegalArgumentException.class, () -> audioController.createAudioDevice(createAudioDTO));
    }

    @Test
    void createAudioDeviceVolumeNegative() {
        addBeforeTest();

        AudioDTO createAudioDTO = new AudioDTO.Builder()
                .name("SpeakerWoonKamer")
                .status(false)
                .volume(-10)
                .roomid(searchedRoom.getRoomID())
                .build();

        assertThrows(IllegalArgumentException.class, () -> audioController.createAudioDevice(createAudioDTO));
    }

    @Test
    void createAudioDeviceVolumeNull() {
        addBeforeTest();

        AudioDTO createAudioDTO = new AudioDTO.Builder()
                .name("SpeakerWoonKamer")
                .status(false)
                .roomid(searchedRoom.getRoomID())
                .build();

        assertThrows(IllegalArgumentException.class, () -> audioController.createAudioDevice(createAudioDTO));
    }

    @Test
    void createAudioDeviceRoomIdNull() {
        addBeforeTest();

        AudioDTO createAudioDTO = new AudioDTO.Builder()
                .name("SpeakerWoonKamer")
                .status(false)
                .volume(10)
                .build();

        assertThrows(IllegalArgumentException.class, () -> audioController.createAudioDevice(createAudioDTO));
    }

    @Test
    void updateAudioDevice() {
        addBeforeTest();

        String alteredName = "AlteredSpeakerWoonKamer";

        AudioDTO updateaudioDTO = new AudioDTO.Builder()
                .id(searchedAudio.getId())
                .name(alteredName)
                .status(false)
                .volume(10)
                .roomid(searchedRoom.getRoomID())
                .build();

        searchedAudio = audioDAO.findAllByRoom(searchedRoom).stream().findFirst().get();

        assertEquals(searchedAudio.getName(), updateaudioDTO.getName());
        assertNotEquals(searchedAudio.getName(), audioDTO.getName());
    }

    @Test
    void updateAudioDeviceNameNull() {
        addBeforeTest();

        AudioDTO updateaudioDTO = new AudioDTO.Builder()
                .id(searchedAudio.getId())
                .name(null)
                .status(false)
                .volume(10)
                .roomid(searchedRoom.getRoomID())
                .build();

        assertThrows(IllegalArgumentException.class, () -> audioController.createAudioDevice(updateaudioDTO));
    }

    @Test
    void updateAudioDeviceNameSpace() {
        addBeforeTest();

        AudioDTO updateaudioDTO = new AudioDTO.Builder()
                .id(searchedAudio.getId())
                .name(" ")
                .status(false)
                .volume(10)
                .roomid(searchedRoom.getRoomID())
                .build();

        assertThrows(IllegalArgumentException.class, () -> audioController.createAudioDevice(updateaudioDTO));
    }

    @Test
    void updateAudioDeviceNameEmpty() {
        addBeforeTest();

        AudioDTO updateaudioDTO = new AudioDTO.Builder()
                .id(searchedAudio.getId())
                .name("")
                .status(false)
                .volume(10)
                .roomid(searchedRoom.getRoomID())
                .build();

        assertThrows(IllegalArgumentException.class, () -> audioController.createAudioDevice(updateaudioDTO));
    }

    @Test
    void updateAudioDeviceStatusEmpty() {
        addBeforeTest();

        String alteredName = "AlteredSpeakerWoonKamer";

        AudioDTO updateaudioDTO = new AudioDTO.Builder()
                .id(searchedAudio.getId())
                .name(alteredName)
                .volume(10)
                .roomid(searchedRoom.getRoomID())
                .build();

        assertThrows(IllegalArgumentException.class, () -> audioController.createAudioDevice(updateaudioDTO));
    }

    @Test
    void updateAudioDeviceNegativeVolume() {
        addBeforeTest();

        String alteredName = "AlteredSpeakerWoonKamer";

        AudioDTO updateaudioDTO = new AudioDTO.Builder()
                .id(searchedAudio.getId())
                .name(alteredName)
                .volume(-10)
                .roomid(searchedRoom.getRoomID())
                .build();

        assertThrows(IllegalArgumentException.class, () -> audioController.createAudioDevice(updateaudioDTO));
    }

    @Test
    void updateAudioDeviceId0() {
        addBeforeTest();

        String alteredName = "AlteredSpeakerWoonKamer";

        AudioDTO updateaudioDTO = new AudioDTO.Builder()
                .id(0)
                .name(alteredName)
                .volume(-10)
                .roomid(searchedRoom.getRoomID())
                .build();

        assertThrows(IllegalArgumentException.class, () -> audioController.createAudioDevice(updateaudioDTO));
    }

    @Test
    void updateAudioDeviceIdRoomID0() {
        addBeforeTest();

        String alteredName = "AlteredSpeakerWoonKamer";

        AudioDTO updateaudioDTO = new AudioDTO.Builder()
                .id(searchedAudio.getId())
                .name(alteredName)
                .volume(-10)
                .roomid(0)
                .build();

        assertThrows(IllegalArgumentException.class, () -> audioController.createAudioDevice(updateaudioDTO));
    }

    @Test
    void getAudioDeviceById() {
        addBeforeTest();
        assertEquals(searchedAudio.getName(), audioController.getAudioDeviceById(searchedAudio.getId()).getName());
    }

    @Test
    void getAudioDeviceById0() {
        addBeforeTest();
        assertEquals(searchedAudio.getName(), audioController.getAudioDeviceById(searchedAudio.getId()).getName());

        assertThrows(IllegalArgumentException.class, () -> audioController.getAudioDeviceById(0));
    }

    @Test
    void getAudioDeviceByIdNegative() {
        addBeforeTest();

        assertThrows(IllegalArgumentException.class, () -> audioController.getAudioDeviceById(-12));
    }

    @Test
    void getAdioDevicesByRoom() {
        addBeforeTest();

        assertEquals(1, audioController.getAdioDevicesByRoom(searchedRoom.getRoomID()).size());
        assertTrue(audioController.getAdioDevicesByRoom(searchedRoom.getRoomID()).contains(audioDTO));
    }

    @Test
    void deleteAudioDevice() {
        addBeforeTest();

        audioController.deleteAudioDevice(searchedAudio.getId());

        assertEquals(0, audioController.getAdioDevicesByRoom(searchedRoom.getRoomID()).size());
    }

    @Test
    void changeStatus() {
        addBeforeTest();

        assertFalse(audioController.getAudioDeviceById(searchedAudio.getId()).isStatus());

        audioController.changeStatus(searchedAudio.getId());

        assertTrue(audioController.getAudioDeviceById(searchedAudio.getId()).isStatus());
    }
}