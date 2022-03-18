package com.ucll.smarthome.controllerREST;

import com.ucll.smarthome.controller.ConsumptionLogController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/consumptionlogs")
public class ConsumptionLogRestController {

    private final ConsumptionLogController consumptionLogController;

    public ConsumptionLogRestController(ConsumptionLogController consumptionLogController) {
        this.consumptionLogController = consumptionLogController;
    }

    @GetMapping("/device/{id}")
    public ResponseEntity getConsumptionLogsByDevice(@PathVariable("id") long deviceid){
        try{
            return new ResponseEntity(consumptionLogController.getConsumptionLogByDevice(deviceid), HttpStatus.OK);
        }catch (IllegalArgumentException ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/room/{id}")
    public ResponseEntity getConsumptionLogsByRoom(@PathVariable("id") long roomid){
        try{
            return new ResponseEntity(consumptionLogController.getConsumptionLogByRoom(roomid), HttpStatus.OK);
        }catch (IllegalArgumentException ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/house/{id}")
    public ResponseEntity getConsumptionLogsByHouse(@PathVariable("id") long roomid){
        try{
            return new ResponseEntity(consumptionLogController.getConsumptionLogByHouse(roomid), HttpStatus.OK);
        }catch (IllegalArgumentException ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user")
    public ResponseEntity getConsumptionLogsByUser(){
        try{
            return new ResponseEntity(consumptionLogController.getConsumptionLogByUser(), HttpStatus.OK);
        }catch (IllegalArgumentException ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/createData")
    public ResponseEntity createData(){
        consumptionLogController.createData();
        return new ResponseEntity("data created",HttpStatus.OK);
    }

}
