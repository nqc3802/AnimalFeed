package com.example.animal_feed.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@CrossOrigin
@RequestMapping("/api/v1/user")
@RestController
public class UserController {
    @Autowired
    private UserService userService;
    
    @GetMapping
    public UserDTO getUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return userService.getUser(userDetails.getId());
    }
    
}
