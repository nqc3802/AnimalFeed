package com.example.animal_feed.feedback;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedbacks, Integer> {
    Page<Feedbacks> findByItemId(int itemId, Pageable pageable);

    boolean existsByOrderId(int orderId);
}
