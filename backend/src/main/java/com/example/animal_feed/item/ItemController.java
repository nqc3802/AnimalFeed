package com.example.animal_feed.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RequestMapping("/api/v1/item")
@RestController
public class ItemController {
    @Autowired
    private ItemService itemService;

    @GetMapping
    public Page<ItemsDTO> getAllItems(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return itemService.getAllItems(page, size);
    }

    @GetMapping("/{id}")
    public ItemsDTO getItem(@PathVariable int id) {
        return itemService.getItem(id);
    }
}
