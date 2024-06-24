package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.StoreData;
import com.example.demo.repo.StoreRepo;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class StoreService {

    private final StoreRepo storeRepo;

    public StoreService(StoreRepo storeRepo) {
        this.storeRepo = storeRepo;
    }

    public List<StoreData> getAllStores() {
        return storeRepo.findAll();
    }
}
