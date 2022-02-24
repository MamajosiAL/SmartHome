package com.ucll.smarthome.controllerREST;

import com.ucll.smarthome.controller.RoomController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rooms")
public class RoomRestController {
    private final RoomController roomController;

    @Autowired
    public RoomRestController(RoomController roomController) {
        this.roomController = roomController;
    }
}
