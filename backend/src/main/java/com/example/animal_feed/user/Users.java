package com.example.animal_feed.user;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Table(name = "user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NonNull
    private String phone;
    @NonNull
    private String password;
    @NonNull
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;
    @NonNull
    private String name;
    @NonNull
    private String gender;
    @NonNull
    @Builder.Default
    private String email = "";
    @NonNull
    private String address;
    @NonNull
    @Builder.Default
    private String state = "ACTIVE";
    
}
