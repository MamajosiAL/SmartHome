package com.ucll.smarthome.controllerREST;

import com.ucll.smarthome.controller.RoomController;
import com.ucll.smarthome.dto.RoomDTO;
import com.ucll.smarthome.dto.UserDTO;
import com.vaadin.flow.router.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rooms")
public class RoomRestController {
    private final RoomController roomController;

    @Autowired
    public RoomRestController(RoomController roomController) {
        this.roomController = roomController;
    }

    @PostMapping
    public ResponseEntity createRoom(@RequestBody RoomDTO roomDTO){

        try {
            roomController.createRoom(roomDTO);
            return new  ResponseEntity("Room succesfully created", HttpStatus.OK);

        }catch (IllegalArgumentException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }catch (AccessDeniedException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.FORBIDDEN);
        }

    }
    @PutMapping
    public ResponseEntity updateRoom( @RequestBody RoomDTO roomDTO){
        try {
            roomController.updateRoom(roomDTO);
            return new ResponseEntity(HttpStatus.OK);
        }catch (IllegalArgumentException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }catch (NotFoundException ex ){
            return new ResponseEntity(ex.getMessage(),HttpStatus.NOT_FOUND);
        }catch (AccessDeniedException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.FORBIDDEN);
        }
    }


    @GetMapping("/house/{id}")
    public ResponseEntity<Optional<List<RoomDTO>>> getRoomsByHouse(@PathVariable("id")long houseid){
        try {
            return new ResponseEntity(Optional.ofNullable(roomController.getRoomsByHouse(houseid)),HttpStatus.OK) ;
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getRoomById(@PathVariable("id") long id){

        try {
            return new ResponseEntity(roomController.getRoomById(id),HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteRoom(@PathVariable("id") long id ){

        try {
            roomController.deleteRoom(id);
            return new ResponseEntity("User successful deleted",HttpStatus.ACCEPTED);

        }catch (IllegalArgumentException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }catch (AccessDeniedException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.FORBIDDEN);
        }
    }

}
