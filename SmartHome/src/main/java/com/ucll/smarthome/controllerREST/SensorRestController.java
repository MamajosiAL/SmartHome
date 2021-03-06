package com.ucll.smarthome.controllerREST;

import com.ucll.smarthome.controller.SensorController;
import com.ucll.smarthome.dto.DeviceDTO;
import com.ucll.smarthome.dto.SensorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sensors")
public class SensorRestController {
    private final SensorController sensorController;

    @Autowired
    public SensorRestController(SensorController sensorController) {
        this.sensorController = sensorController;
    }

    @PostMapping("/create")
    public ResponseEntity createSensorDevice(@RequestBody SensorDTO sensorDTO){
        try {
            sensorController.createSensorDevice(sensorDTO);
            return new ResponseEntity("Sensor device successfully created", HttpStatus.OK);
        }catch (IllegalArgumentException ex){
            return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update")
    public ResponseEntity updateSenorDevice(@RequestBody SensorDTO sensorDTO){
        try {
            sensorController.updateSensorDevice(sensorDTO);
            return new ResponseEntity("Sensor device successfully updated",HttpStatus.OK);

        }catch (IllegalArgumentException ex){
            return new ResponseEntity(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeviceDTO> getSensorDeviceById(@PathVariable("id") long deviceid){
        try {
            return new ResponseEntity(sensorController.getSensorDeviceById(deviceid),HttpStatus.OK);
        }catch (IllegalArgumentException ex){
            return new ResponseEntity(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/room/{id}")
    public ResponseEntity<List<DeviceDTO>> getSenorDevicesByRoom(@PathVariable("id") long roomid){
        try {
            return new ResponseEntity(sensorController.getSonsorDevicesByRoom(roomid),HttpStatus.OK);
        }catch (IllegalArgumentException ex){
            return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
