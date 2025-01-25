package com.example.animal_feed.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersDTO {
    private String phone;
    private Role role;
    private String name;
    private String gender;
    private String email;
    private String address;
    private String state;
}
