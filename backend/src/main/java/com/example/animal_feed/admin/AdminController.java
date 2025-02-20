package com.example.animal_feed.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.animal_feed.email.EmailService;
import com.example.animal_feed.item.ItemAddDTO;
import com.example.animal_feed.item.ItemEditDTO;
import com.example.animal_feed.item.ItemService;
import com.example.animal_feed.order.OrderRequestDTO;
import com.example.animal_feed.order.OrderService;
import com.example.animal_feed.user.UserEditRoleDTO;
import com.example.animal_feed.user.UserService;
import com.example.animal_feed.user.UsersDTO;

@CrossOrigin
@RequestMapping("/api/v1/admin")
@RestController
public class AdminController {
    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private EmailService emailService;

    @GetMapping
    public Page<UsersDTO> getAllUsers(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return userService.getAllUsers(page, size);
    }

    @PutMapping("/user/deactivate/{id}")
    public ResponseEntity<String> deactivateUser(@PathVariable int id) {
        userService.deactivateUser(id);
        return ResponseEntity.status(200).body("User " + id + " deactivated successfully");
    }

    @PutMapping("/user/activate/{id}")
    public ResponseEntity<String> activateUser(@PathVariable int id) {
        userService.activateUser(id);
        return ResponseEntity.status(200).body("User " + id + " activated successfully");
    }

    @PutMapping("/user/edit-role/{id}")
    public ResponseEntity<UserEditRoleDTO> editUserRole(@PathVariable int id, @RequestBody UserEditRoleDTO roles) {
        userService.editUserRole(id, roles.getRole());
        return ResponseEntity.status(200).body(roles);
    }

    @PostMapping("/item/add")
    public ResponseEntity<ItemAddDTO> addItem(@RequestBody ItemAddDTO item) {
        ItemAddDTO savedItem = itemService.addItem(item);
        return ResponseEntity.status(201).body(savedItem);
    }

    @PutMapping("/item/{id}")
    public ResponseEntity<ItemEditDTO> editItem(@PathVariable int id, @RequestBody ItemEditDTO item) {
        item.setId(id);
        ItemEditDTO savedItem = itemService.editItem(id, item);
        return ResponseEntity.status(200).body(savedItem);
    }

    @DeleteMapping("/item/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable int id) {
        itemService.deleteItem(id);
        return ResponseEntity.status(200).body("Item " + id + " deleted successfully.");
    }

    @GetMapping("/orders")
    public Page<OrderRequestDTO> getOrders(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return orderService.getOrders(page, size);
    }

    @PatchMapping("/orders/confirm")
    public ResponseEntity<String> confirmOrder(@RequestParam int id) {
        orderService.confirmOrder(id);
        try {
            String email = orderService.getEmail(id);
            if (email != null && !email.trim().isEmpty()) {
                emailService.sendEmail(email, "Order confirmed", "Your order has been confirmed.");
            }
            return ResponseEntity.status(200).body("Order confirmed successfully.");
        } catch (MailException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred, please try again later");
        }
    }

    @PatchMapping("/orders/reject")
    public ResponseEntity<String> rejectOrder(@RequestParam int id) {
        orderService.rejectOrder(id);
        try {
            String email = orderService.getEmail(id);
            if (email != null && !email.trim().isEmpty()) {
                emailService.sendEmail(email, "Order rejected", "Your order has been rejected.");
            }
            return ResponseEntity.status(200).body("Order rejected successfully.");
        } catch (MailException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred, please try again later");
        }
    }

    @GetMapping("/returns")
    public Page<OrderRequestDTO> getReturns(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return orderService.getReturns(page, size);
    }

}
