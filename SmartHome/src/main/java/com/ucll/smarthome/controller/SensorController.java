package com.ucll.smarthome.controller;

import com.ucll.smarthome.dto.SensorDTO;
import com.ucll.smarthome.functions.UserSecurityFunc;
import com.ucll.smarthome.persistence.entities.Device;
import com.ucll.smarthome.persistence.entities.Room;
import com.ucll.smarthome.persistence.entities.SensorDevice;
import com.ucll.smarthome.persistence.repository.SensorDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.webjars.NotFoundException;

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
    private final ConsumptionController consumptionController;
    private final UserSecurityFunc userSecurityFunc;

    @Autowired
    public SensorController(SensorDAO sensorDAO, RoomController roomController, ConsumptionController consumptionController, UserSecurityFunc userSecurityFunc) {
        this.sensorDAO = sensorDAO;
        this.roomController = roomController;
        this.consumptionController = consumptionController;
        this.userSecurityFunc = userSecurityFunc;
    }

    public void createSensorDevice(SensorDTO sensorDTO) throws IllegalArgumentException{
        if (sensorDTO == null ) throw new IllegalArgumentException("Input data missing");
        if (sensorDTO.getName() == null || sensorDTO.getName().length() == 0) throw new IllegalArgumentException("Name of device is not filled in");

        Room room = roomController.roomExists(sensorDTO.getRoomid());
        if(!userSecurityFunc.checkCurrentUserIsAdmin(room.getHouse().getHouseId())) throw new NotFoundException("User is not admin of house");

        SensorDevice sensorDevice = new SensorDevice.Builder()
                .name(sensorDTO.getName())
                .status(sensorDTO.isStatus())
                .sensorType(sensorDTO.getSensorType())
                .sensordata(sensorDTO.getSensordata())
                .room(roomController.roomExists(sensorDTO.getRoomid())).build();
        sensorDAO.save(sensorDevice);
    }

    public void updateSensorDevice(SensorDTO sensorDTO) throws IllegalArgumentException{
        if (sensorDTO == null ) throw new IllegalArgumentException("Input data missing");
        if (sensorDTO.getName() == null || sensorDTO.getName().trim().equals("")) throw new IllegalArgumentException("Name of device is not filled in");

        Room room = roomController.roomExists(sensorDTO.getRoomid());
        if(!userSecurityFunc.checkCurrentUserIsAdmin(room.getHouse().getHouseId())) throw new NotFoundException("User is not admin of house");

        SensorDevice sensorDevice = sensorExists(sensorDTO.getId());
        sensorDevice.setName(sensorDTO.getName());
        sensorDevice.setStatus(sensorDTO.isStatus());
        sensorDevice.setSensorType(sensorDTO.getName());
        sensorDevice.setSensordata(sensorDTO.getSensordata());
    }
    public SensorDTO getSensorDeviceById(long deviceid) throws IllegalArgumentException{
        SensorDevice sensorDevice = sensorExists(deviceid);
        if(userSecurityFunc.getHouseUser(sensorDevice.getRoom().getHouse().getHouseId()).isEmpty()) throw new NotFoundException("User is not part of house");
        return new SensorDTO.Builder().id(sensorDevice.getId()).name(sensorDevice.getName()).status(sensorDevice.isStatus()).sensordata(sensorDevice.getSensordata()).sensorType(sensorDevice.getSensorType()).build();
    }

    public List<SensorDTO> getSonsorDevicesByRoom(long roomid) throws IllegalArgumentException{
        if (roomid <= 0L) throw new IllegalArgumentException("Invalid id");
        Room room = roomController.roomExists(roomid);

        if(userSecurityFunc.getHouseUser(room.getHouse().getHouseId()).isEmpty()) throw new NotFoundException("User is not part of house");

        Stream<SensorDTO> stream = sensorDAO.findAllByRoom(room).stream()
                .map(rec-> new SensorDTO.Builder().id(rec.getId()).name(rec.getName()).status(rec.isStatus()).sensorType(rec.getSensorType()).sensordata(rec.getSensordata()).build());
        return stream.collect(Collectors.toList());
    }

    public void deleteSensorDeviceById(long deviceid) throws IllegalArgumentException{
        if (deviceid <= 0L) throw new IllegalArgumentException("Invalid id");
        SensorDevice sensorDevice = sensorExists(deviceid);
        if(!userSecurityFunc.checkCurrentUserIsAdmin(sensorDevice.getRoom().getHouse().getHouseId())) throw new NotFoundException("User is not admin of this house");
        sensorDAO.delete(sensorDevice);

    }
    private SensorDevice sensorExists(long deviceid) throws IllegalArgumentException{
        Optional<SensorDevice> device = sensorDAO.findById(deviceid);
        if (device.isEmpty()) throw new IllegalArgumentException("Device doesn't exist ");
        return device.get();
    }

    public void changeStatus(long deviceid){
        Device device = sensorExists(deviceid);
        consumptionController.deviceChangeStatus(device);
    }

}
