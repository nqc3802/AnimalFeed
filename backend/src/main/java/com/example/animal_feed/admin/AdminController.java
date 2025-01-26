package com.example.animal_feed.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.animal_feed.item.ItemAddDTO;
import com.example.animal_feed.item.ItemDeleteDTO;
import com.example.animal_feed.item.ItemEditDTO;
import com.example.animal_feed.item.ItemService;
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

    @GetMapping
    public Page<UsersDTO> getAllUsers(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return userService.getAllUsers(page, size);
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
    public ResponseEntity<ItemDeleteDTO> deleteItem(@PathVariable int id) {
        ItemDeleteDTO deletedItem = itemService.deleteItem(id);
        return ResponseEntity.status(200).body(deletedItem);
    }
}
