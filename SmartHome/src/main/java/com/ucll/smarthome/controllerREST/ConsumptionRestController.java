package com.ucll.smarthome.controllerREST;

import com.ucll.smarthome.controller.ConsumptionController;
import com.ucll.smarthome.dto.ConsumptionDTO;
import liquibase.pro.packaged.H;
import liquibase.pro.packaged.R;
import org.atmosphere.config.service.Get;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consumptions")
public class ConsumptionRestController {

    private final ConsumptionController consumptionController;


    public ConsumptionRestController(ConsumptionController consumptionController) {
        this.consumptionController = consumptionController;
    }

    @PostMapping("/create")
    public ResponseEntity createConsumption(@RequestBody ConsumptionDTO consumptionDTO){
        try {
            consumptionController.createConsumption(consumptionDTO);
            return new ResponseEntity("Consumption succesfully created", HttpStatus.CREATED);
        }catch (IllegalArgumentException ex){
            return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/device/{id}")
    public ResponseEntity getConsumptionsByDevice(@PathVariable("id") long deviceid){
        try{
            return new ResponseEntity(consumptionController.getConsumptionsByDeviceId(deviceid), HttpStatus.OK);
        }catch (IllegalArgumentException ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
