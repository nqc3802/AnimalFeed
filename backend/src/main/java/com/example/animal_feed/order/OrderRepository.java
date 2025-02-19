package com.example.animal_feed.order;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Integer>{
    Slice<Orders> findByUserId(int userId, Pageable pageable);
    
    @Query("SELECT o.state FROM Orders o WHERE o.id = :id")
    Optional<State> findOrderStateById(@Param("id") int id);
    
    @Modifying
    @Query("UPDATE Orders o SET o.state = :state WHERE o.id = :id")
    void updateOrderState(@Param("id") int id, @Param("state") State state);
}
