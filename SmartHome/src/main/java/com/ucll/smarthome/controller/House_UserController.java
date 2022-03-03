package com.ucll.smarthome.controller;

import com.ucll.smarthome.dto.HouseDTO;
import com.ucll.smarthome.dto.House_UserDTO;
import com.ucll.smarthome.persistence.entities.House;
import com.ucll.smarthome.persistence.entities.House_User;
import com.ucll.smarthome.persistence.entities.User;
import com.ucll.smarthome.persistence.repository.HouseDAO;
import com.ucll.smarthome.persistence.repository.House_UserDAO;
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

    @Autowired
    public House_UserController(House_UserDAO dao, HouseDAO houseDAO, UserDAO userDAO) {
        this.dao = dao;
        this.houseDAO = houseDAO;
        this.userDAO = userDAO;
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
        if (houseDTO.getId()<= 0L || houseDTO.getUserid() <= 0) throw new IllegalArgumentException("House id is missing");

        Optional<House> h = houseDAO.findById(houseDTO.getId());
        if (h.isEmpty()) throw new IllegalArgumentException("House couldn't be found");
        Optional<User> u = userDAO.findById(houseDTO.getUserid());
        if (u.isEmpty())throw new IllegalArgumentException("User couldn't be found");

        House_User hs = new House_User.Builder().house(h.get()).user(u.get()).isAdmin(false).isOwner(false).build();
        dao.save(hs);
    }



    public void updateRegistrationHouseUsser(House_UserDTO house_userDTO) throws IllegalArgumentException{
        if (house_userDTO.getId() <= 0) throw new  IllegalArgumentException("Ivalid id");
        House_User hs = houseUserExist(house_userDTO.getId());
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

    private House_User houseUserExist(long id) throws IllegalArgumentException{

       Optional<House_User> hs = dao.findById(id);
       if (hs.isPresent()){
           return hs.get();
       }else{
           throw new IllegalArgumentException("Registration not found");
       }

    }

}
