package com.example.demo.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.OrderDetails;

public interface OrderDetailRepo extends JpaRepository<OrderDetails, Long> {
    List<OrderDetails> findByOrderFormId(Long orderFormId);
}