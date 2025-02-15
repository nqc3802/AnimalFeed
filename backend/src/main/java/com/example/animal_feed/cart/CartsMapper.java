package com.example.animal_feed.cart;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface CartsMapper {
    CartsMapper INSTANCE = Mappers.getMapper(CartsMapper.class);

    CartDTO cartsToCartDTO(Carts cart);
    Carts cartDTOToCarts(CartDTO cartDTO);

    default Page<CartDTO> cartsToCartDTOPage(Page<Carts> carts) {
        return carts.map(this::cartsToCartDTO);
    }
    default Page<Carts> cartDTOToCartsPage(Page<CartDTO> cartDTO) {
        return cartDTO.map(this::cartDTOToCarts);
    }
}
