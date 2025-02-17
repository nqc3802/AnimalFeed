package com.example.animal_feed.cart;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Carts, Integer> {
    Slice<Carts> findByUserId(int userId, Pageable pageable);
    Optional<Carts> findByUserIdAndItemId(int userId, int itemId);
}
