package com.ucll.smarthome.controller;

import com.ucll.smarthome.dto.AppliancesDTO;
import com.ucll.smarthome.functions.UserSecurityFunc;
import com.ucll.smarthome.persistence.entities.*;
import com.ucll.smarthome.persistence.repository.AppliancesDAO;
import com.ucll.smarthome.persistence.repository.TypeDAO;
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
public class AppliancesController {

    private final AppliancesDAO aplDao;
    private final TypeDAO typeDAO;
    private final RoomController roomController;
    private final ConsumptionController consumptionController;
    private final UserSecurityFunc userSecurityFunc;

    @Autowired
    public AppliancesController(AppliancesDAO aplDao, TypeDAO typeDAO, RoomController roomController, ConsumptionController consumptionController, UserSecurityFunc userSecurityFunc) {
        this.aplDao = aplDao;
        this.typeDAO = typeDAO;
        this.roomController = roomController;
        this.consumptionController = consumptionController;
        this.userSecurityFunc = userSecurityFunc;
    }

    public void createApplianceDevice(AppliancesDTO aplDTO) throws IllegalArgumentException{
        if (aplDTO == null ) throw new IllegalArgumentException("Input data missing");
        if (aplDTO.getName() == null || aplDTO.getName().length() == 0) throw new IllegalArgumentException("Name of device is not filled in");

        Room room = roomController.roomExists(aplDTO.getRoomid());
        if(!userSecurityFunc.checkCurrentUserIsAdmin(room.getHouse().getHouseId())) throw new NotFoundException("User is not admin of house");

        Appliances appliances = new Appliances.Builder()
                .name(aplDTO.getName())
                .status(aplDTO.isStatus())
                .type(getType(aplDTO.getType()).orElse(null))
                .tempature(aplDTO.getTempature())
                .timer(aplDTO.getTimer())
                .room(roomController.roomExists(aplDTO.getRoomid())).build();
        aplDao.save(appliances);
    }
    public void updateApplianceDevice(AppliancesDTO aplDTO) throws IllegalArgumentException{
        if (aplDTO == null ) throw new IllegalArgumentException("Input data missing");
        if (aplDTO.getName() == null || aplDTO.getName().trim().equals("")) throw new IllegalArgumentException("Name of device is not filled in");

        Room room = roomController.roomExists(aplDTO.getRoomid());
        if(!userSecurityFunc.checkCurrentUserIsAdmin(room.getHouse().getHouseId())) throw new NotFoundException("User is not admin of house");

        Appliances apl = appliancesExists(aplDTO.getId());
        apl.setName(aplDTO.getName());
        apl.setStatus(aplDTO.isStatus());
        apl.setType(getType(aplDTO.getType()).orElse(null));
        apl.setTempature(aplDTO.getTempature());
        apl.setTimer(aplDTO.getTimer());

    }

    public AppliancesDTO getDeviceByid(long deviceid) throws IllegalArgumentException{
        Appliances apl = appliancesExists(deviceid);

        if(userSecurityFunc.getHouseUser(apl.getRoom().getHouse().getHouseId()).isEmpty()) throw new NotFoundException("User is not part of house");

        return new AppliancesDTO.Builder().id(apl.getId()).name(apl.getName()).status(apl.isStatus())
                .type(apl.getType()).tempature(apl.getTempature()).timer(apl.getTimer()).build();
    }

    public List<AppliancesDTO> getApplianceDevicesByRoom(long roomid) throws IllegalArgumentException{
        if (roomid <= 0L) throw new IllegalArgumentException("Invalid id");

        Room room = roomController.roomExists(roomid);
        if(userSecurityFunc.getHouseUser(room.getHouse().getHouseId()).isEmpty()) throw new NotFoundException("User is not part of house");

        Stream<AppliancesDTO> steam = aplDao.findAllByRoom(room).stream()
                .map(rec-> new AppliancesDTO.Builder().id(rec.getId()).name(rec.getName()).status(rec.isStatus()).tempature(rec.getTempature()).timer(rec.getTimer()).type(rec.getType()).build());
        return steam.collect(Collectors.toList());
    }

    public void deleteApplianceDeviceById(long deviceid) throws IllegalArgumentException{
        if (deviceid <= 0L) throw new IllegalArgumentException("Invalid id");
        Appliances apl = appliancesExists(deviceid);

        if(!userSecurityFunc.checkCurrentUserIsAdmin(apl.getRoom().getHouse().getHouseId())) throw new NotFoundException("User is not admin of house");

        aplDao.delete(apl);
    }

    private Optional<Type> getType(Type type){
        return typeDAO.findById(type.getTypeid());
    }

    private Appliances appliancesExists(long deviceid) throws IllegalArgumentException{
        Optional<Appliances> device = aplDao.findById(deviceid);
        if (device.isEmpty()) throw new IllegalArgumentException("Device doesn't exist ");
        return device.get();
    }

    public void changeStatus(long deviceid){
        Device device = appliancesExists(deviceid);
        consumptionController.deviceChangeStatus(device);
    }

}
