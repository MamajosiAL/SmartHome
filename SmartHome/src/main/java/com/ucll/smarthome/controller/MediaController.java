package com.ucll.smarthome.controller;

import com.ucll.smarthome.dto.MediaDTO;
import com.ucll.smarthome.persistence.entities.MediaDevice;
import com.ucll.smarthome.persistence.entities.Device;
import com.ucll.smarthome.persistence.entities.Room;
import com.ucll.smarthome.persistence.repository.MediaDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@Transactional
public class MediaController {

    private final MediaDAO mediaDAO;
    private final RoomController roomController;
    private final ConsumptionController consumptionController;

    @Autowired
    public MediaController(MediaDAO mediaDAO, RoomController roomController, ConsumptionController consumptionController) {
        this.mediaDAO = mediaDAO;
        this.roomController = roomController;
        this.consumptionController = consumptionController;
    }

    public void createAudioDevice(MediaDTO mediaDTO) throws IllegalArgumentException{
        if (mediaDTO == null ) throw new IllegalArgumentException("Input data missing");
        if (mediaDTO.getName() == null || mediaDTO.getName().length() == 0) throw new IllegalArgumentException("Name of device is not filled in");

        MediaDevice mediaDevice = new MediaDevice.Builder()
                .name(mediaDTO.getName())
                .status(mediaDTO.isStatus())
                .volume(mediaDTO.getVolume())
                .room(roomController.roomExists(mediaDTO.getRoomid()))
                .build();
        mediaDAO.save(mediaDevice);
    }

    public void updateAudioDevice(MediaDTO mediaDTO) throws IllegalArgumentException{
        if (mediaDTO == null ) throw new IllegalArgumentException("Input data missing");
        if (mediaDTO.getName() == null || mediaDTO.getName().trim().equals("")) throw new IllegalArgumentException("Name of device is not filled in");
        MediaDevice mediaDevice = audioExists(mediaDTO.getId());
        mediaDevice.setName(mediaDTO.getName());
        mediaDevice.setStatus(mediaDTO.isStatus());
        mediaDevice.setVolume(mediaDTO.getVolume());

    }

    public MediaDTO getAudioDeviceById(long deviceid) throws IllegalArgumentException{
        MediaDevice mediaDevice = audioExists(deviceid);
        return new MediaDTO.Builder().id(mediaDevice.getId()).name(mediaDevice.getName()).status(mediaDevice.isStatus()).volume(mediaDevice.getVolume()).build();
    }

    public List<MediaDTO> getAdioDevicesByRoom(long roomid) throws IllegalArgumentException{
        if (roomid <= 0L) throw new IllegalArgumentException("Invalid id");
        Room room = roomController.roomExists(roomid);

        Stream<MediaDTO> stream = mediaDAO.findAllByRoom(room).stream()
                .map(rec -> new MediaDTO.Builder().id(rec.getId()).name(rec.getName()).status(rec.isStatus()).volume(rec.getVolume()).build());
        return stream.collect(Collectors.toList());

    }
    public void deleteAudioDevice(long deviceid) throws IllegalArgumentException{
        if (deviceid <= 0L) throw new IllegalArgumentException("Invalid id");
        MediaDevice mediaDevice = audioExists(deviceid);
        mediaDAO.delete(mediaDevice);

    }
    private MediaDevice audioExists(long deviceid) throws IllegalArgumentException{
        Optional<MediaDevice> device = mediaDAO.findById(deviceid);
        if (device.isEmpty()) throw new IllegalArgumentException("Device doesn't exist ");
        return device.get();
    }

    public void changeStatus(long deviceid){
        Device device = audioExists(deviceid);
        consumptionController.deviceChangeStatus(device);
    }

}
