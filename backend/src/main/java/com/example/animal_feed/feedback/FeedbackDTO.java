package com.example.animal_feed.feedback;

import com.example.animal_feed.item.SimpleItemDTO;
import com.example.animal_feed.user.SimpleUserDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackDTO {
    private SimpleUserDTO user;
    private SimpleItemDTO item;
    private int rating;
    private String text;
}
