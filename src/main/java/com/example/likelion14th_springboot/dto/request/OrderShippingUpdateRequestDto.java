package com.example.likelion14th_springboot.dto.request;

import com.example.likelion14th_springboot.domain.Orders;
import lombok.Getter;

@Getter
public class OrderShippingUpdateRequestDto {
    private String recipient;
    private String phoneNumber;
    private String streetAddress;
    private String detailAddress;
    private String postalCode;

    public Orders.ShippingAddress toShippingAddress() {
        return new Orders.ShippingAddress(
                recipient, phoneNumber, streetAddress, detailAddress, postalCode
        );
    }
}
