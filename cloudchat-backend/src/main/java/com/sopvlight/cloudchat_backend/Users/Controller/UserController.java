package com.sopvlight.cloudchat_backend.Users.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sopvlight.cloudchat_backend.Security.Auth.AuthErrors.AuthorizationFailureException;
import com.sopvlight.cloudchat_backend.Exception.GeneralException;
import com.sopvlight.cloudchat_backend.Security.Auth.Model.UserData;
import com.sopvlight.cloudchat_backend.Users.DTO.TokenDTO;
import com.sopvlight.cloudchat_backend.Users.DTO.UserDTO;
import com.sopvlight.cloudchat_backend.Users.Entity.Users;
import com.sopvlight.cloudchat_backend.Users.Service.UserService;
@RestController
@RequestMapping("/user")
public class UserController {
    private UserService service;
    @Autowired
    public UserController(UserService service){
        this.service=service;
    }
    @PostMapping("/register")
    public ResponseEntity<TokenDTO> registerUser(@AuthenticationPrincipal UserData details, @RequestBody Users user) throws GeneralException{
        return new ResponseEntity<>(service.register(user));
    }
    @PostMapping("/login")
    public ResponseEntity<TokenDTO> loginUser(@RequestBody UserDTO user) throws AuthorizationFailureException{
        return new ResponseEntity<>(service.login(user),HttpStatus.OK);
    }
    @PostMapping("/refresh")
    public ResponseEntity<TokenDTO> refreshUser(@RequestHeader(value = "refresh-token",required = false) String refreshToken) throws AuthorizationFailureException{
        return new ResponseEntity<>(service.refresh(refreshToken),HttpStatus.OK);
    }
    @PostMapping("/logout")
    public ResponseEntity<TokenDTO> logoutUser(@AuthenticationPrincipal UserDetails uds) throws AuthorizationFailureException{
        service.endSession(uds);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
