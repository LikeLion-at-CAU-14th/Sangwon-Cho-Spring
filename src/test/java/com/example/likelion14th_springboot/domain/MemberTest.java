package com.example.likelion14th_springboot.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Member 단위 테스트")
class MemberTest {
    private Member member;

    @BeforeEach
    void setUp() {
        member = new Member();
    }

    @Nested
    @DisplayName("Member 예치금 테스트 그룹")
    class MemberTests {

        @Test
        @DisplayName("성공: 1200원이 된다")
        void ChargeTest() {
            int expected = 1500;
            Member member = Member.builder().deposit(1000).build();

            member.chargeDeposit(500);
            assertThat(member.getDeposit()).isEqualTo(expected);
            expected = 1200;
            member.useDeposit(300);
            assertThat(member.getDeposit()).isEqualTo(expected);
        }


    }
}
