package com.example.animal_feed.order;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface OrdersMapper {
    OrdersMapper INSTANCE = Mappers.getMapper(OrdersMapper.class);

    OrderDTO ordersToOrderDTO(Orders order);
    Orders orderDTOToOrders(OrderDTO orderDTO);

    OrderStateDTO ordersToOrderStateDTO(Orders order);
    Orders orderStateDTOToOrders(OrderStateDTO orderStateDTO);

    default Page<OrderDTO> ordersToOrderDTOPage(Page<Orders> orders) {
        return orders.map(this::ordersToOrderDTO);
    }
    default Page<Orders> orderDTOToOrdersPage(Page<OrderDTO> orderDTO) {
        return orderDTO.map(this::orderDTOToOrders);
    }
}
