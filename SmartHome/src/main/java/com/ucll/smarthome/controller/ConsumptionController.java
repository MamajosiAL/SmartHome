package com.ucll.smarthome.controller;

import com.ucll.smarthome.dto.ConsumptionDTO;
import com.ucll.smarthome.functions.UserSecurityFunc;
import com.ucll.smarthome.persistence.entities.*;
import com.ucll.smarthome.persistence.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.webjars.NotFoundException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@Transactional
public class ConsumptionController {

    @Autowired
    private MessageSource msgSrc;

    private final ConsumptionDAO consumptionDAO;
    private final DeviceDAO deviceDAO;
    private final UserSecurityFunc userSecurityFunc;
    private final RoomDAO roomDAO;
    private final HouseDAO houseDAO;
    private final House_UserDAO house_userDAO;
    private final UserDAO userDAO;

    @Autowired
    public ConsumptionController(ConsumptionDAO consumptionDAO, DeviceDAO deviceDAO, UserSecurityFunc userSecurityFunc, RoomDAO roomDAO, HouseDAO houseDAO, House_UserDAO house_userDAO, UserDAO userDAO) {
        this.consumptionDAO = consumptionDAO;
        this.deviceDAO = deviceDAO;
        this.userSecurityFunc = userSecurityFunc;
        this.roomDAO = roomDAO;
        this.houseDAO = houseDAO;
        this.house_userDAO = house_userDAO;
        this.userDAO = userDAO;
    }

    public void createConsumption(ConsumptionDTO consumptionDTO){
        if(consumptionDTO == null) throw new IllegalArgumentException(msgSrc.getMessage("ccontroler.consumption",null, Locale.getDefault()));
        if(consumptionDTO.getDeviceId() <= 0) throw new IllegalArgumentException(msgSrc.getMessage("ccontroler.exception.1",null,Locale.getDefault()));
        //if(consumptionDTO.getUnit() == null || consumptionDTO.getUnit().isEmpty()) throw new IllegalArgumentException("Creating consumption failed. Unit is empty");

        Optional<Device> device = deviceDAO.findById(consumptionDTO.getDeviceId());

        if(device.isEmpty()) throw new IllegalArgumentException("Device does not exist");
        if(!userSecurityFunc.checkCurrentUserIsAdmin(device.get().getRoom().getHouse().getHouseId())) throw new NotFoundException("ccontroller.adminofhouse");
        Consumption consumption = new Consumption.Builder()
                .device(deviceDAO.getById(consumptionDTO.getDeviceId()))
                .aantalMinuten(0)
                .startDatumEnTijd(null)
                .unit("kWh")
                .consumptionPerHour(rngDeviceConsumption())
                .build();

        // if device is on set startdatumtijd on current date and time
        if(device.get().isStatus()){
            consumption.setStartDatumEnTijd(LocalDateTime.now());
        }

        consumptionDAO.save(consumption);
    }

    public void deviceChangeStatus(Device device){
        List<Consumption> consumptionList = getConsumptionsByDevice(device);

        if(userSecurityFunc.getHouseUser(device.getRoom().getHouse().getHouseId()).isEmpty()) throw new NotFoundException("User is not part of house");
        // status wordt aangezet
        if(!device.isStatus()){
                for(Consumption c : consumptionList){
                    c.setStartDatumEnTijd(LocalDateTime.now());
                }
                device.setStatus(true);
            }
        // status wordt uitgezet
        else{
                for(Consumption c : consumptionList){
                    if(c.getStartDatumEnTijd() != null){
                        LocalDateTime start = c.getStartDatumEnTijd();
                        LocalDateTime end = LocalDateTime.now();
                        long minutes = ChronoUnit.MINUTES.between(start,end);
                        c.setAantalMinuten(c.getAantalMinuten() + (int)minutes);
                    }
                }
                device.setStatus(false);
        }
    }

    public List<ConsumptionDTO> getConsumptionsByDeviceId(long deviceid){
        Optional<Device> device = deviceDAO.findById(deviceid);
        if(device.isEmpty()) throw new IllegalArgumentException("No device found");
        Optional<List<Consumption>> consumptionList = consumptionDAO.findAllByDevice(device.get());

        if(userSecurityFunc.getHouseUser(device.get().getRoom().getHouse().getHouseId()).isEmpty()) throw new NotFoundException("ccontroller.userpartof");

        if(device.get().isStatus()){
            // if device is on : reset startdatumentijd & calculate minutes of consumption per day
            for(Consumption c : consumptionList.get()){
                if(c.getStartDatumEnTijd() != null){
                    LocalDateTime start = c.getStartDatumEnTijd();
                    LocalDateTime end = LocalDateTime.now();
                    long minutes = ChronoUnit.MINUTES.between(start,end);

                    // Only when 1 minute has passed from startdatumentijd we can edit aantalminuten & startdatumentijd
                    // otherwise if you keep calling this function startdatumentijd will reset and the calculating won't be accurate anymore
                    if(ChronoUnit.SECONDS.between(start,end) > 60){
                        c.setStartDatumEnTijd(end);
                        c.setAantalMinuten(c.getAantalMinuten() + (int)minutes);
                    }

                }
            }
        }
        return consumptionListToConsumptionDTOList(consumptionList.get());
    }

    public List<Consumption> getConsumptionsByDevice(Device device){
        Optional<List<Consumption>> consumptionList = consumptionDAO.findAllByDevice(device);
        if(userSecurityFunc.getHouseUser(device.getRoom().getHouse().getHouseId()).isEmpty()) throw new NotFoundException("ccontroller.userpartof");

        if(consumptionList.isEmpty()) throw new IllegalArgumentException("This device has no consumption");
        return consumptionList.get();
    }

    public List<ConsumptionDTO> getConsumptionsByRoom(long roomId){
        if(roomId <= 0) throw new IllegalArgumentException("room id not valid");
        Room room = roomDAO.getById(roomId);

        if(userSecurityFunc.getHouseUser(room.getHouse().getHouseId()).isEmpty()) throw new NotFoundException("User is not part of this house");

        List<Device> devices = deviceDAO.findAllByRoom(room);
        List<ConsumptionDTO> consumptionResultList = new ArrayList<>();

        for (Device d : devices) {
            List<ConsumptionDTO> consumptionList = getConsumptionsByDeviceId(d.getId());
            if(!consumptionList.isEmpty()){
                for(ConsumptionDTO cDTO : consumptionList){
                    cDTO.setRoomId(room.getRoomID());
                    cDTO.setHouseId(room.getHouse().getHouseId());
                    consumptionResultList.add(cDTO);
                }
            }
        }
        return consumptionResultList;
    }

    public List<ConsumptionDTO> getConsumptionsByHouse(long houseId){
        if(houseId <= 0) throw new IllegalArgumentException("room id not valid");
        Optional<House> house = houseDAO.findById(houseId);
        if(house.isEmpty()) throw new NotFoundException("house not found");
        Optional<List<Room>> rooms = roomDAO.findAllByHouseHouseId(house.get().getHouseId());

        //security
        if(userSecurityFunc.getHouseUser(house.get().getHouseId()).isEmpty()) throw new NotFoundException("User is not part of this house");

        List<ConsumptionDTO> consumptionList = new ArrayList<>();
        if(rooms.isPresent()){
            for (Room room : rooms.get()){
                consumptionList.addAll(getConsumptionsByRoom(room.getRoomID()));
            }
        }
        return consumptionList;
    }

    public List<ConsumptionDTO> getConsumptionsByUser(){
        long userId = userSecurityFunc.getLoggedInUserId();
        Optional<User> user = userDAO.findById(userId);
        if(user.isEmpty()) throw new NotFoundException("User not found");

        List<House_User> house_user = house_userDAO.findAllByUser(user.get());

        List<ConsumptionDTO> consumptionDTOList = new ArrayList<>();
        for(House_User hu : house_user){
            consumptionDTOList.addAll(getConsumptionsByHouse(hu.getHouse().getHouseId()));
        }

        return consumptionDTOList;
    }

    public Consumption consumptionExists(long consumptionid){
        Optional<Consumption> consumption = consumptionDAO.findById(consumptionid);
        if(consumption.isEmpty()) throw new IllegalArgumentException("Consumption doesn't exist");
        if(userSecurityFunc.getHouseUser(consumption.get().getDevice().getRoom().getHouse().getHouseId()).isEmpty()) throw new NotFoundException("ccontroller.userpartof");
        return consumption.get();
    }

    private double rngDeviceConsumption(){
        int min = 1;
        int max = 2000;
        int random =  (int)Math.floor(Math.random()*(max-min+1)+min);
        return (double)random / 1000;
    }

    private ConsumptionDTO consumptionToConsumptionDTO(Consumption consumption){
        return new ConsumptionDTO.Builder()
                .consumptionId(consumption.getConsumptionId())
                .aantalMinuten(consumption.getAantalMinuten())
                .device(consumption.getDevice().getId())
                .unit(consumption.getUnit())
                .build();
    }

    private List<ConsumptionDTO> consumptionListToConsumptionDTOList(List<Consumption> lst) {
        Stream<ConsumptionDTO> stream = lst.stream()
                .map(rec-> new ConsumptionDTO.Builder()
                        .consumptionId(rec.getConsumptionId())
                        .aantalMinuten(rec.getAantalMinuten())
                        .device(rec.getDevice().getId())
                        .unit(rec.getUnit())
                        .consumptionPerHour(rec.getConsumptionPerHour())
                        .build());

        return stream.collect(Collectors.toList());
    }
}
