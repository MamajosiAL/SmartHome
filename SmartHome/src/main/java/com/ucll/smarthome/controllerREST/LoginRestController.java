package com.ucll.smarthome.controllerREST;

import com.ucll.smarthome.controller.LoginController;
import com.ucll.smarthome.dto.UserLoginDTO;
import org.springframework.http.HttpStatus;
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
    @PostMapping("/user")
    public UserDetails Authentication(@RequestBody UserLoginDTO userLoginDTO) throws Exception {
        return loginController.Authentication(userLoginDTO);
    }
}
