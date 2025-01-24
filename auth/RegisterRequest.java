package com.example.animal_feed.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String phone;
    private String password;
    private String name;
    private String gender;
    private String email;
    private String address;
}
