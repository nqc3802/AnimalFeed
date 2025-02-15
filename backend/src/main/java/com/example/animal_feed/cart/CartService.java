package com.example.animal_feed.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    public Slice<CartDTO> getCarts(int userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<Carts> carts = cartRepository.findByUserId(userId, pageable);
        return carts.map(CartsMapper.INSTANCE::cartsToCartDTO);
    }
}
