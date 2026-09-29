package com.example.likelion14th_springboot.repository;

import com.example.likelion14th_springboot.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByBuyer_IdAndDeletedFalse(Long buyerId);

    Optional<Orders> findByIdAndDeletedFalse(Long id);
}
