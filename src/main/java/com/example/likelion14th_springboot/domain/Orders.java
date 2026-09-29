package com.example.likelion14th_springboot.domain;

import com.example.likelion14th_springboot.domain.mapping.ProductOrders;
import com.example.likelion14th_springboot.enums.DeliverStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Orders extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private DeliverStatus deliverStatus; // 배송상태

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private Member buyer;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
    private List<ProductOrders> productOrders;

    @OneToOne(mappedBy = "orders", cascade = CascadeType.ALL)
    private Coupon coupon;

    @Embedded
    private ShippingAddress shippingAddress;

    @Embeddable
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShippingAddress {
        private String recipient;//수령인
        private String phoneNumber; //전화번호
        private String streetAddress;//도로명주소
        private String detailAddress; //상세주소
        private String postalCode; //우편번호
    }

    public void updateShippingAddress(ShippingAddress newAddress) {
        if (this.deliverStatus != DeliverStatus.PREPARATION) {
            throw new IllegalArgumentException("배송 준비 중에만 배송정보를 수정할 수 있습니다.");
        }

        this.shippingAddress = newAddress;
    }

    @Builder.Default
    private boolean deleted = false;

    public void softDelete() {
        if (this.deliverStatus != DeliverStatus.COMPLETED) {
            throw new IllegalArgumentException("배송 완료된 주문만 삭제할 수 있습니다.");
        }

        this.deleted = true;
    }


}
