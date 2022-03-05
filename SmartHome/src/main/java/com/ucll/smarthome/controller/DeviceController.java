package com.ucll.smarthome.controller;

import com.ucll.smarthome.dto.DeviceDTO;
import com.ucll.smarthome.persistence.entities.*;
import com.ucll.smarthome.persistence.repository.*;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@Transactional
public class DeviceController {
    private final DeviceDAO deviceDAO;
    private final RoomController roomController;
    private final ConsumptionController consumptionController;

    public DeviceController(DeviceDAO deviceDAO, RoomController roomController, ConsumptionController consumptionController) {
        this.deviceDAO = deviceDAO;
        this.roomController = roomController;
        this.consumptionController = consumptionController;
    }

    public void createDevice(DeviceDTO deviceDTO) throws IllegalArgumentException{
        if (deviceDTO == null ) throw new IllegalArgumentException("Input data missing");
        if (deviceDTO.getName() == null || deviceDTO.getName().length() == 0) throw new IllegalArgumentException("Name of device is not filled in");

        Device device = new Device.Builder()
                .name(deviceDTO.getName())
                .status(deviceDTO.isStatus())
                .room(roomController.roomExists(deviceDTO.getRoomid()))
                .build();
        deviceDAO.save(device);

    }
    public void updateDevice(DeviceDTO deviceDTO) throws IllegalArgumentException{
        if (deviceDTO == null ) throw new IllegalArgumentException("Input data missing");
        if (deviceDTO.getName() == null || deviceDTO.getName().trim() .equals("")) throw new IllegalArgumentException("Name of device is not filled in");

         Device device =  deviceExists(deviceDTO.getId());

        device.setName(deviceDTO.getName());
        device.setStatus(deviceDTO.isStatus());
    }

    public DeviceDTO getDeviceById(long deviceid) throws IllegalArgumentException{
        Device device = deviceExists(deviceid);
        return new DeviceDTO.Builder().id(device.getId()).name(device.getName()).status(device.isStatus()).build();
    }

    public List<DeviceDTO> getDevicdsByRoom(long roomid) throws IllegalArgumentException{
        if (roomid <= 0L) throw new IllegalArgumentException("Invalid id");
        Room room = roomController.roomExists(roomid);

        Stream<DeviceDTO> stream = deviceDAO.findAllByRoomAndCategoryid(room,4).stream()
                .map(rec -> new DeviceDTO.Builder().id(rec.getId()).name(rec.getName()).status(rec.isStatus()).build());
        return stream.collect(Collectors.toList());
    }

    public void deleteDeviceById(long deviceid) throws IllegalArgumentException{
        if (deviceid <= 0L) throw new IllegalArgumentException("Invalid id");

        Device device = deviceExists(deviceid);
        deviceDAO.delete(device);
    }

    public Device deviceExists(long deviceid) throws IllegalArgumentException{
        Optional<Device> device = deviceDAO.findById(deviceid);
        if (device.isEmpty()) throw new IllegalArgumentException("Device doesn't exist ");
        return device.get();
    }

    public void changeStatus(long deviceid){
        Device device = deviceExists(deviceid);
        consumptionController.deviceChangeStatus(device);
    }
}
