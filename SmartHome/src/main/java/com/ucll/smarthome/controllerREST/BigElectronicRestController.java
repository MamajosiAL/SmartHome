package com.ucll.smarthome.controllerREST;

import com.ucll.smarthome.controller.BigElectronicController;
import com.ucll.smarthome.dto.BigElectronicDTO;
import com.ucll.smarthome.dto.DeviceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appliances")
public class BigElectronicRestController {
    private final BigElectronicController beController;

    @Autowired
    public BigElectronicRestController(BigElectronicController beController) {
        this.beController = beController;
    }

    @PostMapping("/create")
    public ResponseEntity createApplianceDevice(@RequestBody BigElectronicDTO beDTO){
        try {
            beController.createApplianceDevice(beDTO);
            return new ResponseEntity("Appliance device successfully created", HttpStatus.OK);
        }catch (IllegalArgumentException ex){
            return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update")
    public ResponseEntity updateApplianceDevice(@RequestBody BigElectronicDTO beDTO){
        try {
            beController.updateBeDeviceDevice(beDTO);
            return new ResponseEntity("Appliance device successfully updated",HttpStatus.OK);

        }catch (IllegalArgumentException ex){
            return new ResponseEntity(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<DeviceDTO> getApplianceDeviceById(@PathVariable("id") long deviceid){
        try {
            return new ResponseEntity(beController.getDeviceByid(deviceid),HttpStatus.OK);
        }catch (IllegalArgumentException ex){
            return new ResponseEntity(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/room/{id}")
    public ResponseEntity<List<DeviceDTO>> getApplianceDevicesByRoom(@PathVariable("id") long roomid){
        try {
            return new ResponseEntity(beController.getApplianceDevicesByRoom(roomid),HttpStatus.OK);
        }catch (IllegalArgumentException ex){
            return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/{id}/delete")
    public ResponseEntity deleteApplianceDevice(@PathVariable("id") long deviceid){
        try{
            beController.deleteApplianceDeviceById(deviceid);
            return new ResponseEntity("Appliance device successfully deleted",HttpStatus.OK);

        }catch (IllegalArgumentException ex){
            return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("{id}/changestatus")
    public ResponseEntity changeStatus(@PathVariable("id") long bigElectorId){
        try{
            beController.changeStatus(bigElectorId);
            return new ResponseEntity("appliance status changed",HttpStatus.OK);
        }catch (IllegalArgumentException ex){
            return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
