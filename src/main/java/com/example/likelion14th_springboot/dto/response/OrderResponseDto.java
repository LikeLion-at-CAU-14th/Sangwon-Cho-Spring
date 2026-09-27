package com.example.likelion14th_springboot.dto.response;

import com.example.likelion14th_springboot.domain.Orders;
import com.example.likelion14th_springboot.domain.mapping.ProductOrders;
import com.example.likelion14th_springboot.enums.DeliverStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderResponseDto {
    private Long id;
    private Long buyerId;
    private Long productId;
    private Integer quantity;
    private DeliverStatus deliverStatus;
    private Orders.ShippingAddress shippingAddress;

    public static OrderResponseDto fromEntity(Orders order) {
        ProductOrders item = order.getProductOrders().get(0);
        return OrderResponseDto.builder()
                .id(order.getId())
                .buyerId(order.getBuyer().getId())
                .productId(item.getProduct().getId())
                .quantity(item.getQuantity())
                .deliverStatus(order.getDeliverStatus())
                .shippingAddress(order.getShippingAddress())
                .build();
    }
}
