package com.example.animal_feed.feedback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RequestMapping("/api/v1/feedback")
@RestController
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;

    @GetMapping("/{itemId}")
    public ResponseEntity<Page<FeedbackDTO>> getFeedbacks(
            @PathVariable int itemId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<FeedbackDTO> feedbacks = feedbackService.getFeedbacks(itemId, page, size);
        return ResponseEntity.ok(feedbacks);
    }

}
