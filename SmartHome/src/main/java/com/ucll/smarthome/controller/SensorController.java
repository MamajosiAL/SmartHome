package com.ucll.smarthome.controller;

import com.ucll.smarthome.dto.SensorDTO;
import com.ucll.smarthome.persistence.entities.Room;
import com.ucll.smarthome.persistence.entities.Sensor;
import com.ucll.smarthome.persistence.repository.SensorDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@Transactional
public class SensorController {

    private final SensorDAO sensorDAO;
    private final RoomController roomController;

    @Autowired
    public SensorController(SensorDAO sensorDAO, RoomController roomController) {
        this.sensorDAO = sensorDAO;
        this.roomController = roomController;
    }

    public void createSensorDevice(SensorDTO sensorDTO) throws IllegalArgumentException{
        if (sensorDTO == null ) throw new IllegalArgumentException("Input data missing");
        if (sensorDTO.getName() == null || sensorDTO.getName().length() == 0) throw new IllegalArgumentException("Name of device is not filled in");

        Sensor sensor = new Sensor.Builder()
                .name(sensorDTO.getName())
                .status(sensorDTO.isStatus())
                .sensorType(sensorDTO.getSensorType())
                .sensordata(sensorDTO.getSensordata())
                .room(roomController.roomExists(sensorDTO.getRoomid())).build();
        sensorDAO.save(sensor);
    }

    public void updateSensorDevice(SensorDTO sensorDTO) throws IllegalArgumentException{
        if (sensorDTO == null ) throw new IllegalArgumentException("Input data missing");
        if (sensorDTO.getName() == null || sensorDTO.getName().trim().equals("")) throw new IllegalArgumentException("Name of device is not filled in");

        Sensor sensor = sensorExists(sensorDTO.getId());
        sensor.setName(sensorDTO.getName());
        sensor.setStatus(sensorDTO.isStatus());
        sensor.setSensorType(sensorDTO.getName());
        sensor.setSensordata(sensorDTO.getSensordata());
    }
    public SensorDTO getSensorDeviceById(long deviceid) throws IllegalArgumentException{
        Sensor sensor = sensorExists(deviceid);
        return new SensorDTO.Builder().id(sensor.getId()).name(sensor.getName()).status(sensor.isStatus()).build();
    }
    public List<SensorDTO> getSonsorDevicesByRoom(long roomid) throws IllegalArgumentException{
        if (roomid <= 0L) throw new IllegalArgumentException("Invalid id");
        Room room = roomController.roomExists(roomid);
        Stream<SensorDTO> stream = sensorDAO.findAllByRoom(room).stream()
                .map(rec-> new SensorDTO.Builder().id(rec.getId()).name(rec.getName()).status(rec.isStatus()).sensorType(rec.getSensorType()).sensordata(rec.getSensordata()).build());
        return stream.collect(Collectors.toList());
    }
    public void deleteSensorDeviceById(long deviceid) throws IllegalArgumentException{
        if (deviceid <= 0L) throw new IllegalArgumentException("Invalid id");
        Sensor sensor = sensorExists(deviceid);
        sensorDAO.delete(sensor);

    }
    private Sensor sensorExists(long deviceid) throws IllegalArgumentException{
        Optional<Sensor> device = sensorDAO.findById(deviceid);
        if (device.isEmpty()) throw new IllegalArgumentException("Device doesn't exist ");
        return device.get();
    }
}
