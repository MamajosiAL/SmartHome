package com.ucll.smarthome.controller;

import com.ucll.smarthome.dto.RoomDTO;
import com.ucll.smarthome.dto.UserDTO;
import com.ucll.smarthome.persistence.entities.House;
import com.ucll.smarthome.persistence.entities.Room;
import com.ucll.smarthome.persistence.entities.User;
import com.ucll.smarthome.persistence.repository.HouseDAO;
import com.ucll.smarthome.persistence.repository.RoomDAO;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@Transactional
public class RoomController {

    private final RoomDAO dao;
    private final HouseDAO houseDAO;

    public RoomController(RoomDAO dao, HouseDAO houseDAO) {
        this.dao = dao;
        this.houseDAO = houseDAO;
    }

    public void createRoom(RoomDTO roomDTO) throws IllegalArgumentException{
        if (roomDTO == null) throw new IllegalArgumentException("Inputdata missing");
        if (roomDTO.getName() == null || roomDTO.getName().length() == 0) throw new IllegalArgumentException("Name for room is not filled in");
        if (roomDTO.getHouseid() <=0) throw new IllegalArgumentException("Invalid house id");


        Optional<House> house = houseDAO.findById(roomDTO.getHouseid());
        if (house.isEmpty()) throw new IllegalArgumentException("User does not exist");

        Room r = new Room.Builder().name(roomDTO.getName()).house(house.get()).build();

        dao.save(r);

    }

    public void updateRoom(RoomDTO roomDTO) throws IllegalArgumentException{
        if (roomDTO == null) throw new IllegalArgumentException("Inputdata missing");
        if (roomDTO.getName() == null || roomDTO.getName().trim().equals("")) throw new IllegalArgumentException("Name for room is not filled in");
        //TODO check if house is different then before

        Room r = roomExists(roomDTO.getId());
        r.setName(roomDTO.getName());

    }

    public RoomDTO getRoomById(long roomid) throws IllegalArgumentException{
        if (roomid <= 0L) throw new IllegalArgumentException("User Id is missing");
        Room r = roomExists(roomid);

        return new RoomDTO.Builder().id(r.getRoomID()).name(r.getName()).houseid(r.getHouse().getHouseId()).build();
    }

    public List<RoomDTO> getRoomsByHouse(long houseid) throws IllegalArgumentException{

        if (houseid <= 0) throw new IllegalArgumentException("Invalid id");
        Optional<List<Room>> lst = dao.findAllByHouseHouseId(houseid);
        if (lst.isEmpty()) throw new IllegalArgumentException("Couldn't find anny rooms");
        return roomListToDtoList(lst.get());

    }

    public void deleteRoom(long roomId) throws IllegalArgumentException {
        if (roomId <= 0) throw new IllegalArgumentException("Invalid id");

        Room r = roomExists(roomId);

        dao.delete(r);
    }


    private Room roomExists(long roomid ) throws IllegalArgumentException{
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
