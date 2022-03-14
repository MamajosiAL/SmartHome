package com.ucll.smarthome.controller;

import com.ucll.smarthome.dto.RoomDTO;
import com.ucll.smarthome.functions.UserSecurityFunc;
import com.ucll.smarthome.persistence.entities.House;
import com.ucll.smarthome.persistence.entities.Room;
import com.ucll.smarthome.persistence.repository.HouseDAO;
import com.ucll.smarthome.persistence.repository.RoomDAO;
import com.vaadin.flow.router.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@Transactional
public class RoomController {

    private final RoomDAO dao;
    private final HouseDAO houseDAO;
    private final UserSecurityFunc userSecurityFunc;

    @Autowired
    public RoomController(RoomDAO dao, HouseDAO houseDAO, UserSecurityFunc userSecurityFunc) {
        this.dao = dao;
        this.houseDAO = houseDAO;
        this.userSecurityFunc = userSecurityFunc;
    }

    public void createRoom(RoomDTO roomDTO) throws IllegalArgumentException {
        if (roomDTO == null) throw new IllegalArgumentException("Inputdata missing");
        if (roomDTO.getName() == null || roomDTO.getName().length() == 0) throw new IllegalArgumentException("Name for room is not filled in");
        if (roomDTO.getHouseid() <=0) throw new IllegalArgumentException("Invalid house id");

        Optional<House> house = houseDAO.findById(roomDTO.getHouseid());
        if (house.isEmpty()) throw new IllegalArgumentException("User does not exist");
        if(!userSecurityFunc.checkCurrentUserIsAdmin(house.get().getHouseId())) throw new AccessDeniedException("User is not admin of house");

        Room r = new Room.Builder().name(roomDTO.getName()).house(house.get()).build();
        dao.save(r);

    }

    public void updateRoom(RoomDTO roomDTO) throws IllegalArgumentException{
        if (roomDTO == null) throw new IllegalArgumentException("Inputdata missing");
        if (roomDTO.getName() == null || roomDTO.getName().trim().equals("")) throw new IllegalArgumentException("Name for room is not filled in");

        //TODO check if house is different then before

        Room r = roomExists(roomDTO.getId());
        if(!userSecurityFunc.checkCurrentUserIsAdmin(r.getHouse().getHouseId())) throw new AccessDeniedException("User is not admin of house");
        r.setName(roomDTO.getName());

    }

    public RoomDTO getRoomById(long roomid) throws IllegalArgumentException{
        if (roomid <= 0L) throw new IllegalArgumentException("User Id is missing");
        Room r = roomExists(roomid);
        if(userSecurityFunc.getHouseUser(r.getHouse().getHouseId()).isEmpty()) throw new NotFoundException("User is not part of this house");
        return new RoomDTO.Builder().id(r.getRoomID()).name(r.getName()).houseid(r.getHouse().getHouseId()).build();
    }

    public List<RoomDTO> getRoomsByHouse(long houseid) throws IllegalArgumentException{
        if (houseid <= 0) throw new IllegalArgumentException("Invalid id");
        if(userSecurityFunc.getHouseUser(houseid).isEmpty()) throw new NotFoundException("User is not part of this house");
        //Optional<List<Room>> lst = dao.findAllByHouseHouseId(houseid);
        Stream<RoomDTO> stream = dao.findAllByHouseHouseId(houseid).stream()
                .sorted(Comparator.comparing(Room::getRoomID))
                .map(rec -> new RoomDTO.Builder().id(rec.getRoomID()).name(rec.getName()).build());
        return stream.collect(Collectors.toList());
    }

    public void deleteRoom(long roomId) throws IllegalArgumentException {
        if (roomId <= 0) throw new IllegalArgumentException("Invalid id");

        Room r = roomExists(roomId);
        if(!userSecurityFunc.checkCurrentUserIsAdmin(r.getHouse().getHouseId())) throw new AccessDeniedException("User is not admin of house");
        dao.delete(r);
    }


    public Room roomExists(long roomid) throws IllegalArgumentException{
        Optional<Room> r = dao.findById(roomid);
        if (r.isPresent()) {
           return r.get();
        } else {
            throw new IllegalArgumentException("Room does not exist");
        }
    }
    private List<RoomDTO> roomListToDtoList(List<Room> lst){
        Stream<RoomDTO> stream = lst.stream()
                .map(rec-> new RoomDTO.Builder()
                        .id(rec.getRoomID())
                        .name(rec.getName())
                        .houseid(rec.getHouse().getHouseId())
                        .build());
        return stream.collect(Collectors.toList());
    }
}
