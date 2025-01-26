package com.example.animal_feed.item;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemAddDTO {
    @NotBlank(message = "Type cannot be blank")
    private String type;
    @NotBlank(message = "Code cannot be blank")
    private String code;
    private String img = "";
    private int weight = 0;
    private int price;
    private String state = "Available";
    private String description = "";
}
