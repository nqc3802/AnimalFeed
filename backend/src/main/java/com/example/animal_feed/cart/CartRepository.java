package com.example.animal_feed.cart;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Carts, Integer> {
    Slice<Carts> findByUserId(int userId, Pageable pageable);
}
