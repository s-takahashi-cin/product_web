package com.example.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.form.OrderForm;

@Repository
public interface OrderFormRepo extends JpaRepository<OrderForm, Long> {
    List<OrderForm> findByStoreId(Long storeId);
}

