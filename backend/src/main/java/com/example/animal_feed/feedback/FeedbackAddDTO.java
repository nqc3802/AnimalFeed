package com.example.animal_feed.feedback;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackAddDTO {
    private int orderId;
    private int rating;
    private String text;
}
