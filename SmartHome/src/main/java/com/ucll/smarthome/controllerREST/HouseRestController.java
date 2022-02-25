package com.ucll.smarthome.controllerREST;

import com.ucll.smarthome.controller.HouseController;
import com.ucll.smarthome.controller.House_UserController;
import com.ucll.smarthome.dto.HouseDTO;
import com.ucll.smarthome.dto.House_UserDTO;
import com.vaadin.flow.router.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity createHouse(@RequestBody HouseDTO houseDTO){

        try {
            houseController.createHouse(houseDTO);
            return new  ResponseEntity("House succesfully created",HttpStatus.OK);

        }catch (IllegalArgumentException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/isadmin")
    public ResponseEntity updateUseIsAdmin(@RequestBody House_UserDTO house_userDTO){
        try {

            house_userController.updateRegistrationHouseUsser(house_userDTO);
            return new  ResponseEntity("Is updated",HttpStatus.ACCEPTED);
        }catch (IllegalArgumentException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping
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

    @GetMapping("/user/{id}")
    public ResponseEntity<Optional<List<HouseDTO>>> getHousesByUser(@PathVariable("id") long userid){
        try {
            return new ResponseEntity<>(Optional.ofNullable(houseController.getHousesByUser(userid)),HttpStatus.OK);
        }catch (IllegalArgumentException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }
    @GetMapping("/{id}")
    public ResponseEntity<HouseDTO> getHouseById(@PathVariable("id") long id){

        try {
            return new ResponseEntity(houseController.getHouseById(id),HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity deleteHouse(@PathVariable("id") long id ){

        try {
            houseController.deleteHouse(id);
            return new ResponseEntity("House successful deleted",HttpStatus.ACCEPTED);

        }catch (IllegalArgumentException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
