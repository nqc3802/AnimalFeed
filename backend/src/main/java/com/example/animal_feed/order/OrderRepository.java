package com.example.animal_feed.order;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Integer> {
    Optional<Orders> findById(int id);

    Slice<Orders> findByUserId(int userId, Pageable pageable);

    @Query("SELECT o FROM Orders o WHERE o.state = :state")
    Page<Orders> findByState(@Param("state") State state, Pageable pageable);

    @Query("SELECT o.state FROM Orders o WHERE o.id = :id")
    Optional<State> findOrderStateById(@Param("id") int id);

    @Modifying
    @Query("UPDATE Orders o SET o.state = :state WHERE o.id = :id")
    void updateOrderState(@Param("id") int id, @Param("state") State state);
    
    @Query("SELECT u.email FROM Users u JOIN Orders o ON u.id = o.userId WHERE o.id = :id")
    Optional<String> findEmailByOrderId(@Param("id") int id);
}
