package com.ucll.smarthome.controllerREST;

import com.ucll.smarthome.controller.House_UserController;
import com.ucll.smarthome.controller.UserController;
import com.ucll.smarthome.dto.UserDTO;
import com.vaadin.flow.router.NotFoundException;
import org.aspectj.weaver.ast.Not;
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

    @PostMapping("/create")
    public ResponseEntity createUser(@RequestBody UserDTO userDTO){
        try {
            userController.createUser(userDTO);
            return new  ResponseEntity("User successfully created",HttpStatus.OK);

        }catch (IllegalArgumentException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update")
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

    @GetMapping("user")
    public ResponseEntity getUser(){
        try{
            return new ResponseEntity(userController.getUser(), HttpStatus.OK);
        }catch (IllegalArgumentException ex){
            return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }catch (NotFoundException ex){
            return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
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
        }catch (NotFoundException ex ){
            return new ResponseEntity(ex.getMessage(),HttpStatus.NOT_FOUND);
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

    @DeleteMapping("/delete")
    public ResponseEntity deleteUser(){
        try {
            userController.deleteUser();
            return new ResponseEntity("User successful deleted",HttpStatus.ACCEPTED);

        }catch (IllegalArgumentException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }catch (NotFoundException ex ){
            return new ResponseEntity(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
}
