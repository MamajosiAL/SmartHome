package com.ucll.smarthome.controller;


import com.ucll.smarthome.config.UserDetailService;
import com.ucll.smarthome.dto.UserLoginDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;

@Controller
@Transactional
public class LoginController {

    private final UserDetailService userDetailService;

    public LoginController(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    // Log de user in met
    public UserDetails Authentication(UserLoginDTO userLoginDTO) throws Exception {
        return userDetailService.Login(userLoginDTO);
    }


}
