package com.ucll.smarthome.controller;

import com.ucll.smarthome.dto.BigElectronicDTO;
import com.ucll.smarthome.persistence.entities.*;
import com.ucll.smarthome.persistence.repository.BigElectronicDAO;
import com.ucll.smarthome.persistence.repository.ProgrammeDAO;
import com.ucll.smarthome.persistence.repository.TypeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@Transactional
public class BigElectronicController {

    private final BigElectronicDAO beDao;
    private final TypeDAO typeDAO;
    private final ProgrammeDAO programmeDAO;
    private final RoomController roomController;
    private final ConsumptionController consumptionController;

    @Autowired
    public BigElectronicController(BigElectronicDAO beDao, TypeDAO typeDAO, ProgrammeDAO programmeDAO, RoomController roomController, ConsumptionController consumptionController) {
        this.beDao = beDao;
        this.typeDAO = typeDAO;
        this.programmeDAO = programmeDAO;
        this.roomController = roomController;
        this.consumptionController = consumptionController;
    }

    public void createApplianceDevice(BigElectronicDTO beDTO) throws IllegalArgumentException{
        if (beDTO == null ) throw new IllegalArgumentException("Input data missing");
        if (beDTO.getName() == null || beDTO.getName().length() == 0) throw new IllegalArgumentException("Name of device is not filled in");

        BigElectronicDevice appliances = new BigElectronicDevice.Builder()
                .name(beDTO.getName())
                .status(beDTO.isStatus())
                .type(getType(beDTO.getType()).orElse(null))
                .tempature(beDTO.getTempature())
                .timer(beDTO.getTimer())
                .room(roomController.roomExists(beDTO.getRoomid())).build();
        beDao.save(appliances);
    }
    private void updateBeDeviceWithProgramme(BigElectronicDTO beDTO , Programme programme) throws IllegalArgumentException{

            BigElectronicDevice apl = appliancesExists(beDTO.getId());
            apl.setName(beDTO.getName());
            apl.setStatus(beDTO.isStatus());
            apl.setProgramme(programme);
            apl.setType(getType(programme.getType()).orElse(null));
            apl.setTempature(programme.getTempature());
            apl.setTimer(programme.getTimer());

    }
    public void updateApplianceDevice(BigElectronicDTO beDTO) throws IllegalArgumentException{
        if (beDTO == null ) throw new IllegalArgumentException("Input data missing");
        if (beDTO.getName() == null || beDTO.getName().trim().equals("")) throw new IllegalArgumentException("Name of device is not filled in");
        Optional<Programme> programme = programmeDAO.findById(beDTO.getProgramid());
        if (programme.isPresent()){
            updateBeDeviceWithProgramme(beDTO,programme.get());
        }else{

            BigElectronicDevice apl = appliancesExists(beDTO.getId());
            apl.setName(beDTO.getName());
            apl.setStatus(beDTO.isStatus());
            apl.setType(getType(beDTO.getType()).orElse(null));
            apl.setTempature(beDTO.getTempature());
            apl.setTimer(beDTO.getTimer());
        }


    }

    public BigElectronicDTO getDeviceByid(long deviceid) throws IllegalArgumentException{
        BigElectronicDevice apl = appliancesExists(deviceid);
        return new BigElectronicDTO.Builder().id(apl.getId()).name(apl.getName()).status(apl.isStatus())
                .type(apl.getType()).tempature(apl.getTempature()).timer(apl.getTimer()).build();
    }

    public List<BigElectronicDTO> getApplianceDevicesByRoom(long roomid) throws IllegalArgumentException{
        if (roomid <= 0L) throw new IllegalArgumentException("Invalid id");
        Room room = roomController.roomExists(roomid);
        Stream<BigElectronicDTO> steam = beDao.findAllByRoom(room).stream()
                .map(rec-> new BigElectronicDTO.Builder().id(rec.getId()).name(rec.getName()).status(rec.isStatus()).tempature(rec.getTempature()).timer(rec.getTimer()).type(rec.getType()).build());
        return steam.collect(Collectors.toList());
    }
    public void deleteApplianceDeviceById(long deviceid) throws IllegalArgumentException{
        if (deviceid <= 0L) throw new IllegalArgumentException("Invalid id");
        BigElectronicDevice apl = appliancesExists(deviceid);
        beDao.delete(apl);
    }
    private Optional<Type> getType(Type type){
        return typeDAO.findById(type.getTypeid());
    }
    private BigElectronicDevice appliancesExists(long deviceid) throws IllegalArgumentException{
        Optional<BigElectronicDevice> device = beDao.findById(deviceid);
        if (device.isEmpty()) throw new IllegalArgumentException("Device doesn't exist ");
        return device.get();
    }

    public void changeStatus(long deviceid){
        Device device = appliancesExists(deviceid);
        consumptionController.deviceChangeStatus(device);
    }

}
