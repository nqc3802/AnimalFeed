package com.example.animal_feed.feedback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.animal_feed.user.CustomUserDetails;

@CrossOrigin
@RequestMapping("/api/v1/reviews")
@RestController
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;

    @GetMapping("/view/{itemId}")
    public ResponseEntity<Page<FeedbackDTO>> getFeedbacks(
            @PathVariable int itemId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<FeedbackDTO> feedbacks = feedbackService.getFeedbacks(itemId, page, size);
        return ResponseEntity.ok(feedbacks);
    }

    @PostMapping("/add")
    public ResponseEntity<FeedbackAddDTO> addFeedback(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody FeedbackAddDTO feedback) {
        FeedbackAddDTO savedFeedback = feedbackService.addFeedback(userDetails.getId(), feedback);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFeedback);
    }

}
