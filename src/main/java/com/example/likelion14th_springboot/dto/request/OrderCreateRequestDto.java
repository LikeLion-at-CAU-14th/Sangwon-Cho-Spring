package com.example.likelion14th_springboot.dto.request;

import com.example.likelion14th_springboot.domain.Member;
import com.example.likelion14th_springboot.domain.Orders;
import com.example.likelion14th_springboot.enums.DeliverStatus;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class OrderCreateRequestDto {

    private Long buyerId;
    private Long productId;
    private Integer quantity;
    private String recipient;
    private String phoneNumber;
    private String streetAddress;
    private String detailAddress;
    private String postalCode;

    public Orders toEntity(Member buyer) {
        Orders.ShippingAddress address = new Orders.ShippingAddress(
                recipient,
                phoneNumber,
                streetAddress,
                detailAddress,
                postalCode
        );

        return Orders.builder()
                .buyer(buyer)
                .deliverStatus(DeliverStatus.PREPARATION)
                .shippingAddress(address)
                .productOrders(new ArrayList<>())
                .build();
    }
}
