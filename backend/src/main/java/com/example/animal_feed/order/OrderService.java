package com.example.animal_feed.order;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.animal_feed.bill.BillRepository;
import com.example.animal_feed.bill.Bills;
import com.example.animal_feed.cart.CartRepository;
import com.example.animal_feed.cart.Carts;
import com.example.animal_feed.exception.BillNotFoundException;
import com.example.animal_feed.exception.CartEmptyException;
import com.example.animal_feed.exception.CartQuantityException;
import com.example.animal_feed.exception.ItemNotFoundException;
import com.example.animal_feed.exception.OrderNotFoundException;
import com.example.animal_feed.exception.OrderNotLinkedException;
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
    public void cancelOrder(int userId, int orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with id " + orderId + " not found."));

        if (order.getState() == State.CANCELLED) {
            throw new OrderStateException("Order is already canceled.");
        }

        if (order.getState() != State.ORDERED) {
            throw new OrderStateException("Order is not in ORDERED state, cannot cancel.");
        }

        orderRepository.updateOrderState(orderId, State.CANCELLED);

        updateBillTotalAmount(order);
    }

    public Page<OrderRequestDTO> getOrders(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Orders> orders = orderRepository.findByState(State.ORDERED, pageable);
        return orders.map(OrdersMapper.INSTANCE::ordersToOrderRequestDTO);
    }

    @Transactional
    public void confirmOrder(int orderId) {
        State currentState = orderRepository.findOrderStateById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with id " + orderId + " not found."));

        if (currentState == State.CONFIRMED) {
            throw new OrderStateException("Order is already confirmed.");
        }

        if (currentState != State.ORDERED) {
            throw new OrderStateException("Order is not in ORDERED state.");
        }

        orderRepository.updateOrderState(orderId, State.CONFIRMED);
    }

    @Transactional
    public void rejectOrder(int orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with id " + orderId + " not found."));

        if (order.getState() == State.REJECTED) {
            throw new OrderStateException("Order is already rejected.");
        }

        if (order.getState() != State.ORDERED) {
            throw new OrderStateException("Order is not in ORDERED state.");
        }

        orderRepository.updateOrderState(orderId, State.REJECTED);

        updateBillTotalAmount(order);
    }

    @Transactional
    public void requestExchange(int userId, int orderId) {
        State currentState = orderRepository.findOrderStateById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with id " + orderId + " not found."));

        if (currentState == State.EXCHANGE_REQUESTED) {
            throw new OrderStateException("Exchange is already requested.");
        }

        if (currentState != State.DELIVERED) {
            throw new OrderStateException("Order is not delivered yet, cannot request exchange.");
        }

        orderRepository.updateOrderState(orderId, State.EXCHANGE_REQUESTED);
    }

    @Transactional
    public void requestReturn(int userId, int orderId) {
        State currentState = orderRepository.findOrderStateById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with id " + orderId + " not found."));

        if (currentState == State.RETURN_REQUESTED) {
            throw new OrderStateException("Return is already requested.");
        }

        if (currentState != State.DELIVERED) {
            throw new OrderStateException("Order is not delivered yet, cannot request return.");
        }

        orderRepository.updateOrderState(orderId, State.RETURN_REQUESTED);
    }

    public Page<OrderRequestDTO> getReturns(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Orders> returns = orderRepository.findByState(State.RETURN_REQUESTED, pageable);
        return returns.map(OrdersMapper.INSTANCE::ordersToOrderRequestDTO);
    }

    @Transactional
    public void approveReturn(int orderId) {
        State currentState = orderRepository.findOrderStateById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with id " + orderId + " not found."));
        

        if (currentState == State.RETURN_APPROVED) {
            throw new OrderStateException("Return is already approved.");
        }

        if (currentState != State.RETURN_REQUESTED) {
            throw new OrderStateException("Order is not in RETURN_REQUESTED state.");
        }

        orderRepository.updateOrderState(orderId, State.RETURN_APPROVED);
    }

    @Transactional
    public void rejectReturn(int orderId) {
        State currentState = orderRepository.findOrderStateById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with id " + orderId + " not found."));

        if (currentState == State.RETURN_REJECTED) {
            throw new OrderStateException("Return is already rejected.");
        }

        if (currentState != State.RETURN_REQUESTED) {
            throw new OrderStateException("Order is not in RETURN_REQUESTED state.");
        }

        orderRepository.updateOrderState(orderId, State.RETURN_REJECTED);
    }

    public Page<OrderRequestDTO> getExchanges(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Orders> exchanges = orderRepository.findByState(State.EXCHANGE_REQUESTED, pageable);
        return exchanges.map(OrdersMapper.INSTANCE::ordersToOrderRequestDTO);
    }

    @Transactional
    public void approveExchange(int orderId) {
        State currentState = orderRepository.findOrderStateById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with id " + orderId + " not found."));

        if (currentState == State.EXCHANGE_APPROVED) {
            throw new OrderStateException("Exchange is already approved.");
        }

        if (currentState != State.EXCHANGE_REQUESTED) {
            throw new OrderStateException("Order is not in EXCHANGE_REQUESTED state.");
        }

        orderRepository.updateOrderState(orderId, State.EXCHANGE_APPROVED);
    }

    @Transactional
    public void rejectExchange(int orderId) {
        State currentState = orderRepository.findOrderStateById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with id " + orderId + " not found."));

        if (currentState == State.EXCHANGE_REJECTED) {
            throw new OrderStateException("Exchange is already rejected.");
        }

        if (currentState != State.EXCHANGE_REQUESTED) {
            throw new OrderStateException("Order is not in EXCHANGE_REQUESTED state.");
        }

        orderRepository.updateOrderState(orderId, State.EXCHANGE_REJECTED);
    }

    public String getEmail(int orderId) {
        return orderRepository.findEmailByOrderId(orderId).orElse(null);
    }

    private void updateBillTotalAmount(Orders order) {
        if (order.getBillId() == 0) {
            throw new OrderNotLinkedException("Order is not linked to a valid bill.");
        }

        Bills bill = billRepository.findById(order.getBillId())
                .orElseThrow(() -> new BillNotFoundException("Bill with id " + order.getBillId() + " not found."));

        bill.setTotalAmount(bill.getTotalAmount() - order.getAmount());

        billRepository.save(bill);
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
