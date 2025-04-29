package org.fm.backend.controller;

import org.fm.backend.dto.LoginToken;
import org.fm.backend.dto.RegisterInfo;
import org.fm.backend.dto.ResultMessage;
import org.fm.backend.model.User;
import org.fm.backend.service.UserService;
import org.fm.backend.util.JWTHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/User")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JWTHelper jwtHelper;

    @GetMapping("/Login")
    public LoginToken login(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String role) {
        return userService.login(email, password, role);
    }

    @PostMapping("/Register")
    public ResultMessage register(@RequestBody RegisterInfo registerInfo){
        return userService.register(registerInfo);
    }

    @GetMapping("/GetPersonalProfile")
    public User getPersonalProfile (String token){
        return userService.getProfile(token);
    }

}
