package com.example.animal_feed.feedback;

import com.example.animal_feed.item.SimpleItemDTO;
import com.example.animal_feed.user.SimpleUserDTO;

public record FeedbackDTO(SimpleUserDTO user, SimpleItemDTO item, int rating, String text) {
}
