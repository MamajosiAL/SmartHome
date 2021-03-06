package com.ucll.smarthome.controllerREST;

import com.ucll.smarthome.controller.LoginController;
import com.ucll.smarthome.dto.UserLoginDTO;
import liquibase.pro.packaged.R;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginRestController {

    private final LoginController loginController;

    public LoginRestController(LoginController loginController) {
        this.loginController = loginController;
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping
    public ResponseEntity Authentication(@RequestBody UserLoginDTO userLoginDTO) throws Exception {
        try{

            ResponseEntity re = new ResponseEntity("test",HttpStatus.CREATED);
            loginController.Authentication(userLoginDTO);
            return new ResponseEntity(userLoginDTO.getUsername() + " Logged in", HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
