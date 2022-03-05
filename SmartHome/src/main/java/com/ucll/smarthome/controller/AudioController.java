package com.ucll.smarthome.controller;

import com.ucll.smarthome.dto.AudioDTO;
import com.ucll.smarthome.persistence.entities.Audio;
import com.ucll.smarthome.persistence.entities.Device;
import com.ucll.smarthome.persistence.entities.Room;
import com.ucll.smarthome.persistence.repository.AudioDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@Transactional
public class AudioController {

    private final AudioDAO audioDAO;
    private final RoomController roomController;
    private final ConsumptionController consumptionController;

    @Autowired
    public AudioController(AudioDAO audioDAO, RoomController roomController, ConsumptionController consumptionController) {
        this.audioDAO = audioDAO;
        this.roomController = roomController;
        this.consumptionController = consumptionController;
    }

    public void createAudioDevice(AudioDTO audioDTO) throws IllegalArgumentException{
        if (audioDTO == null ) throw new IllegalArgumentException("Input data missing");
        if (audioDTO.getName() == null || audioDTO.getName().length() == 0) throw new IllegalArgumentException("Name of device is not filled in");

        Audio audio = new Audio.Builder()
                .name(audioDTO.getName())
                .status(audioDTO.isStatus())
                .volume(audioDTO.getVolume())
                .room(roomController.roomExists(audioDTO.getRoomid()))
                .build();
        audioDAO.save(audio);
    }

    public void updateAudioDevice(AudioDTO audioDTO) throws IllegalArgumentException{
        if (audioDTO == null ) throw new IllegalArgumentException("Input data missing");
        if (audioDTO.getName() == null || audioDTO.getName().trim().equals("")) throw new IllegalArgumentException("Name of device is not filled in");
        Audio audio = audioExists(audioDTO.getId());
        audio.setName(audioDTO.getName());
        audio.setStatus(audioDTO.isStatus());
        audio.setVolume(audioDTO.getVolume());

    }

    public AudioDTO getAudioDeviceById(long deviceid) throws IllegalArgumentException{
        Audio audio = audioExists(deviceid);
        return new AudioDTO.Builder().id(audio.getId()).name(audio.getName()).status(audio.isStatus()).volume(audio.getVolume()).build();
    }

    public List<AudioDTO> getAdioDevicesByRoom(long roomid) throws IllegalArgumentException{
        if (roomid <= 0L) throw new IllegalArgumentException("Invalid id");
        Room room = roomController.roomExists(roomid);

        Stream<AudioDTO> stream = audioDAO.findAllByRoom(room).stream()
                .map(rec -> new AudioDTO.Builder().id(rec.getId()).name(rec.getName()).status(rec.isStatus()).volume(rec.getVolume()).build());
        return stream.collect(Collectors.toList());

    }
    public void deleteAudioDevice(long deviceid) throws IllegalArgumentException{
        if (deviceid <= 0L) throw new IllegalArgumentException("Invalid id");
        Audio audio = audioExists(deviceid);
        audioDAO.delete(audio);

    }
    private Audio audioExists(long deviceid) throws IllegalArgumentException{
        Optional<Audio> device = audioDAO.findById(deviceid);
        if (device.isEmpty()) throw new IllegalArgumentException("Device doesn't exist ");
        return device.get();
    }

    public void changeStatus(long deviceid){
        Device device = audioExists(deviceid);
        consumptionController.deviceChangeStatus(device);
    }

}
