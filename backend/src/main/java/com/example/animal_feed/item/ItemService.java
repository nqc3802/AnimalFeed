package com.example.animal_feed.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.animal_feed.exception.ItemAlreadyExistsException;
import com.example.animal_feed.exception.ItemNotFoundException;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;

    @Cacheable(value = "items", key = "#page + '-' + #size")
    public Page<ItemsDTO> getAllItems(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Items> items = itemRepository.findAll(pageable);
        return ItemsMapper.INSTANCE.itemsToItemsDTOPage(items);
    }

    @Cacheable(value = "item", key = "#id")
    public ItemsDTO getItem(int id) {
        checkIfItemNotFound(id);
        Items item = itemRepository.findById(id);
        return ItemsMapper.INSTANCE.itemsToItemsDTO(item);
    }

    public ItemAddDTO addItem(ItemAddDTO item) {
        checkIfItemExists(item.getCode());

        Items newItem = ItemsMapper.INSTANCE.itemAddDTOToItems(item);
        try {
            itemRepository.save(newItem);
        } catch (DataIntegrityViolationException ex) {
            throw new ItemAlreadyExistsException("Item with code " + item.getCode() + " already exists.");
        }
        return ItemsMapper.INSTANCE.itemsToItemAddDTO(newItem);
    }

    public ItemEditDTO editItem(int id, ItemEditDTO item) {
        checkIfItemNotFound(id);
        Items currentItem = itemRepository.findById(id);
        Items editedItem = Items.builder()
                .id(id)
                .type(item.getType() != null ? item.getType() : currentItem.getType())
                .code(item.getCode() != null ? item.getCode() : currentItem.getCode())
                .img(item.getImg() != null ? item.getImg() : currentItem.getImg())
                .weight(item.getWeight() > 0 ? item.getWeight() : currentItem.getWeight())
                .price(item.getPrice() > 0 ? item.getPrice() : currentItem.getPrice())
                .state(item.getState() != null ? item.getState() : currentItem.getState())
                .description(item.getDescription() != null ? item.getDescription() : currentItem.getDescription())
                .build();
        try {
            itemRepository.save(editedItem);
        } catch (DataIntegrityViolationException ex) {
            throw new ItemAlreadyExistsException("Item with code " + item.getCode() + " already exists.");
        }
        return ItemsMapper.INSTANCE.itemsToItemEditDTO(editedItem);
    }

    public ItemDeleteDTO deleteItem(int id) {
        checkIfItemNotFound(id);
        Items item = itemRepository.findById(id);
        try {
            itemRepository.delete(item);
        } catch (DataIntegrityViolationException ex) {
            throw new ItemNotFoundException("Item not found.");
        }
        return ItemsMapper.INSTANCE.itemsToItemDeleteDTO(item);
    }

    private void checkIfItemExists(String code) {
        if (itemRepository.findOneByCode(code) != null) {
            throw new ItemAlreadyExistsException("Item with code " + code + " already exists.");
        }
    }

    private void checkIfItemNotFound(int id) {
        if (itemRepository.findById(id) == null) {
            throw new ItemNotFoundException("Item not found.");
        }
    }

}
