package com.example.animal_feed.feedback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.animal_feed.exception.FeedbackExistsException;
import com.example.animal_feed.exception.OrderNotFoundException;
import com.example.animal_feed.exception.OrderStateException;
import com.example.animal_feed.exception.UnauthorizedFeedbackException;
import com.example.animal_feed.item.ItemRepository;
import com.example.animal_feed.item.SimpleItemDTO;
import com.example.animal_feed.order.OrderRepository;
import com.example.animal_feed.order.Orders;
import com.example.animal_feed.order.State;
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

    @Autowired
    private OrderRepository orderRepository;

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
                    feedback.getText());
        });
    }

    public FeedbackAddDTO addFeedback(int userId, FeedbackAddDTO feedbackAddDTO) {
        Orders order = orderRepository.findById(feedbackAddDTO.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException("Order with id " + feedbackAddDTO.getOrderId() + " does not exist."));

        if (order.getUserId() != userId) {
            throw new UnauthorizedFeedbackException("You can only feedback on your own orders.");
        }

        if (order.getState() != State.DELIVERED) {
            throw new OrderStateException("Only delivered orders can be feedbacked.");
        }

        if (feedbackRepository.existsByOrderId(order.getId())) {
            throw new FeedbackExistsException("Feedback already exists for order " + order.getId());
        }

        Feedbacks feedback = Feedbacks.builder()
                .userId(userId)
                .itemId(order.getItemId())
                .orderId(order.getId())
                .rating(feedbackAddDTO.getRating())
                .text(feedbackAddDTO.getText())
                .build();

        feedbackRepository.save(feedback);

        return new FeedbackAddDTO(
                feedback.getOrderId(),
                feedback.getRating(),
                feedback.getText());

    }

}
