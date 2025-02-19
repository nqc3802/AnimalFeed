package com.example.animal_feed.order;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.animal_feed.bill.BillRepository;
import com.example.animal_feed.bill.Bills;
import com.example.animal_feed.cart.CartRepository;
import com.example.animal_feed.cart.Carts;
import com.example.animal_feed.exception.CartEmptyException;
import com.example.animal_feed.exception.CartQuantityException;
import com.example.animal_feed.exception.ItemNotFoundException;
import com.example.animal_feed.exception.OrderNotFoundException;
import com.example.animal_feed.exception.OrderStateException;
import com.example.animal_feed.item.ItemRepository;
import com.example.animal_feed.item.Items;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private BillRepository billRepository;

    public Slice<OrderDTO> getOrders(int userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<Orders> orders = orderRepository.findByUserId(userId, pageable);
        return orders.map(OrdersMapper.INSTANCE::ordersToOrderDTO);
    }

    @Transactional
    public void placeOrder(int userId) {
        List<Carts> cartItems = cartRepository.findByUserId(userId);
        if (cartItems.isEmpty()) {
            throw new CartEmptyException("Cart is empty. Cannot place an order.");
        }

        int totalAmount = 0;
        List<Orders> ordersList = new ArrayList<>();

        for (Carts cart : cartItems) {
            checkIfItemNotFound(cart.getItemId());
            checkIfQuantityValid(cart.getQuantity());

            Items item = itemRepository.findById(cart.getItemId());
            if (item == null) {
                throw new ItemNotFoundException("Item with id " + cart.getItemId() + " not found.");
            }

            int price = item.getPrice();
            int amount = price * cart.getQuantity();
            totalAmount += amount;

            Orders order = Orders.builder()
                    .userId(userId)
                    .itemId(cart.getItemId())
                    .quantity(cart.getQuantity())
                    .price(price)
                    .amount(amount)
                    .state(State.ORDERED)
                    .build();

            ordersList.add(order);
        }

        Bills bill = new Bills();
        bill.setTotalAmount(totalAmount);
        bill.setDate(LocalDate.now());
        Bills savedBill = billRepository.save(bill);

        for (Orders order : ordersList) {
            order.setBillId(savedBill.getId());
        }
        orderRepository.saveAll(ordersList);

        cartRepository.deleteByUserId(userId);
    }

    @Transactional
    public void confirmOrder(int orderId) {
        State currentState = orderRepository.findOrderStateById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with id " + orderId + " not found."));
        
        if (currentState  == State.CONFIRMED) {
            throw new OrderStateException("Order is already confirmed.");
        }

        if (currentState  != State.ORDERED) {
            throw new OrderStateException("Order is not in ORDERED state.");
        }

        orderRepository.updateOrderState(orderId, State.CONFIRMED);
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

}
