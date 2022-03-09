package com.ucll.smarthome.controllerREST;

import com.ucll.smarthome.controller.DeviceController;
import com.ucll.smarthome.dto.DeviceDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/devices")
public class DeviceRestController {

    private final DeviceController deviceController;

    public DeviceRestController(DeviceController deviceController) {
        this.deviceController = deviceController;
    }


    @PostMapping("/create")
    public ResponseEntity createDevice(@RequestBody DeviceDTO deviceDTO){
        try {
            deviceController.createDevice(deviceDTO);
            return new ResponseEntity("Device successfully created",HttpStatus.OK);
        }catch (IllegalArgumentException ex){
            return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update")
    public ResponseEntity updateDevice(@RequestBody DeviceDTO deviceDTO){
        try {
            deviceController.updateDevice(deviceDTO);
            return new ResponseEntity("Device successfully updated",HttpStatus.OK);

        }catch (IllegalArgumentException ex){
            return new ResponseEntity(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<DeviceDTO> getDeviceById(@PathVariable("id") long deviceid){
        try {
            return new ResponseEntity(deviceController.getDeviceById(deviceid),HttpStatus.OK);
        }catch (IllegalArgumentException ex){
            return new ResponseEntity(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/room/{id}")
    public ResponseEntity<List<DeviceDTO>> getDevicesByRoomId(@PathVariable("id") long roomid){
        try {
            return new ResponseEntity(deviceController.getDevicdsByRoom(roomid),HttpStatus.OK);
        }catch (IllegalArgumentException ex){
            return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("{id}/delete")
    public ResponseEntity deleteDevice(@PathVariable("id") long deviceid){
        try{
            deviceController.deleteDeviceById(deviceid);
            return new ResponseEntity("Device successfully deleted",HttpStatus.OK);

        }catch (IllegalArgumentException ex){
            return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("{id}/changestatus")
    public ResponseEntity changeStatus(@PathVariable("id") long deviceid){
        try{
            deviceController.changeStatus(deviceid);
            return new ResponseEntity("device status changed",HttpStatus.OK);
        }catch (IllegalArgumentException ex){
            return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
