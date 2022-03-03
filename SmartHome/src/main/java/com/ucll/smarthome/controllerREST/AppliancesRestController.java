package com.ucll.smarthome.controllerREST;

import com.ucll.smarthome.controller.AppliancesController;
import com.ucll.smarthome.dto.AppliancesDTO;
import com.ucll.smarthome.dto.DeviceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appliances")
public class AppliancesRestController {
    private final AppliancesController aplController;

    @Autowired
    public AppliancesRestController(AppliancesController aplController) {
        this.aplController = aplController;
    }

    @PostMapping
    public ResponseEntity createApplianceDevice(@RequestBody AppliancesDTO aplDTO){
        try {
            aplController.createApplianceDevice(aplDTO);
            return new ResponseEntity("Appliance device successfully created", HttpStatus.OK);
        }catch (IllegalArgumentException ex){
            return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity updateApplianceDevice(@RequestBody AppliancesDTO aplDTO){
        try {
            aplController.updateApplianceDevice(aplDTO);
            return new ResponseEntity("Appliance device successfully updated",HttpStatus.OK);

        }catch (IllegalArgumentException ex){
            return new ResponseEntity(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<DeviceDTO> getApplianceDeviceById(@PathVariable("id") long deviceid){
        try {
            return new ResponseEntity(aplController.getDeviceByid(deviceid),HttpStatus.OK);
        }catch (IllegalArgumentException ex){
            return new ResponseEntity(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/room/{id}")
    public ResponseEntity<List<DeviceDTO>> getApplianceDevicesByRoom(@PathVariable("id") long roomid){
        try {
            return new ResponseEntity(aplController.getApplianceDevicesByRoom(roomid),HttpStatus.OK);
        }catch (IllegalArgumentException ex){
            return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("{id}")
    public ResponseEntity deleteApplianceDevice(@PathVariable("id") long deviceid){
        try{
            aplController.deleteApplianceDeviceById(deviceid);
            return new ResponseEntity("Appliance device successfully deleted",HttpStatus.OK);

        }catch (IllegalArgumentException ex){
            return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
