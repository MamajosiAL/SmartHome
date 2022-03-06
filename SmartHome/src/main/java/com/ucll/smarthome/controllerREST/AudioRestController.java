package com.ucll.smarthome.controllerREST;

import com.ucll.smarthome.controller.AudioController;
import com.ucll.smarthome.dto.AudioDTO;
import com.ucll.smarthome.dto.DeviceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/audios")
public class AudioRestController {

    private final AudioController audioController;

    @Autowired
    public AudioRestController(AudioController audioController) {
        this.audioController = audioController;
    }

    @PostMapping
    public ResponseEntity createAudioDevice(@RequestBody AudioDTO audioDTO){
        try {
            audioController.createAudioDevice(audioDTO);
            return new ResponseEntity("Audio device successfully created", HttpStatus.OK);
        }catch (IllegalArgumentException ex){
            return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity updateAudioDevice(@RequestBody AudioDTO audioDTO){
        try {
            audioController.updateAudioDevice(audioDTO);
            return new ResponseEntity("Audio device successfully updated",HttpStatus.OK);

        }catch (IllegalArgumentException ex){
            return new ResponseEntity(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<DeviceDTO> getAudioDeviceById(@PathVariable("id") long deviceid){
        try {
            return new ResponseEntity(audioController.getAudioDeviceById(deviceid),HttpStatus.OK);
        }catch (IllegalArgumentException ex){
            return new ResponseEntity(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/room/{id}")
    public ResponseEntity<List<DeviceDTO>> getAudioDevicesByRoom(@PathVariable("id") long roomid){
        try {
            return new ResponseEntity(audioController.getAdioDevicesByRoom(roomid),HttpStatus.OK);
        }catch (IllegalArgumentException ex){
            return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("{id}")
    public ResponseEntity deletAudioDevice(@PathVariable("id") long deviceid){
        try{
            audioController.deleteAudioDevice(deviceid);
            return new ResponseEntity("Audio device successfully deleted",HttpStatus.OK);

        }catch (IllegalArgumentException ex){
            return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("{id}/changestatus")
    public ResponseEntity changeStatus(@PathVariable("id") long audioid){
        try{
            audioController.changeStatus(audioid);
            return new ResponseEntity("audio status changed",HttpStatus.OK);
        }catch (IllegalArgumentException ex){
            return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
