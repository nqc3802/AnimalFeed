package com.example.animal_feed.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.animal_feed.user.CustomUserDetails;

@CrossOrigin
@RequestMapping("/api/v1/orders")
@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping
    public Slice<OrderDTO> getOrders(@AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return orderService.getOrders(userDetails.getId(), page, size);
    }

    @PostMapping
    public ResponseEntity<String> placeOrder(@AuthenticationPrincipal CustomUserDetails userDetails) {
        orderService.placeOrder(userDetails.getId());
        return ResponseEntity.status(201).body("Order placed successfully.");
    }

    @PatchMapping("/cancel")
    public ResponseEntity<String> cancelOrder(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam int orderId) {
        orderService.cancelOrder(userDetails.getId(), orderId);
        return ResponseEntity.status(200).body("Order canceled successfully.");
    }
}
