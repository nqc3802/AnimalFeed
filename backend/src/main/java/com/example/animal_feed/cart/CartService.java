package com.example.animal_feed.cart;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.example.animal_feed.exception.CartQuantityException;
import com.example.animal_feed.exception.ItemNotFoundException;
import com.example.animal_feed.item.ItemRepository;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ItemRepository itemRepository;

    public Slice<CartDTO> getCarts(int userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<Carts> carts = cartRepository.findByUserId(userId, pageable);
        return carts.map(CartsMapper.INSTANCE::cartsToCartDTO);
    }

    public CartDTO addToCart(int userId, CartDTO cartDTO) {
        checkIfItemNotFound(cartDTO.getItemId());
        checkIfQuantityValid(cartDTO.getQuantity());

        Optional<Carts> existingCart = cartRepository.findByUserIdAndItemId(userId, cartDTO.getItemId());

        Carts updatedCart;
        if (existingCart.isPresent()) {
            updatedCart = existingCart.get();
            updatedCart.setQuantity(updatedCart.getQuantity() + cartDTO.getQuantity());
        } else {
            updatedCart = CartsMapper.INSTANCE.cartDTOToCarts(cartDTO);
            updatedCart.setUserId(userId);
        }

        Carts savedCart = cartRepository.save(updatedCart);
        return CartsMapper.INSTANCE.cartsToCartDTO(savedCart);
    }

    public CartDTO updateCart(int userId, CartDTO cartDTO) {
        checkIfItemNotFound(cartDTO.getItemId());
        checkIfQuantityValid(cartDTO.getQuantity());

        Carts existingCart = getExistingCartOrThrow(userId, cartDTO.getItemId());

        existingCart.setQuantity(cartDTO.getQuantity());

        Carts savedCart = cartRepository.save(existingCart);
        return CartsMapper.INSTANCE.cartsToCartDTO(savedCart);
    }

    public CartDeleteDTO deleteCart(int userId, int itemId) {
        checkIfItemNotFound(itemId);

        Carts existingCart = getExistingCartOrThrow(userId, itemId);
        
        cartRepository.delete(existingCart);
        return CartsMapper.INSTANCE.cartsToCartDeleteDTO(existingCart);
    }

    private void checkIfItemNotFound(int itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new ItemNotFoundException("Item with id " + itemId + " not found.");
        }
    }

    private void checkIfQuantityValid(int quantity) {
        if (quantity < 1) {
            throw new CartQuantityException("Quantity must be greater than 0.");
        }
    }

    private Carts getExistingCartOrThrow(int userId, int itemId) {
        return cartRepository.findByUserIdAndItemId(userId, itemId)
                .orElseThrow(() -> new ItemNotFoundException("Item with id " + itemId + " not found in cart."));
    }

}
