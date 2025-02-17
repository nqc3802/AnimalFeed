package com.example.animal_feed.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByPhone(String phone);
    Users findById(int id);
}
