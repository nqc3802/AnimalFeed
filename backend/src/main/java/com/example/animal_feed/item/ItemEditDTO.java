package com.example.animal_feed.item;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemEditDTO {
    private int id;
    @NotBlank(message = "Type cannot be blank")
    private String type;
    @NotBlank(message = "Type cannot be blank")
    private String code;
    private String img;
    private int weight;
    private int price;
    private String state;
    private String description;
}
