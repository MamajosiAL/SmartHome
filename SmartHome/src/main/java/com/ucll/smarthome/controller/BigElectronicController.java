package com.ucll.smarthome.controller;

import com.ucll.smarthome.dto.BigElectronicDTO;
import com.ucll.smarthome.dto.ConsumptionDTO;
import com.ucll.smarthome.dto.ConsumptionDTO;
import com.ucll.smarthome.dto.TypeDTO;
import com.ucll.smarthome.functions.UserSecurityFunc;
import com.ucll.smarthome.persistence.entities.*;
import com.ucll.smarthome.persistence.repository.BigElectronicDAO;
import com.ucll.smarthome.persistence.repository.ProgrammeDAO;
import com.ucll.smarthome.persistence.repository.TypeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.webjars.NotFoundException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Comparator;
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
    private final UserSecurityFunc userSecurityFunc;

    @Autowired
    public BigElectronicController(BigElectronicDAO beDao, TypeDAO typeDAO, ProgrammeDAO programmeDAO, RoomController roomController, ConsumptionController consumptionController, UserSecurityFunc userSecurityFunc) {
        this.beDao = beDao;
        this.typeDAO = typeDAO;
        this.programmeDAO = programmeDAO;
        this.roomController = roomController;
        this.consumptionController = consumptionController;
        this.userSecurityFunc = userSecurityFunc;
    }

    public void createApplianceDevice(BigElectronicDTO beDTO) throws IllegalArgumentException{
        if (beDTO == null ) throw new IllegalArgumentException("Input data missing");
        if (beDTO.getName() == null || beDTO.getName().length() == 0) throw new IllegalArgumentException("Name of device is not filled in");
        if (beDTO.getType() == null) throw new IllegalArgumentException("Please select a type");
        Room room = roomController.roomExists(beDTO.getRoomid());
        if(!userSecurityFunc.checkCurrentUserIsAdmin(room.getHouse().getHouseId())) throw new NotFoundException("User is not admin of house");

        BigElectronicDevice appliances = new BigElectronicDevice.Builder()
                .name(beDTO.getName())
                .status(beDTO.isStatus())
                .type(getTypeByName(beDTO.getType().getTypeName()).orElse(null))
                .tempature(beDTO.getTempature())
                .timer(null)
                .room(roomController.roomExists(beDTO.getRoomid())).build();
        beDao.save(appliances);
        consumptionController.createConsumption(new ConsumptionDTO.Builder().device(appliances.getId()).build());
    }
    private void updateBeDeviceWithProgramme(BigElectronicDTO beDTO, Programme programme) throws IllegalArgumentException{
        System.out.println(beDTO.toString());
        Room room = roomController.roomExists(beDTO.getRoomid());

        BigElectronicDevice apl = appliancesExists(beDTO.getId());
            apl.setName(beDTO.getName());
            apl.setStatus(beDTO.isStatus());
            apl.setProgramme(programme);
            apl.setType(getType(programme.getType()).orElse(null));
            apl.setTempature(programme.getTempature());
            apl.setTimer(programme.getTimer());
    }

    public BigElectronicDTO getProgramValues(long programid){

        Optional<Programme> programme = programmeDAO.findById(programid);
        if (programme.isEmpty()) throw new IllegalArgumentException("Program not found");
        return new BigElectronicDTO.Builder().tempature(programme.get().getTempature()).timer(programme.get().getTimer()).build();

    }

    public void updateBeDeviceDevice(BigElectronicDTO beDTO) throws IllegalArgumentException{
        if (beDTO == null ) throw new IllegalArgumentException("Input data missing");
        if (beDTO.getName() == null || beDTO.getName().trim().equals("")) throw new IllegalArgumentException("Name of device is not filled in");
        Optional<Programme> programme = programmeDAO.findById(beDTO.getProgramid());
        if (beDTO.getTimer() == null) throw new IllegalArgumentException("Please fill in the time");
        if (beDTO.getType() != null){
            if (!beDTO.getType().getTypeName().equals("Cooling Device") && beDTO.getTempature() < 0){
                beDTO.setTempature(0);
            }else  {
                if (programme.isEmpty()) throw new IllegalArgumentException("Programme does not exist");
            }
        }
            BigElectronicDevice apl = appliancesExists(beDTO.getId());
            apl.setName(beDTO.getName());
            apl.setStatus(beDTO.isStatus());
            apl.setProgramme(programme.get());
            apl.setTempature(beDTO.getTempature());
            apl.setTimer(beDTO.getTimer());
            apl.setEndProgramme(LocalDateTime.now().plusHours(beDTO.getTimer().getHour()).plusMinutes(beDTO.getTimer().getMinute())
                    .plusSeconds(beDTO.getTimer().getSecond()));


    }
    public void beSetToOf(long deviceid){
        BigElectronicDevice be = beDao.getById(deviceid);
        if (!be.isStatus()){
            be.setProgramme(null);
            be.setEndProgramme(null);
            be.setTempature(0);
            be.setTimer(null);
        }

    }

    public BigElectronicDTO getDeviceByid(long deviceid) throws IllegalArgumentException{
        BigElectronicDevice apl = appliancesExists(deviceid);

        if(userSecurityFunc.getHouseUser(apl.getRoom().getHouse().getHouseId()).isEmpty()) throw new NotFoundException("User is not part of house");

        return new BigElectronicDTO.Builder().id(apl.getId()).name(apl.getName()).status(apl.isStatus())
                .type(new TypeDTO.Builder().typeid(apl.getType().getTypeid()).typeName(apl.getName()).build()).tempature(apl.getTempature()).timer(apl.getTimer()).build();
    }

    public List<BigElectronicDTO> getApplianceDevicesByRoom(long roomid) throws IllegalArgumentException{
        if (roomid <= 0L) throw new IllegalArgumentException("Invalid id");
        Room room = roomController.roomExists(roomid);
        if(userSecurityFunc.getHouseUser(room.getHouse().getHouseId()).isEmpty()) throw new NotFoundException("User is not part of house");
        Stream<BigElectronicDTO> steam = beDao.findAllByRoom(room).stream()
                .sorted(Comparator.comparing(BigElectronicDevice::isStatus).reversed().thenComparing(BigElectronicDevice::getId))
                .map(rec-> new BigElectronicDTO.Builder().id(rec.getId()).name(rec.getName()).status(rec.isStatus()).tempature(rec.getTempature()).timer(rec.getTimer())
                        .programid(getProgrammeid(rec) )
                        .type(new TypeDTO.Builder().typeid(rec.getType().getTypeid()).typeName(rec.getType().getName()).build()).eindeProgramma(rec.getEndProgramme()).build());
        return steam.collect(Collectors.toList());
    }

    public List<BigElectronicDTO> getBigelectronicScheduled(long roomid) throws IllegalArgumentException{
        if (roomid <= 0L) throw new IllegalArgumentException("Invalid id");
        Room room = roomController.roomExists(roomid);
        Stream<BigElectronicDTO> steam = beDao.findAllByRoom(room).stream()
                .sorted(Comparator.comparing(BigElectronicDevice::isStatus).reversed().thenComparing(BigElectronicDevice::getId))
                .map(rec-> new BigElectronicDTO.Builder().id(rec.getId()).name(rec.getName()).status(rec.isStatus()).tempature(rec.getTempature()).timer(rec.getTimer())
                        .programid(getProgrammeid(rec) )
                        .type(new TypeDTO.Builder().typeid(rec.getType().getTypeid()).typeName(rec.getType().getName()).build()).eindeProgramma(rec.getEndProgramme()).build());
        return steam.collect(Collectors.toList());
    }
    private long getProgrammeid(BigElectronicDevice be){
        if (be.getProgramme() != null){
            return be.getProgramme().getProgramid();
        }
        return 0;

    }

    private Optional<Type> getType(Type type){
        return typeDAO.findById(type.getTypeid());
    }

    private Optional<Type> getTypeByName(String typeName){
        return typeDAO.findByName(typeName);
    }

    private BigElectronicDevice appliancesExists(long deviceid) throws IllegalArgumentException{
        Optional<BigElectronicDevice> device = beDao.findById(deviceid);
        if (device.isEmpty()) throw new IllegalArgumentException("Device doesn't exist ");
        return device.get();
    }


}
