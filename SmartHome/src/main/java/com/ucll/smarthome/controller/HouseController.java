package com.ucll.smarthome.controller;

import com.ucll.smarthome.dto.HouseDTO;
import com.ucll.smarthome.persistence.entities.House;
import com.ucll.smarthome.persistence.entities.User;
import com.ucll.smarthome.persistence.repository.HouseDAO;
import com.ucll.smarthome.persistence.repository.UserDAO;
import com.vaadin.flow.router.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@Transactional
public class HouseController {
    private final HouseDAO dao;
    private final UserDAO userDAO;
    private final House_UserController house_userController;


    @Autowired
    public HouseController(HouseDAO dao, UserDAO userDAO, House_UserController house_userController) {
        this.dao = dao;
        this.userDAO = userDAO;
        this.house_userController = house_userController;
    }

    /**
     * Creating a house
     * @param houseDTO is the house that needs to be saved in the database
     * @throws IllegalArgumentException if something goes wrong with the info what went wrong
     */
    public void createHouse(HouseDTO houseDTO) throws IllegalArgumentException{
        if (houseDTO == null) throw new IllegalArgumentException("Creating house failed. Iputdata missing.");
        if (houseDTO.getName() == null || houseDTO.getName().length() == 0) throw new IllegalArgumentException("Creating house failed. Name of the house not filled in.");
        if (houseDTO.getUserid() <= 0L) throw new IllegalArgumentException("Can't create without a user id");

        Optional<User> user = userDAO.findById(houseDTO.getUserid());
        if (user.isEmpty()) throw new IllegalArgumentException("User does not exist");

        House house = houseDtoToHouse(houseDTO);
        dao.save(house);

        house_userController.createRegistratoinHouseUser(house,user.get());

    }

    /**
     * Updating a house
     * @param houseDTO with the values that needs to be updated
     * @throws IllegalArgumentException if something goes wrong with the info what went wrong
     */
    public void updateHouse(HouseDTO houseDTO) throws IllegalArgumentException{

        if (houseDTO == null) throw new IllegalArgumentException("Updating house failed. Iputdata missing.");
        if (houseDTO.getName() == null || houseDTO.getName().trim().equals("")) throw new IllegalArgumentException("Updatin house failed. Name of the house not filled in.");



        Optional<House> h = dao.findById(houseDTO.getId());
        if (h.isEmpty()) {
            throw new NotFoundException("The house you want to update is not found");
        } else {
            h.get().setName(houseDTO.getName());
        }

    }

    /**
     * Get one house
     * @param houseId id to search house
     * @return houseDTO if house is found
     * @throws IllegalArgumentException if something goes wrong with the info what went wrong
     */
    public HouseDTO getHouseById(long houseId) throws IllegalArgumentException{
        if (houseId <= 0L) throw new IllegalArgumentException("House Id is missing");
        Optional<House> house = dao.findById(houseId);
        if (house.isPresent()){
            return new HouseDTO.Builder().id(house.get().getHouseId()).name(house.get().getName()).build();
        }else {
            throw new IllegalArgumentException("No house found with id: " + houseId);
        }
    }

    /**
     * find the houses by a user
     * @param userid id to search for a user
     * @return list of housedto
     * @throws IllegalArgumentException if something goes wrong with the info what went wrong
     */
    public List<HouseDTO> getHousesByUser(long userid) throws IllegalArgumentException{
        if (userid <= 0L) throw new IllegalArgumentException("Invalid id");
        Optional<User> u = userDAO.findById(userid) ;
        if (u.isEmpty()){
            throw new IllegalArgumentException("User not found to get houses");
        }else{
            Stream<HouseDTO> stream =  house_userController.getByUser(u.get()).stream()
                    .map(rec -> new HouseDTO.Builder()
                            .id(rec.getHouse().getHouseId())
                            .name(rec.getHouse().getName())
                            .userid(rec.getUser().getId())
                            .build());
            return stream.collect(Collectors.toList());
        }

    }

    /**
     * @return all houses
     */
    public List<HouseDTO> getAllHouses(){
        Stream<HouseDTO> stream = dao.findAll().stream()
                .map(rec-> new HouseDTO.Builder()
                        .id(rec.getHouseId())
                        .name(rec.getName()).build());
        return stream.collect(Collectors.toList());
    }

    /**
     * Deletes house with his registrations house_user
     * @param houseid id to search house
     * @throws IllegalArgumentException if something goes wrong with the info what went wrong
     */
    public void deleteHouse(long houseid) throws IllegalArgumentException {
        if (houseid <= 0L) throw new IllegalArgumentException("Invalid id");

        //checking if the house exists
        getHouseById(houseid);


        house_userController.deleteRegistratieHouseUser(house_userController.getByHouse(dao.getById(houseid)));
        dao.deleteById(houseid);
    }


    private House houseDtoToHouse(HouseDTO houseDTO){
        return new House.Builder()
                .name(houseDTO.getName())
                .build();
    }
}
