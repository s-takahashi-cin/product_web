package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Products;

import java.util.List;

public interface ProductRepo extends JpaRepository<Products, Long> {
    List<Products> findByProductCategoryId(Long productCategoryId);
}

