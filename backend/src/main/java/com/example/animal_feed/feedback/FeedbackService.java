package com.example.animal_feed.feedback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.animal_feed.item.ItemRepository;
import com.example.animal_feed.item.SimpleItemDTO;
import com.example.animal_feed.user.SimpleUserDTO;
import com.example.animal_feed.user.UserRepository;
import com.example.animal_feed.user.Users;

@Service
public class FeedbackService {
    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    public Page<FeedbackDTO> getFeedbacks(int itemId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
    
        return feedbackRepository.findByItemId(itemId, pageable).map(feedback -> {
            Users user = userRepository.findById(feedback.getUserId());
            SimpleUserDTO userDTO = user != null ? new SimpleUserDTO(user.getId(), user.getName()) : null;
    
            SimpleItemDTO itemDTO = new SimpleItemDTO(itemId, itemRepository.findById(itemId).getCode());
    
            return new FeedbackDTO(
                userDTO,
                itemDTO,
                feedback.getRating(),
                feedback.getText()
            );
        });
    }
    
}
