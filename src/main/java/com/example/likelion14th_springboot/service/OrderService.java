package com.example.likelion14th_springboot.service;

import com.example.likelion14th_springboot.domain.Member;
import com.example.likelion14th_springboot.domain.Orders;
import com.example.likelion14th_springboot.domain.Product;
import com.example.likelion14th_springboot.domain.mapping.ProductOrders;
import com.example.likelion14th_springboot.dto.request.OrderCreateRequestDto;
import com.example.likelion14th_springboot.dto.request.OrderShippingUpdateRequestDto;
import com.example.likelion14th_springboot.dto.response.OrderResponseDto;
import com.example.likelion14th_springboot.repository.MemberRepository;
import com.example.likelion14th_springboot.repository.OrdersRepository;
import com.example.likelion14th_springboot.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrdersRepository ordersRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    @Transactional
    public OrderResponseDto createOrder(OrderCreateRequestDto dto) {
        // 구매자 조회
        Member buyer = memberRepository.findById(dto.getBuyerId())
                .orElseThrow(() -> new IllegalArgumentException("구매자가 없습니다."));

        // 상품 조회
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품이 없습니다."));

        if (dto.getQuantity() == null || dto.getQuantity() <= 0) {
            throw new IllegalArgumentException("주문 수량은 1개 이상이어야 합니다.");
        }


        if (product.getStock() < dto.getQuantity()) {
            throw new IllegalArgumentException("상품 재고가 부족합니다.");
        }

        long totalPrice = (long) product.getPrice() * dto.getQuantity();

        if (buyer.getDeposit() < totalPrice) {
            throw new IllegalArgumentException("계좌 잔액이 부족합니다.");
        }

        product.reduceStock(dto.getQuantity());
        buyer.useDeposit(Math.toIntExact(totalPrice));

        Orders order = dto.toEntity(buyer);

        ProductOrders item = ProductOrders.builder()
                .orders(order)
                .product(product)
                .quantity(dto.getQuantity())
                .build();

        order.getProductOrders().add(item);

        Orders saved = ordersRepository.save(order);


        return OrderResponseDto.fromEntity(saved);
    }


    // 구매자의 주문 목록
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getOrdersByBuyer(Long buyerId) {
        return ordersRepository.findByBuyer_IdAndDeletedFalse(buyerId).stream()
                .map(OrderResponseDto::fromEntity)
                .toList();
    }

    // 주문 한 건
    @Transactional(readOnly = true)
    public OrderResponseDto getOrder(Long orderId) {
        Orders order = ordersRepository.findByIdAndDeletedFalse(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 없습니다."));

        return OrderResponseDto.fromEntity(order);
    }


    @Transactional
    public OrderResponseDto updateShippingAddress(
            Long orderId,
            OrderShippingUpdateRequestDto dto
    ) {
        Orders order = ordersRepository.findByIdAndDeletedFalse(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 없습니다."));

        order.updateShippingAddress(dto.toShippingAddress());

        return OrderResponseDto.fromEntity(order);
    }

    @Transactional
    public void deleteOrder(Long orderId) {
        Orders order = ordersRepository.findByIdAndDeletedFalse(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 없습니다."));

        order.softDelete();
    }
}
