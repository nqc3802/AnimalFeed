package com.example.animal_feed.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String phone;
    private String name;
    private String gender;
    private String email;
    private String address;
    private String state;
}
