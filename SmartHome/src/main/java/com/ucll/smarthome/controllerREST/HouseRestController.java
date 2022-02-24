package com.ucll.smarthome.controllerREST;

import com.ucll.smarthome.controller.HouseController;
import com.ucll.smarthome.dto.HouseDTO;
import com.ucll.smarthome.dto.UserDTO;
import com.vaadin.flow.router.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/houses")
public class HouseRestController {
    private final HouseController houseController;

    @Autowired
    public HouseRestController(HouseController houseController) {
        this.houseController = houseController;
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

    @PutMapping("/{id}")
    public ResponseEntity updateHouse(@PathVariable("id") long id, @RequestBody HouseDTO houseDTO){
        try {
            houseDTO.setId(id);
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
    public Optional<List<HouseDTO>> getHousesByUser(@PathVariable("id") long userid){
        return Optional.ofNullable(houseController.getHousesByUser(userid));
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
