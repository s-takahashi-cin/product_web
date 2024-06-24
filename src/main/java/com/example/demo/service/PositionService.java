package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.PositionData;
import com.example.demo.repo.PositionRepo;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PositionService {

    private final PositionRepo positionRepo;

    public PositionService(PositionRepo positionRepo) {
        this.positionRepo = positionRepo;
    }

    public List<PositionData> getAllPositions() {
        return positionRepo.findAll();
    }
}
