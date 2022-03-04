package com.ucll.smarthome.controller;

import com.ucll.smarthome.dto.HouseDTO;

import com.ucll.smarthome.functions.UserSecurityFunc;

import com.ucll.smarthome.persistence.entities.House;
import com.ucll.smarthome.persistence.entities.House_User;
import com.ucll.smarthome.persistence.entities.User;
import com.ucll.smarthome.persistence.repository.HouseDAO;
import com.ucll.smarthome.persistence.repository.UserDAO;
import com.vaadin.flow.router.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
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
    private final UserSecurityFunc userSecurityFunc;


    @Autowired
    public HouseController(HouseDAO dao, UserDAO userDAO, House_UserController house_userController, UserSecurityFunc userSecurityFunc) {
        this.dao = dao;
        this.userDAO = userDAO;
        this.house_userController = house_userController;
        this.userSecurityFunc = userSecurityFunc;
    }

    /**
     * Creating a house
     * @param houseDTO is the house that needs to be saved in the database
     * @throws IllegalArgumentException if something goes wrong with the info what went wrong
     */
    public void createHouse(HouseDTO houseDTO) throws IllegalArgumentException{
        if (houseDTO == null) throw new IllegalArgumentException("Creating house failed. Iputdata missing.");
        if (houseDTO.getName() == null || houseDTO.getName().length() == 0) throw new IllegalArgumentException("Creating house failed. Name of the house not filled in.");

        long userId = userSecurityFunc.getLoggedInUserId();

        Optional<User> user = userDAO.findById(userId);
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

        long userId = userSecurityFunc.getLoggedInUserId();

        Optional<House> h = dao.findById(userId);

        if (h.isPresent()) {
            if(userSecurityFunc.checkCurrentUserIsAdmin(h.get().getHouseId())){
                h.get().setName(houseDTO.getName());
            }else{
                throw new AccessDeniedException("You are not an admin of this house.");
            }
        } else {
            throw new NotFoundException("The house you want to update is not found.");
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

        if (house.isEmpty()) throw new IllegalArgumentException("No house found with id: " + houseId);
        if (userSecurityFunc.getHouseUser(houseId).isEmpty()) throw new NotFoundException("user has no access to this house");

        return new HouseDTO.Builder().id(house.get().getHouseId()).name(house.get().getName()).build();
    }

    /**
     * find the houses by a user
     * @return list of housedto
     * @throws IllegalArgumentException if something goes wrong with the info what went wrong
     */
    public List<HouseDTO> getHousesByUser() throws IllegalArgumentException{

        long userId = userSecurityFunc.getLoggedInUserId();

        Optional<User> u = userDAO.findById(userId) ;
        if (u.isEmpty()){
            throw new IllegalArgumentException("User not found to get houses");
        }else{
            Stream<HouseDTO> stream =  house_userController.getByUser(u.get()).stream()
                    .map(rec -> new HouseDTO.Builder()
                            .id(rec.getHouse().getHouseId())
                            .name(rec.getHouse().getName())
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
    public void deleteHouse(long houseid) throws IllegalArgumentException{
        if (houseid <= 0L) throw new IllegalArgumentException("Invalid id");

        if(!userSecurityFunc.checkCurrentUserIsOwner(houseid))  throw new AccessDeniedException("The house you want to delete is not yours");

        house_userController.deleteRegistratieHouseUser(house_userController.getByHouse(dao.getById(houseid)));
        dao.deleteById(houseid);
    }

    /**
     * Delete user from house as Owner
     * @param houseid house id to find house_user
     * @param userid user id to find house_user
     */
    public void deleteUserFromHouse(long houseid, long userid){
        long loggedInUserId = userSecurityFunc.getLoggedInUserId();
        if(!userSecurityFunc.checkCurrentUserIsOwner(loggedInUserId)) throw new AccessDeniedException("Logged in user is not owner of this house");

        House_User house_user = house_userController.getHouseUserByHouseIdAndUserId(houseid,userid);
        house_userController.deleteSingleHouseUser(house_user);
    }


    private House houseDtoToHouse(HouseDTO houseDTO){
        return new House.Builder()
                .name(houseDTO.getName())
                .build();
    }
}
