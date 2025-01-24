package com.example.animal_feed.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByPhone(String phone);
}
