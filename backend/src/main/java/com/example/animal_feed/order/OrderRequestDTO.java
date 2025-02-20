package com.example.animal_feed.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {
    private int id;
    private int billId;
    private int userId;
    private int itemId;
    private int quantity;
    private int price;
    private int amount;
    private State state;
}
