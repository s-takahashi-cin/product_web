package com.example.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.StoreData;

public interface StoreRepo extends JpaRepository<StoreData, Long> {
    List<StoreData> findAll();
    StoreData getStoreDataById(Long id);
}
