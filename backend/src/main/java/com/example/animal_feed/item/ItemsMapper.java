package com.example.animal_feed.item;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface ItemsMapper {
    ItemsMapper INSTANCE = Mappers.getMapper(ItemsMapper.class);

    ItemsDTO itemsToItemsDTO(Items item);
    Items itemsDTOToItems(ItemsDTO itemDTO);

    ItemAddDTO itemsToItemAddDTO(Items item);
    Items itemAddDTOToItems(ItemAddDTO itemAddDTO);

    ItemEditDTO itemsToItemEditDTO(Items item);
    Items itemEditDTOToItems(ItemEditDTO itemEditDTO);

    ItemDeleteDTO itemsToItemDeleteDTO(Items item);
    Items itemDeleteDTOToItems(ItemDeleteDTO itemDeleteDTO);

    SimpleItemDTO itemsToSimpleItemDTO(Items item);
    Items simpleItemDTOToItems(SimpleItemDTO simpleItemDTO);

    default Page<ItemsDTO> itemsToItemsDTOPage(Page<Items> items) {
        return items.map(this::itemsToItemsDTO);
    }
    default Page<Items> itemsDTOToItemsPage(Page<ItemsDTO> itemsDTO) {
        return itemsDTO.map(this::itemsDTOToItems);
    }
}
