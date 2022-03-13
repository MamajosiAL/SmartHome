package com.ucll.smarthome.controllerREST;

import com.ucll.smarthome.controller.HouseController;
import com.ucll.smarthome.controller.House_UserController;
import com.ucll.smarthome.dto.HouseDTO;
import com.ucll.smarthome.dto.House_UserDTO;
import com.vaadin.flow.router.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/houses")
public class HouseRestController {
    private final HouseController houseController;
    private final House_UserController house_userController;

    @Autowired
    public HouseRestController(HouseController houseController, House_UserController house_userController) {
        this.houseController = houseController;
        this.house_userController = house_userController;
    }

    @PostMapping("/create")
    public ResponseEntity createHouse(@RequestBody HouseDTO houseDTO){

        try {
            houseController.createHouse(houseDTO);
            return new  ResponseEntity("House succesfully created",HttpStatus.OK);

        }catch (IllegalArgumentException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/register")
    public ResponseEntity registerUserToHouseNotOwner(@RequestBody HouseDTO houseDTO){
        try{
            house_userController.registerUserToHouseNotOwner(houseDTO);
            return new ResponseEntity("User successfully added to your house", HttpStatus.OK);
        }catch (IllegalArgumentException ex){
            return new ResponseEntity(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/setadmin")
    public ResponseEntity updateUseIsAdmin(@RequestBody House_UserDTO house_userDTO){
        try {
            house_userController.updateUserSetAdmin(house_userDTO);
            return new  ResponseEntity("Is updated",HttpStatus.ACCEPTED);
        }catch (IllegalArgumentException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }catch (NotFoundException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update")
    public ResponseEntity updateHouse( @RequestBody HouseDTO houseDTO){
        try {
            houseController.updateHouse(houseDTO);
            return new ResponseEntity("Changes made",HttpStatus.OK);
        }catch (IllegalArgumentException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }catch (NotFoundException ex ){
            return new ResponseEntity(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public Optional<List<HouseDTO>> getAllHouses(){
        return Optional.of(houseController.getAllHouses());
    }


    @GetMapping("/user")
    public ResponseEntity<Optional<List<HouseDTO>>> getHousesByUser(){
        try {
            return new ResponseEntity<>(Optional.ofNullable(houseController.getHousesByUser()),HttpStatus.OK);
        }catch (IllegalArgumentException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }catch (NotFoundException ex ){
            return new ResponseEntity(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<HouseDTO> getHouseById(@PathVariable("id") long id){

        try {
            return new ResponseEntity(houseController.getHouseById(id),HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }catch (NotFoundException ex ){
            return new ResponseEntity(ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity deleteHouse(@PathVariable("id") long id ){

        try {
            houseController.deleteHouse(id);
            return new ResponseEntity("House successful deleted",HttpStatus.ACCEPTED);
        }catch (IllegalArgumentException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }catch (AccessDeniedException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{houseid}/user/{userid}")
    public ResponseEntity deleteUserFromHouse(@PathVariable("houseid") long houseid, @PathVariable("userid") long userid){
        try {
            houseController.deleteUserFromHouse(houseid,userid);
            return new ResponseEntity("User is successfully deleted from hosue", HttpStatus.ACCEPTED);
        }catch (IllegalArgumentException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }catch (NotFoundException ex ){
            return new ResponseEntity(ex.getMessage(),HttpStatus.NOT_FOUND);
        }catch (AccessDeniedException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.FORBIDDEN);
        }
    }
}
