package com.example.animal_feed.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.animal_feed.user.CustomUserDetails;

import jakarta.validation.Valid;

@CrossOrigin
@RequestMapping("/api/v1/cart")
@RestController
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping
    public Slice<CartDTO> getCarts(@AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return cartService.getCarts(userDetails.getId(), page, size);
    }

    @PostMapping
    public ResponseEntity<CartDTO> addToCart(@AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CartDTO cart) {
        CartDTO savedCart = cartService.addToCart(userDetails.getId(), cart);
        return ResponseEntity.status(201).body(savedCart);
    }

    @PutMapping
    public ResponseEntity<CartDTO> updateCart(@AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CartDTO cart) {
        CartDTO updatedCart = cartService.updateCart(userDetails.getId(), cart);
        return ResponseEntity.status(200).body(updatedCart);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteCart(@AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CartDeleteDTO cart) {
        cartService.deleteCart(userDetails.getId(), cart.getItemId());
        return ResponseEntity.status(200).body("Cart deleted successfully");
    }

}
