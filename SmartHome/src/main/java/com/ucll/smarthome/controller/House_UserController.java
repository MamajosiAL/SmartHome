package com.ucll.smarthome.controller;

import com.ucll.smarthome.dto.HouseDTO;
import com.ucll.smarthome.dto.House_UserDTO;
import com.ucll.smarthome.functions.UserSecurityFunc;
import com.ucll.smarthome.persistence.entities.House;
import com.ucll.smarthome.persistence.entities.House_User;
import com.ucll.smarthome.persistence.entities.User;
import com.ucll.smarthome.persistence.repository.HouseDAO;
import com.ucll.smarthome.persistence.repository.House_UserDAO;
import com.ucll.smarthome.persistence.repository.UserDAO;
import com.vaadin.flow.router.NotFoundException;
import com.ucll.smarthome.persistence.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Controller
@Transactional
public class House_UserController {

    private final House_UserDAO dao;
    private final HouseDAO houseDAO;
    private final UserDAO userDAO;
    private final UserSecurityFunc userSecurityFunc;

    @Autowired
    public House_UserController(House_UserDAO dao, HouseDAO houseDAO, UserDAO userDAO, UserSecurityFunc userSecurityFunc) {
        this.dao = dao;
        this.houseDAO = houseDAO;
        this.userDAO = userDAO;
        this.userSecurityFunc = userSecurityFunc;
    }

    /**
     * @param h house that needs to get registered to a user
     * @param user that needs to get registered to a house
     * @throws IllegalArgumentException if something goes wrong with the info what went wrong
     */
    public void createRegistratoinHouseUser(House h , User user) throws IllegalArgumentException{

        if (h == null ) throw new IllegalArgumentException("House is needed");
        if (h.getHouseId()<= 0L || h.getHouseId() == null) throw new IllegalArgumentException("House id is missing");

        House_User hs = new House_User.Builder().house(h).user(user).isAdmin(true).isOwner(true).build();
        dao.save(hs);
    }

    public void registerUserToHouseNotOwner(HouseDTO houseDTO) throws IllegalArgumentException{
        if (houseDTO == null ) throw new IllegalArgumentException("House is needed");


        Optional<House> h = houseDAO.findById(houseDTO.getId());
        if (h.isEmpty()) throw new IllegalArgumentException("House couldn't be found");
        Optional<User> u = userDAO.findFirstByUsername(houseDTO.getUsername());
        if (u.isEmpty())throw new IllegalArgumentException("User couldn't be found");

        House_User hs = new House_User.Builder().house(h.get()).user(u.get()).isAdmin(false).isOwner(false).build();
        dao.save(hs);
    }



    public void updateUserSetAdmin(House_UserDTO house_userDTO) throws IllegalArgumentException{
        if (house_userDTO.getHouseid() <= 0) throw new  IllegalArgumentException("Invalid id");
        Optional<House> house = houseDAO.findById(house_userDTO.getHouseid());
        Optional<User> user = userDAO.findById(userSecurityFunc.getLoggedInUserId());
        if (house.isEmpty()|| user.isEmpty()) throw new  IllegalArgumentException("House or user not found");

        House_User hs = getHouseUserByHouseAndUser(house.get(), user.get());
        if(userSecurityFunc.checkCurrentUserIsOwner(hs.getHouse().getHouseId())) throw new NotFoundException("logged in user is not owner of this house");
        hs.setAdmin(house_userDTO.getIsadmin());
    }

    /**
     * @param user to search house_user objects
     * @return a list of house_user(registrations) by user
     */
    public List<House_User> getByUser(User user) throws IllegalArgumentException{
        if (user == null) throw new IllegalArgumentException("Need to give the user to get list");
        return dao.findAllByUser(user);

    }

    /**
     * @param house to search house_user objects
     * @return a list of house_user(registrations) by house
     */
    public List<House_User> getByHouse(House house) throws IllegalArgumentException{
        if (house == null) throw new IllegalArgumentException("Need to give the house to get list");
        return dao.findAllByHouse(house);
    }


    /**
     * @param lst list of registrations house_user that needs to be deleted
     */
    public void deleteRegistratieHouseUser(List<House_User> lst) {
        dao.deleteAll(lst);
    }

    /**
     * Delete single house_user
     * @param house_user
     */
    public void deleteSingleHouseUser(House_User house_user){
        dao.delete(house_user);
    }

    /**
     * Get house_user by :
     * @param house object to find house_user in dao
     * @param user object to find house_user in dao
     * @return house_user
     */
    public House_User getHouseUserByHouseAndUser(House house, User user){
        Optional<House_User> house_user = dao.findByHouseAndUser(house,user);
        if(house_user.isEmpty()) throw new NotFoundException("user is not part of house");
        return house_user.get();
    }

    /**
     * Get house_user by:
     * @param houseid id to find house_user in dao
     * @param userid id to find house_user in dao
     * @return house_user
     */
    public House_User getHouseUserByHouseIdAndUserId(long houseid, long userid){
        if (houseid <= 0L) throw new IllegalArgumentException("Invalid houseid");
        if (userid <= 0L) throw new IllegalArgumentException("Invalid userid");

        Optional<House> house = houseDAO.findById(houseid);
        Optional<User> user = userDAO.findById(userid);
        if(house.isEmpty()) throw new NotFoundException("house not found");
        if(user.isEmpty()) throw new NotFoundException("user not found");

        return getHouseUserByHouseAndUser(house.get(),user.get());
    }

    private House_User houseUserExist(long id) throws IllegalArgumentException{

       Optional<House_User> hs = dao.findById(id);
       if (hs.isPresent()){
           return hs.get();
       }else{
           throw new IllegalArgumentException("Registration not found");
       }

    }

}
