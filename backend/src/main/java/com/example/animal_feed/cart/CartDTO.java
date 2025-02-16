package com.example.animal_feed.cart;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
    private int itemId;

    @Min(value = 1, message = "Số lượng sản phẩm phải lớn hơn hoặc bằng 1.")
    private int quantity;
}
