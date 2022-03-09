package com.ucll.smarthome.controller;

import com.ucll.smarthome.dto.UserDTO;
import com.ucll.smarthome.functions.UserSecurityFunc;
import com.ucll.smarthome.persistence.entities.House;
import com.ucll.smarthome.persistence.entities.User;
import com.ucll.smarthome.persistence.repository.HouseDAO;
import com.ucll.smarthome.persistence.repository.UserDAO;
import com.vaadin.flow.router.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Controller
@Transactional
public class UserController {
    private static final Logger log = Logger.getLogger(UserController.class.getName());

    private final UserDAO dao;
    private final HouseDAO houseDAO;
    private final House_UserController house_userController;
    private final UserSecurityFunc userSecurityFunc;
    private final BCryptPasswordEncoder passwordEncoder;


    @Autowired
    public UserController(UserDAO dao, HouseDAO houseDAO, House_UserController house_userController, UserSecurityFunc userSecurityFunc, BCryptPasswordEncoder passwordEncoder) {
        this.dao = dao;
        this.houseDAO = houseDAO;
        this.house_userController = house_userController;
        this.userSecurityFunc = userSecurityFunc;
        this.passwordEncoder = passwordEncoder;
    }


    /**
     * Creates user in the database
     * @param userDTO that needs to be created
     * @throws IllegalArgumentException if something goes wrong with the info what went wrong
     */
    public void createUser(UserDTO userDTO) throws IllegalArgumentException{
        //General input check
        if (userDTO == null) throw new IllegalArgumentException("Creating user failed. Iputdata missing.");
        //required fields check
        if (userDTO.getUsername() == null || userDTO.getUsername().length() == 0) throw new IllegalArgumentException("Creating user failed. Username not filled in.");
        if (userDTO.getName() == null || userDTO.getName().length() == 0) throw new IllegalArgumentException("Creating user failed. Name not filled in.");
        if (userDTO.getFirstname() == null || userDTO.getFirstname().length() == 0) throw new IllegalArgumentException("Creating user failed. Firstname not filled in.");
        if (userDTO.getEmail() == null || userDTO.getEmail().length() == 0) throw new IllegalArgumentException("Creating user failed. Email not filled in.");
        if (userDTO.getPassword() == null || userDTO.getPassword().length() == 0) throw new IllegalArgumentException("Creating user failed. Password not filled in.");

        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        if(dao.findUserByUsername(userDTO.getUsername()).isPresent()) throw new IllegalArgumentException("This username is already taken.");

        User u = userDtoToUser(userDTO);
        dao.save(u);
    }

    /**
     * Updating a user
     * @param userDTO with data that needs to be updated
     * @throws IllegalArgumentException if something goes wrong with the info what went wrong
     */
    public void updateUser(UserDTO userDTO) throws IllegalArgumentException{
        if (userDTO == null) throw  new IllegalArgumentException("Updating user failed. Inputdata missing");
        if (userDTO.getId() <= 0L ) throw new IllegalArgumentException("User id is wrong, the user won't be found");
        if (userDTO.getUsername() == null || userDTO.getUsername().trim().equals("")) throw new IllegalArgumentException("Updating user failed. Username not filled in.");
        if (userDTO.getName() == null || userDTO.getName().trim().equals("")) throw new IllegalArgumentException("Updating user failed. Name not filled in.");
        if (userDTO.getFirstname() == null || userDTO.getFirstname().trim().equals("")) throw new IllegalArgumentException("Updating user failed. Firstname not filled in.");
        if (userDTO.getEmail() == null || userDTO.getEmail().trim().equals("")) throw new IllegalArgumentException("Updating user failed. Email not filled in.");
        //if (userDTO.getPassword() == null || userDTO.getPassword().trim().equals("")) throw new IllegalArgumentException("Updating user failed. Password not filled in.");

        long userId = userSecurityFunc.getLoggedInUserId();
        Optional<User> user = dao.findById(userId);
        if (user.isPresent()){
            user.get().setUsername(userDTO.getUsername());
            user.get().setName(userDTO.getName());
            user.get().setFirstname(userDTO.getFirstname());
            user.get().setEmail(userDTO.getEmail());

            //TODO maybe the ability to change password
        }else  {
            throw new NotFoundException("The user you want to update is not found");
        }

    }

    public List<UserDTO> getAllUsers(){
        return userListToUserDtoList(dao.findAll());
    }

    /**
     * @param userId to find user
     * @return userDTO of found user
     * @throws IllegalArgumentException if something goes wrong with the info what went wrong
     */
    public UserDTO getUserById(long userId) throws IllegalArgumentException{
        if (userId <= 0L) throw new IllegalArgumentException("User Id is missing");
        Optional<User> user = dao.findById(userId);
        if (user.isPresent()){
            return new UserDTO.Builder().id(user.get().getId()).username(user.get().getUsername()).name(user.get().getName())
                    .firstname(user.get().getFirstname()).email(user.get().getEmail()).build();
        }else {
            throw new IllegalArgumentException("No user found with id: " + userId);
        }
    }

    /**
     * get userid from security context
     * @return the current logged in user
     */
    public UserDTO getUser(){
        long userid = userSecurityFunc.getLoggedInUserId();
        return getUserById(userid);
    }

    /**
     * @param houseid to find house
     * @return a list of userDTO that are registered to the found house
     * @throws IllegalArgumentException if something goes wrong with the info what went wrong
     */
    public List<UserDTO> getUsersByHouse(long houseid) throws IllegalArgumentException{
        if (houseid <= 0L) throw new IllegalArgumentException("Invalid id");
        if(userSecurityFunc.getHouseUser(houseid).isEmpty()) throw new NotFoundException("User is not part of this house") ;

        Optional<House> h = houseDAO.findById(houseid) ;
        if (h.isEmpty()){
            throw new IllegalArgumentException("House not found to get users");
        }else{
              Stream<UserDTO>  stream =  house_userController.getByHouse(h.get()).stream()
                      .map(rec -> new UserDTO.Builder()
                              .id(rec.getUser().getId())
                              .name(rec.getUser().getName())
                              .firstname(rec.getUser().getFirstname())
                              .username(rec.getUser().getUsername())
                              .email(rec.getUser().getEmail())
                              .build());
              return stream.collect(Collectors.toList());
        }
    }

    /**
     * deletes a user and its registrations(house_user)
     * @throws IllegalArgumentException if something goes wrong with the info what went wrong
     */

    public void deleteUser() throws IllegalArgumentException {

        long userId = userSecurityFunc.getLoggedInUserId();
        //checking if the house exists
        getUserById(userId);

        house_userController.deleteRegistratieHouseUser(house_userController.getByUser(dao.getById(userId)));
        dao.deleteById(userId);
    }

    private User userDtoToUser(UserDTO userDTO){
        return new User.Builder()
                .username(userDTO.getUsername())
                .firstname(userDTO.getFirstname())
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .build();
    }

    private List<UserDTO> userListToUserDtoList(List<User> lst){
        Stream<UserDTO> stream = lst.stream()
                .map(rec-> new UserDTO.Builder()
                        .id(rec.getId())
                        .name(rec.getName())
                        .firstname(rec.getFirstname())
                        .email(rec.getEmail())
                        .username(rec.getUsername())
                        .build());
        return stream.collect(Collectors.toList());
    }
}
