package com.example.demo.repo;

import com.example.demo.entity.ProductStorePrice;
import com.example.demo.entity.Products;
import com.example.demo.entity.StoreData;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductStoreRepo extends JpaRepository<ProductStorePrice, Long> {
    ProductStorePrice findByProductAndStoreData(Products product, StoreData storeData);

}