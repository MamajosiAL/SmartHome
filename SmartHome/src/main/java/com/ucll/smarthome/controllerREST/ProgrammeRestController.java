package com.ucll.smarthome.controllerREST;

import com.ucll.smarthome.controller.ProgrammeConttoller;
import com.ucll.smarthome.dto.ProgrammeDTO;
import com.ucll.smarthome.dto.TypeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/programs")
public class ProgrammeRestController {

    private final ProgrammeConttoller programmeConttoller;

    @Autowired
    public ProgrammeRestController(ProgrammeConttoller programmeConttoller) {
        this.programmeConttoller = programmeConttoller;
    }
    @GetMapping("/{id}")
    public ResponseEntity<List<ProgrammeDTO>> getProgramsByType(@PathVariable("id") long typeid){
        return new ResponseEntity(programmeConttoller.getProgramsByTypeId(typeid), HttpStatus.OK);
    }

    @GetMapping("types")
    public ResponseEntity<List<TypeDTO>> getTypes(){
        return new ResponseEntity(programmeConttoller.getTypes(),HttpStatus.OK);
    }
}
