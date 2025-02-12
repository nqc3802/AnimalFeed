package com.example.animal_feed.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@CrossOrigin
@RequestMapping("/api/v1/user")
@RestController
public class UserController {
    @Autowired
    private UserService userService;
    
    @GetMapping("/{id}")
    @PreAuthorize("T(com.example.animal_feed.user.CustomUserDetails).cast(authentication.principal).id == #id")
    public UserDTO getUser(@PathVariable int id) {
        return userService.getUser(id);
    }
    
}
