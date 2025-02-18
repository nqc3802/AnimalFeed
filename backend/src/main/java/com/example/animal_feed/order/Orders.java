package com.example.animal_feed.order;

import jakarta.persistence.Column;
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

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "bill_id")
    private int billId;
    @Column(name = "user_id")
    private int userId;
    @Column(name = "item_id")
    private int itemId;
    @Builder.Default
    private int quantity = 1;
    private int price;
    private int amount;
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private State state = State.ORDERED;
}
