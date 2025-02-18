package com.example.animal_feed.order;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private int itemId;
    @Min(value = 1, message = "Quantity must be greater than 0")
    private int quantity;
    private int price;
    private int amount;
    private State state;
}
