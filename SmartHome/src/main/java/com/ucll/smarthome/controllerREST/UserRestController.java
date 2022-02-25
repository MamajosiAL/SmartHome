package com.ucll.smarthome.controllerREST;

import com.ucll.smarthome.controller.House_UserController;
import com.ucll.smarthome.controller.UserController;
import com.ucll.smarthome.dto.UserDTO;
import com.vaadin.flow.router.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/users")
public class UserRestController {

    private final UserController userController;
    private final House_UserController house_userController;

    @Autowired
    public UserRestController(UserController userController, House_UserController house_userController) {
        this.userController = userController;
        this.house_userController = house_userController;
    }

    @PostMapping
    public ResponseEntity createUser(@RequestBody UserDTO userDTO){

        try {
            userController.createUser(userDTO);
            return new  ResponseEntity("User succesfully created",HttpStatus.OK);

        }catch (IllegalArgumentException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping
    public ResponseEntity updateUser( @RequestBody UserDTO userDTO){
        try {

            userController.updateUser(userDTO);
            return new ResponseEntity(HttpStatus.OK);
        }catch (IllegalArgumentException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }catch (NotFoundException ex ){
            return new ResponseEntity(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public Optional<List<UserDTO>> getAllUsers(){
        return Optional.of(userController.getAllUsers());
    }
    @GetMapping("/house/{id}")
    public ResponseEntity<Optional<List<UserDTO>>> getUsersByHouse(@PathVariable("id")long houseid){
        try {
            return new ResponseEntity(Optional.ofNullable(userController.getUsersByHouse(houseid)),HttpStatus.OK) ;
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") long id){

        try {
            return new ResponseEntity(userController.getUserById(id),HttpStatus.OK);
        } catch (IllegalArgumentException e) {
           return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable("id") long id ){

        try {
            userController.deleteUser(id);
            return new ResponseEntity("User successful deleted",HttpStatus.ACCEPTED);

        }catch (IllegalArgumentException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


}
