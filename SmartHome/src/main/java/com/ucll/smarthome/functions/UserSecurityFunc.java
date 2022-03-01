package com.ucll.smarthome.functions;

import com.ucll.smarthome.config.UserPrincipal;
import com.ucll.smarthome.persistence.entities.House;
import com.ucll.smarthome.persistence.entities.House_User;
import com.ucll.smarthome.persistence.entities.User;
import com.ucll.smarthome.persistence.repository.HouseDAO;
import com.ucll.smarthome.persistence.repository.House_UserDAO;
import com.ucll.smarthome.persistence.repository.UserDAO;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.Optional;

@Service
public class UserSecurityFunc {

    private final House_UserDAO house_userDAO;
    private final HouseDAO houseDAO;
    private final UserDAO userDAO;

    public UserSecurityFunc(House_UserDAO house_userDAO, HouseDAO houseDAO, UserDAO userDAO) {
        this.house_userDAO = house_userDAO;
        this.houseDAO = houseDAO;
        this.userDAO = userDAO;
    }

    public long getLoggedInUserId(){
        try {
            UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return userPrincipal.getUser().getId();
        }catch (ClassCastException e){
            throw new NotFoundException("User does not exist");
        }
    }

    // checks if user is admin of house
    public Boolean checkCurrentUserIsAdmin(long houseId){
        Optional<House_User> house_user = getHouseUser(houseId);

        if(getHouseUser(houseId).isEmpty()){
            throw new NotFoundException("User is not part of this house");
        }else{
            return house_user.get().isAdmin();
        }
    }

    public Optional<House_User> getHouseUser(long houseId){
        Optional<User> user = userDAO.findById(getLoggedInUserId());
        Optional<House> house = houseDAO.findById(houseId);

        if(user.isEmpty()) throw new NotFoundException("User not found");
        if(house.isEmpty()) throw new NotFoundException("House not found");

        return house_userDAO.findByHouseAndUser(house.get(), user.get());
    }
}
