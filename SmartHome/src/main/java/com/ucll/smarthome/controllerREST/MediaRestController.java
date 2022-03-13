package com.ucll.smarthome.controllerREST;

import com.ucll.smarthome.controller.MediaController;
import com.ucll.smarthome.dto.MediaDTO;
import com.ucll.smarthome.dto.DeviceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/audios")
public class MediaRestController {

    private final MediaController mediaController;

    @Autowired
    public MediaRestController(MediaController mediaController) {
        this.mediaController = mediaController;
    }

    @PostMapping("/create")
    public ResponseEntity createAudioDevice(@RequestBody MediaDTO mediaDTO){
        try {
            mediaController.createAudioDevice(mediaDTO);
            return new ResponseEntity("Audio device successfully created", HttpStatus.OK);
        }catch (IllegalArgumentException ex){
            return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update")
    public ResponseEntity updateAudioDevice(@RequestBody MediaDTO mediaDTO){
        try {
            mediaController.updateAudioDevice(mediaDTO);
            return new ResponseEntity("Audio device successfully updated",HttpStatus.OK);

        }catch (IllegalArgumentException ex){
            return new ResponseEntity(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<DeviceDTO> getAudioDeviceById(@PathVariable("id") long deviceid){
        try {
            return new ResponseEntity(mediaController.getAudioDeviceById(deviceid),HttpStatus.OK);
        }catch (IllegalArgumentException ex){
            return new ResponseEntity(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/room/{id}")
    public ResponseEntity<List<DeviceDTO>> getAudioDevicesByRoom(@PathVariable("id") long roomid){
        try {
            return new ResponseEntity(mediaController.getAdioDevicesByRoom(roomid),HttpStatus.OK);
        }catch (IllegalArgumentException ex){
            return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
